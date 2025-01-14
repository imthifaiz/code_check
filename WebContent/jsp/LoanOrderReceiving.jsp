<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>

<%--New page design begin --%>
<%
	String title = "Goods Receipt By Rental Order";
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.RENTAL_CONSIGNMENT%>"/>
    <jsp:param name="submenu" value="<%=IConstants.RENTAL_CONSIGNMENT_TRANSACTION%>"/>
</jsp:include>
<%--New page design end --%>



<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;

function popUpWin(URL) {
 subWin = window.open(URL, 'LoanOrderPicking', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function sf()
{
document.form.DONO.focus();
}


function validatePO(form)
{

	if (form.DONO.value.length < 1)
  {
    alert("Please Enter Order Number !");
    form.DONO.focus();
    return false;
  }
 
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



function onNew(form)
{
    document.form.DONO.value   ="DUMMY"
    document.form.CUST_NAME.value       ="DUMMY"
    form.JOB_NUM.value         =""
    form.PERSON_INCHARGE.value =""
    form.CONTACT_NUM.value     =""
    form.ADDRESS.value         =""
    form.ADDRESS2.value        =""
    form.ADDRESS3.value        =""
    form.DELDATE.value         =""
    form.COLLECTION_TIME.value =""
    form.REMARK1.value         =""
    form.REMARK2.value         =""
    form.FRLOC.value         =""
    form.TOLOC.value         =""
    return true;
    
}


function onReceive(form){
	
 var ischeck = false;
 var Traveler ;
 var concatTraveler="";
 var j=0;
  var pono=document.form.DONO.value;
  var i = 0;
  var noofcheckbox=1;
   if(pono==null||pono=="")
	{
	alert("Please Enter/Select Order Number");	
	return false;
	}    

   if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
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
               alert("Please Select Product For Receiving");
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
    //alert("Hi 1");
    return true;
  }
 function submitFormValues(){
	 
     document.form.action ="/track/LoanOrderReceivingServlet?action=Receiving";
     document.form.submit();  
     }

</SCRIPT>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />

<jsp:useBean id="du" class="com.track.util.DateUtils" />

<%
	session = request.getSession();
   String plant=(String)session.getAttribute("PLANT");
   String pono     = su.fString(request.getParameter("DONO"));
   String action   = su.fString(request.getParameter("action")).trim();
   String sUserId = (String) session.getAttribute("LOGIN_USER");
   String AFLAG=    (String) session.getAttribute("AFLAG");
   boolean confirm = false;
    
   LoanUtil _loanUtil=new LoanUtil();
   ItemMstDAO _ItemMstDAO=new ItemMstDAO();
   _loanUtil.setmLogger(mLogger);
   _ItemMstDAO.setmLogger(mLogger);
   
    String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
    String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
    String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",chkString ="",frLoc="",toLoc="";
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
         frLoc=(String)m.get("frLoc");
         toLoc=(String)m.get("toLoc");
       
        
           
      }
      else 
      {
        fieldDesc="Details not found for Rental order:"+ pono;
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
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
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
		</div>

		<div class="box-body">
			<%--New page design end --%>

			<form class="form-horizontal" name="form" method="post"
				action="/track/LoanOrderReceivingServlet?">

				<center>
					<h2>
						<small> <%=fieldDesc%></small>
					</h2>
				</center>


				<div class="form-group">
					<label class="control-label col-sm-4" for="loan Order"> 
					<i class="glyphicon glyphicon-star" style="font-size: 10px; top: -6px; color: #e50000"></i>&nbsp;Order
						Number:
					</label>
					<div class="col-sm-4">
						<div class="input-group">
							<INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20"
								name="DONO" value="<%=pono%>"
								onkeypress="if((event.keyCode=='13') && ( document.form.DONO.value.length > 0)){loadLoanOrderDetails();}" />
							<span class="input-group-addon"
								onClick="javascript:popUpWin('list/loan_hdrlist.jsp?DONO='+form.DONO.value);">
								<a href="#" data-toggle="tooltip" data-placement="top"
								title="Rental Details"> <i
									class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
							</span>
						</div>
					</div>
					<div class="form-inline">
						<div class="col-sm-1 ">
							<button type="button" class="Submit btn btn-default"
								onClick="viewLoanOrders();">
								<b>View</b>
							</button>
						</div>
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-sm-4" for="Assignee name">Customer Name:</label>
					<div class="col-sm-4">
						<INPUT name="CUST_NAME" class="form-control" readonly type="TEXT"
							value="<%=su.forHTMLTag(custName)%>" size="30" MAXLENGTH=80>
					</div>
				</div>

				<INPUT type="hidden" name="CUST_CODE" value="<%=custCode%>">
				<INPUT type="hidden" name="LOGIN_USER" value="<%=sUserId%>">
				<INPUT type="hidden" name="CUST_CODE1" value="<%=custCode%>">

				<div class="form-group">
					<label class="control-label col-sm-4" for="Person Incharge">Contact Name:</label>
					<div class="col-sm-4">
						<INPUT type="TEXT" size="30" MAXLENGTH="20" class="form-control"
							readonly name="PERSON_INCHARGE" value="<%=personIncharge%>">
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-sm-4" for="Reference No">Reference No:</label>
					<div class="col-sm-4">
						<INPUT type="TEXT" size="30" MAXLENGTH=20 name="JOB_NUM"
							class="form-control" readonly value="<%=jobNum%>">
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-sm-4" for="Order Date">Order Date:</label>
					<div class="col-sm-4">
						<INPUT type="TEXT" class="form-control" readonly size="30"
							MAXLENGTH="10" name="DELDATE" value="<%=deldate%>" />
					</div>
					<INPUT type="Hidden" size="20" MAXLENGTH=20 name="CONTACT_NUM"
						value="<%=contactNum%>">
					<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Telephone">Telephone:</label>
        <div class="col-sm-3"> -->
					<INPUT class="form-control" name="TELNO" type="hidden"
						value="<%=telno%>" size="30" MAXLENGTH=100 readonly>
					<!-- </div>
 		</div> -->
				</div>

				<div class="form-group">
					<label class="control-label col-sm-4" for="Time">Order Time:</label>
					<div class="col-sm-4">
						<INPUT type="TEXT" size="30" class="form-control" readonly
							MAXLENGTH="20" name="COLLECTION_TIME" value="<%=collectionTime%>" />
					</div>
					<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Email Address">Email:</label>
        <div class="col-sm-3"> -->
					<INPUT type="hidden" size="30" class="form-control" readonly
						MAXLENGTH="20" name="EMAIL" value="<%=email%>" />
					<!-- </div>
 		</div> -->
				</div>

				<div class="form-group">
					<label class="control-label col-sm-4" for="From Location">From Location:</label>
					<div class="col-sm-4">
						<INPUT type="TEXT" size="20" class="form-control" MAXLENGTH="30"
							readonly name="FRLOC" value="<%=frLoc%>" />
					</div>
					<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Unit Number">Unit No:</label>
        <div class="col-sm-3"> -->
					<INPUT type="hidden" size="30" class="form-control" readonly
						MAXLENGTH="20" name="ADD1" value="<%=add1%>" />
					<!-- </div>
 		</div> -->
				</div>

				<div class="form-group">
					<label class="control-label col-sm-4" for="To Location">To Location:</label>
					<div class="col-sm-4">
						<INPUT type="TEXT" size="30" MAXLENGTH="20" class="form-control"
							readonly name="TOLOC" value="<%=toLoc%>" />
					</div>
					<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Buiding Name">Building:</label>
        <div class="col-sm-3"> -->
					<INPUT type="hidden" size="30" MAXLENGTH="20" class="form-control"
						readonly name="ADD2" value="<%=add2%>" />
					<!-- </div>
 		</div> -->
				</div>

				<div class="form-group">
					<label class="control-label col-sm-4" for="Remarks">Remarks:</label>
					<div class="col-sm-4">
						<INPUT type="TEXT" size="30" MAXLENGTH="20" name="REMARK1"
							class="form-control" readonly value="<%=remark1%>" />
					</div>
					<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="street">Street:</label>
        <div class="col-sm-3"> -->
					<INPUT type="hidden" size="30" MAXLENGTH="20" class="form-control"
						readonly name="ADD3" value="<%=add3%>" />
					<!-- </div>
 		</div> -->
				</div>

				<!-- <div class="form-group">
    	<div class="form-inline">
    	<label class="control-label col-sm-8" for="City">City:</label>
        <div class="col-sm-3"> -->
				<INPUT type="hidden" size="30" MAXLENGTH="20" class="form-control"
					readonly name="ADD4" value="<%=add4%>" />
				<!-- </div>
 		</div>
 		</div> -->

				<!-- <div class="form-group">
 		<div class="form-inline">
    	<label class="control-label col-sm-8" for="State">State:</label>
        <div class="col-sm-3"> -->
				<INPUT type="hidden" size="30" MAXLENGTH="20" class="form-control"
					readonly name="" value="" />
				<!-- </div>
 		</div> 
 	 	</div> -->

				<!-- <div class="form-group">
    	<div class="form-inline">
    	<label class="control-label col-sm-8" for="country">Country:</label>
        <div class="col-sm-3"> -->
				<INPUT type="hidden" size="30" class="form-control" MAXLENGTH="20"
					name="COUNTRY" readonly value="<%=country%>" />
				<!-- </div>
 		</div>
 		</div> -->

				<!-- <div class="form-group">
        <div class="form-inline">
    	<label class="control-label col-sm-8" for="postal code">Postal Code:</label>
        <div class="col-sm-3"> -->
				<INPUT type="hidden" size="30" class="form-control" MAXLENGTH="20"
					name="ZIP" readonly value="<%=zip%>" />
				<!-- </div>
 		</div>
 		</div> -->

				<!-- <div class="form-group">
        <div class="form-inline">
    	<label class="control-label col-sm-8" for="Assignee remarks">Assignee Remarks:</label>
        <div class="col-sm-3"> -->
				<INPUT type="hidden" size="30" class="form-control" readonly
					MAXLENGTH=20 name="REMARK2" value="<%=remarks%>">
				<!-- </div>
 		</div>
 		</div> -->

				<TABLE BORDER="0" CELLSPACING="0" WIDTH="100%" class="table">
					<thead style="background: #eaeafa; font-size: 15px">
						<tr>
							<th width="8%">Select</th>
							<th width="10%">Line No</th>
							<th width="15%">Product ID</th>
							<th width="20%">Description</th>
							<th width="15%">Order Quantity</th>
							<th width="14%">Pick Quantity</th>
							<th width="10%">Recv Quantity</th>
							<th width="5%"><%=IDBConstants.UOM_LABEL%></th>
							<th width="5%">Status</th>
						</tr>
					</thead>
					<tbody style="font-size: 15px;">

						<%
							if(pono.length()>0){
						     ArrayList al= _loanUtil.loadLoanOrderDetailstoPickorRecv(plant,pono,"R");
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
						          String ordno = (String)m.get("ordno");
						          String ordlnno = (String)m.get("ordlnno");
						          String custname= (String)m.get("custname");
						          String item= (String)m.get("item");
						          String loc= (String)m.get("frLoc");
						          String loc1= (String)m.get("toLoc");
						          String batch= (String)m.get("batch");
						          String qtyor= (String)m.get("qtyor");
						          String qtyis= (String)m.get("qtyis");
						          String qtyrc= (String)m.get("qtyrc");
						          String desc= _ItemMstDAO.getItemDesc(plant ,item);
						          //String uom = _ItemMstDAO.getItemUOM(plant,item);
						          String uom = (String) m.get("UOM");
						          String UOMQTY = (String) m.get("UOMQTY");
						          if(desc.length()==0){desc="NOITEMDESC";}
						          chkString  =ordno+","+ordlnno+","+item+","+su.replaceCharacters2Send(desc)+","+qtyor+","+qtyis+","+qtyrc+","+sUserId+","+loc1+","+loc+","+custname+","+uom+","+UOMQTY;
						         if(desc.equalsIgnoreCase("NOITEMDESC")){
						                            desc="";
						                        }
						%>
						<TR bgcolor="<%=bgcolor%>">

							<TD width="8%" align="left"><font color="black"><INPUT
									Type=radio style="border: 0;" name="chkdDoNo"
									value="<%=chkString%>"></font></TD>
							<TD width="10%" align="left"><font color="black"><%=(String)m.get("ordlnno")%></font></TD>
							<TD align="left" width="15%"><%=(String)m.get("item")%></TD>
							<TD align="left" width="20%"><%=(String)desc%></TD>
							<TD align="center" width="15%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
							<TD align="center" width="14%"><%=StrUtils.formatNum((String)m.get("qtyis"))%></TD>
							<TD align="center" width="10%"><%=StrUtils.formatNum((String)m.get("qtyrc"))%></TD>
							<TD align="center" width="5%"><%=uom%></TD>
							<TD align="center" width="5%"><%=(String)m.get("lnstat")%></TD>
						</TR>

						<%
							}} else {
						%>

						<TR>
							<td colspan="9" align="center">Data's Not Found For Loan
								Order Receiving</TD>
						</TR>
						<%
							}
						       }
						%>
					</tbody>
				</TABLE>


				<INPUT type="Hidden" name="ENCRYPT_FLAG" value="1"> <INPUT
					type="Hidden" name="AFLAG" value="<%=AFLAG%>">


				<div class="form-group">
					<div class="col-sm-12" align="center">
					<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
						<!-- <button type="button" class="Submit btn btn-default"
							onClick="window.location.href='inboundTransaction.jsp'">
							<b>Back</b>
						</button>
						&nbsp;&nbsp; -->
						<button type="button" class="Submit btn btn-default"
							onClick="if(onReceive(document.form)) { submitFormValues();}">
							<b>Receive</b>
						</button>
						&nbsp;&nbsp; <INPUT type="hidden" name="TRAVELER" value="">
						<INPUT name="LOGIN_USER" type="hidden" value="<%=sUserId%>"
							size="1" MAXLENGTH=80>
					</div>
				</div>

			</form>
		</div>
	</div>
</div>


<script>

function viewLoanOrders(){
			document.form.action="/track/LoanOrderReceivingServlet?action=View";
			//document.form.action.value ="View";
            document.form.submit();
		}
function loadLoanOrderDetails() {
	var loanOrderNo = document.form.DONO.value;
	var urlStr = "/track/LoanOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : loanOrderNo,
			PLANT : "<%=plant%>",
						ACTION : "LOAD_LOAN_ORDER_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.form.JOB_NUM.value = resultVal.JOBNUM;
							document.form.CUST_NAME.value = resultVal.CUSTNAME;
							document.form.action = "/track/LoanOrderReceivingServlet?action=View";
							document.form.submit();

						} else {
							alert("Not a valid Order Number!");
							document.form.DONO.value = "";
							document.form.JOB_NUM.value = "";
							document.form.CUST_NAME.value = "";
							document.form.DONO.focus();
						}
					}
				});
	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
