<%@page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Accounting User Access Rights";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<style>
th {
vertical-align: top;
}
</style>
<script type="text/javascript" src="js/general.js"></script>

<script type="text/javascript" src="js/userLevel.js"></script>

<script>
function checkAll(isChk)
{
	 if (document.levelform.Home)
	    {
    if (document.levelform.Home.disabled == false)
    	document.levelform.Home.checked = isChk;
    for (i = 0; i < document.levelform.Home.length; i++)
    {
            if (document.levelform.Home[i].disabled == false)
        	document.levelform.Home[i].checked = isChk;
   
}
	    }
if (document.levelform.UserAdmin)
{
    if (document.levelform.UserAdmin.disabled == false)
    	document.levelform.UserAdmin.checked = isChk;
    for (i = 0; i < document.levelform.UserAdmin.length; i++)
    {
            if (document.levelform.UserAdmin[i].disabled == false)
        	document.levelform.UserAdmin[i].checked = isChk;
    }
}



    if (document.levelform.SystemMaster)
    {
        if (document.levelform.SystemMaster.disabled == false)
        	document.levelform.SystemMaster.checked = isChk;
        for (i = 0; i < document.levelform.SystemMaster.length; i++)
        {
                if (document.levelform.SystemMaster[i].disabled == false)
            	document.levelform.SystemMaster[i].checked = isChk;
        }
    }




    if (document.levelform.SystemAdmin)
    {
        if (document.levelform.SystemAdmin.disabled == false)
        	document.levelform.SystemAdmin.checked = isChk;
        for (i = 0; i < document.levelform.SystemAdmin.length; i++)
        {
                if (document.levelform.SystemAdmin[i].disabled == false)
            	document.levelform.SystemAdmin[i].checked = isChk;
        }
    }

    if (document.levelform.AccountingAdmin)
    {
        if (document.levelform.AccountingAdmin.disabled == false)
        	document.levelform.AccountingAdmin.checked = isChk;
        for (i = 0; i < document.levelform.AccountingAdmin.length; i++)
        {
                if (document.levelform.AccountingAdmin[i].disabled == false)
            	document.levelform.AccountingAdmin[i].checked = isChk;
        }
    }



    if (document.form.OrderAdmin)
    {
        if (document.levelform.OrderAdmin.disabled == false)
        	document.levelform.OrderAdmin.checked = isChk;
        for (i = 0; i < document.levelform.OrderAdmin.length; i++)
        {
                if (document.levelform.OrderAdmin[i].disabled == false)
            	document.levelform.OrderAdmin[i].checked = isChk;
        }
    }




    if (document.levelform.OrderConfiguration)
    {
        if (document.levelform.OrderConfiguration.disabled == false)
        	document.levelform.OrderConfiguration.checked = isChk;
        for (i = 0; i < document.levelform.OrderConfiguration.length; i++)
        {
                if (document.levelform.OrderConfiguration[i].disabled == false)
            	document.levelform.OrderConfiguration[i].checked = isChk;
        }
    }

    if (document.levelform.EmailConfiguration)
    {
        if (document.levelform.EmailConfiguration.disabled == false)
        	document.levelform.EmailConfiguration.checked = isChk;
        for (i = 0; i < document.levelform.EmailConfiguration.length; i++)
        {
                if (document.levelform.EmailConfiguration[i].disabled == false)
            	document.levelform.EmailConfiguration[i].checked = isChk;
        }
    }


    if (document.levelform.OrderManagement)
    {
        if (document.levelform.OrderManagement.disabled == false)
        	document.levelform.OrderManagement.checked = isChk;
        for (i = 0; i < document.levelform.OrderManagement.length; i++)
        {
                if (document.levelform.OrderManagement[i].disabled == false)
            	document.levelform.OrderManagement[i].checked = isChk;
        }
    }




    if (document.levelform.OrderManagement1)
    {
        if (document.levelform.OrderManagement1.disabled == false)
        	document.levelform.OrderManagement1.checked = isChk;
        for (i = 0; i < document.levelform.OrderManagement1.length; i++)
        {
                if (document.levelform.OrderManagement1[i].disabled == false)
            	document.levelform.OrderManagement1[i].checked = isChk;
        }
    }




    if (document.levelform.SalesEstimate)
    {
        if (document.levelform.SalesEstimate.disabled == false)
        	document.levelform.SalesEstimate.checked = isChk;
        for (i = 0; i < document.levelform.SalesEstimate.length; i++)
        {
                if (document.levelform.SalesEstimate[i].disabled == false)
            	document.levelform.SalesEstimate[i].checked = isChk;
        }
    }


   if (document.levelform.InHouse)
    {
        if (document.levelform.InHouse.disabled == false)
        	document.levelform.InHouse.checked = isChk;
        for (i = 0; i < document.levelform.InHouse.length; i++)
        {
                if (document.levelform.InHouse[i].disabled == false)
            	document.levelform.InHouse[i].checked = isChk;
        }
    }


    if (document.levelform.SalesTransaction)
    {
        if (document.levelform.SalesTransaction.disabled == false)
        	document.levelform.SalesTransaction.checked = isChk;
        for (i = 0; i < document.levelform.SalesTransaction.length; i++)
        {
                if (document.levelform.SalesTransaction[i].disabled == false)
            	document.levelform.SalesTransaction[i].checked = isChk;
        }
    }

    if (document.levelform.RentalTransaction)
    {
        if (document.levelform.RentalTransaction.disabled == false)
        	document.levelform.RentalTransaction.checked = isChk;
        for (i = 0; i < document.levelform.RentalTransaction.length; i++)
        {
                if (document.levelform.RentalTransaction[i].disabled == false)
            	document.levelform.RentalTransaction[i].checked = isChk;
        }
    }

    if (document.levelform.Reports)
    {
        if (document.levelform.Reports.disabled == false)
        	document.levelform.Reports.checked = isChk;
        for (i = 0; i < document.levelform.Reports.length; i++)
        {
                if (document.levelform.Reports[i].disabled == false)
            	document.levelform.Reports[i].checked = isChk;
        }
    }



    if (document.levelform.AccountingHome)
    {
        if (document.levelform.AccountingHome.disabled == false)
        	document.levelform.AccountingHome.checked = isChk;
        for (i = 0; i < document.levelform.AccountingHome.length; i++)
        {
                if (document.levelform.AccountingHome[i].disabled == false)
            	document.levelform.AccountingHome[i].checked = isChk;
        }
    }


   if (document.levelform.AccountingMaster)
    {
        if (document.levelform.AccountingMaster.disabled == false)
        	document.levelform.AccountingMaster.checked = isChk;
        for (i = 0; i < document.levelform.AccountingMaster.length; i++)
        {
                if (document.levelform.AccountingMaster[i].disabled == false)
            	document.levelform.AccountingMaster[i].checked = isChk;
        }
    }



    if (document.levelform.AccountingTransactions)
    {
        if (document.levelform.AccountingTransactions.disabled == false)
        	document.levelform.AccountingTransactions.checked = isChk;
        for (i = 0; i < document.levelform.AccountingTransactions.length; i++)
        {
                if (document.levelform.AccountingTransactions[i].disabled == false)
            	document.levelform.AccountingTransactions[i].checked = isChk;
        }
    }



    if (document.levelform.AccountingReports)
    {
        if (document.levelform.AccountingReports.disabled == false)
        	document.levelform.AccountingReports.checked = isChk;
        for (i = 0; i < document.levelform.AccountingReports.length; i++)
        {
                if (document.levelform.AccountingReports[i].disabled == false)
            	document.levelform.AccountingReports[i].checked = isChk;
        }
    }


   if (document.levelform.PdaPurchaseTransaction)
    {
        if (document.levelform.PdaPurchaseTransaction.disabled == false)
        	document.levelform.PdaPurchaseTransaction.checked = isChk;
        for (i = 0; i < document.levelform.PdaPurchaseTransaction.length; i++)
        {
                if (document.levelform.PdaPurchaseTransaction[i].disabled == false)
            	document.levelform.PdaPurchaseTransaction[i].checked = isChk;
        }
    }


  if (document.levelform.PdaSalesTransaction)
    {
        if (document.levelform.PdaSalesTransaction.disabled == false)
        	document.levelform.PdaSalesTransaction.checked = isChk;
        for (i = 0; i < document.levelform.PdaSalesTransaction.length; i++)
        {
                if (document.levelform.PdaSalesTransaction[i].disabled == false)
            	document.levelform.PdaSalesTransaction[i].checked = isChk;
        }
    }


    if (document.levelform.PdaInHouse)
    {
        if (document.levelform.PdaInHouse.disabled == false)
        	document.levelform.PdaInHouse.checked = isChk;
        for (i = 0; i < document.levelform.PdaInHouse.length; i++)
        {
                if (document.levelform.PdaInHouse[i].disabled == false)
            	document.levelform.PdaInHouse[i].checked = isChk;
        }
    }
}

function checkAllHome(isChk)

{
    if (document.levelform.Home)  
    {
        if (document.levelform.Home.disabled == false)
        	document.levelform.Home.checked = isChk;
        for (i = 0; i < document.levelform.Home.length; i++)
        {
            if (document.levelform.Home[i].disabled == false)
            	document.levelform.Home[i].checked = isChk;
        }
    }
}

function checkAllUserAdmin(isChk)
{
    if (document.levelform.UserAdmin)
    {
        if (document.levelform.UserAdmin.disabled == false)
        	document.levelform.UserAdmin.checked = isChk;
        for (i = 0; i < document.levelform.UserAdmin.length; i++)
        {
                if (document.levelform.UserAdmin[i].disabled == false)
            	document.levelform.UserAdmin[i].checked = isChk;
        }
    }
}

function checkAllSystemMaster(isChk)
{
    if (document.levelform.SystemMaster)
    {
        if (document.levelform.SystemMaster.disabled == false)
        	document.levelform.SystemMaster.checked = isChk;
        for (i = 0; i < document.levelform.SystemMaster.length; i++)
        {
                if (document.levelform.SystemMaster[i].disabled == false)
            	document.levelform.SystemMaster[i].checked = isChk;
        }
    }
}

function checkAllSystemAdmin(isChk)
{
    if (document.levelform.SystemAdmin)
    {
        if (document.levelform.SystemAdmin.disabled == false)
        	document.levelform.SystemAdmin.checked = isChk;
        for (i = 0; i < document.levelform.SystemAdmin.length; i++)
        {
                if (document.levelform.SystemAdmin[i].disabled == false)
            	document.levelform.SystemAdmin[i].checked = isChk;
        }
    }
}

function checkAllAccountingAdmin(isChk)
{
    if (document.levelform.AccountingAdmin)
    {
        if (document.levelform.AccountingAdmin.disabled == false)
        	document.levelform.AccountingAdmin.checked = isChk;
        for (i = 0; i < document.levelform.AccountingAdmin.length; i++)
        {
                if (document.levelform.AccountingAdmin[i].disabled == false)
            	document.levelform.AccountingAdmin[i].checked = isChk;
        }
    }
}

function checkAllOrderAdmin(isChk)
{
    if (document.levelform.OrderAdmin)
    {
        if (document.levelform.OrderAdmin.disabled == false)
        	document.levelform.OrderAdmin.checked = isChk;
        for (i = 0; i < document.levelform.OrderAdmin.length; i++)
        {
                if (document.levelform.OrderAdmin[i].disabled == false)
            	document.levelform.OrderAdmin[i].checked = isChk;
        }
    }
}

function checkAllOrderConfiguration(isChk)
{
    if (document.levelform.OrderConfiguration)
    {
        if (document.levelform.OrderConfiguration.disabled == false)
        	document.levelform.OrderConfiguration.checked = isChk;
        for (i = 0; i < document.levelform.OrderConfiguration.length; i++)
        {
                if (document.levelform.OrderConfiguration[i].disabled == false)
            	document.levelform.OrderConfiguration[i].checked = isChk;
        }
    }
}

function checkAllEmailConfiguration(isChk)
{
    if (document.levelform.EmailConfiguration)
    {
        if (document.levelform.EmailConfiguration.disabled == false)
        	document.levelform.EmailConfiguration.checked = isChk;
        for (i = 0; i < document.levelform.EmailConfiguration.length; i++)
        {
                if (document.levelform.EmailConfiguration[i].disabled == false)
            	document.levelform.EmailConfiguration[i].checked = isChk;
        }
    }
}

function checkAllOrderManagement(isChk)
{
    if (document.levelform.OrderManagement)
    {
        if (document.levelform.OrderManagement.disabled == false)
        	document.levelform.OrderManagement.checked = isChk;
        for (i = 0; i < document.levelform.OrderManagement.length; i++)
        {
                if (document.levelform.OrderManagement[i].disabled == false)
            	document.levelform.OrderManagement[i].checked = isChk;
        }
    }
}

function checkAllOrderManagement1(isChk)
{
    if (document.levelform.OrderManagement1)
    {
        if (document.levelform.OrderManagement1.disabled == false)
        	document.levelform.OrderManagement1.checked = isChk;
        for (i = 0; i < document.levelform.OrderManagement1.length; i++)
        {
                if (document.levelform.OrderManagement1[i].disabled == false)
            	document.levelform.OrderManagement1[i].checked = isChk;
        }
    }
}

function checkAllSalesEstimate(isChk)
{
    if (document.levelform.SalesEstimate)
    {
        if (document.levelform.SalesEstimate.disabled == false)
        	document.levelform.SalesEstimate.checked = isChk;
        for (i = 0; i < document.levelform.SalesEstimate.length; i++)
        {
                if (document.levelform.SalesEstimate[i].disabled == false)
            	document.levelform.SalesEstimate[i].checked = isChk;
        }
    }
}

function checkAllInHouse(isChk)
{
    if (document.levelform.InHouse)
    {
        if (document.levelform.InHouse.disabled == false)
        	document.levelform.InHouse.checked = isChk;
        for (i = 0; i < document.levelform.InHouse.length; i++)
        {
                if (document.levelform.InHouse[i].disabled == false)
            	document.levelform.InHouse[i].checked = isChk;
        }
    }
}

function checkAllSalesTransaction(isChk)
{
    if (document.levelform.SalesTransaction)
    {
        if (document.levelform.SalesTransaction.disabled == false)
        	document.levelform.SalesTransaction.checked = isChk;
        for (i = 0; i < document.levelform.SalesTransaction.length; i++)
        {
                if (document.levelform.SalesTransaction[i].disabled == false)
            	document.levelform.SalesTransaction[i].checked = isChk;
        }
    }
}

function checkAllRentalTransaction(isChk)
{
    if (document.levelform.RentalTransaction)
    {
        if (document.levelform.RentalTransaction.disabled == false)
        	document.levelform.RentalTransaction.checked = isChk;
        for (i = 0; i < document.levelform.RentalTransaction.length; i++)
        {
                if (document.levelform.RentalTransaction[i].disabled == false)
            	document.levelform.RentalTransaction[i].checked = isChk;
        }
    }
}

function checkAllReports(isChk)
{
    if (document.levelform.Reports)
    {
        if (document.levelform.Reports.disabled == false)
        	document.levelform.Reports.checked = isChk;
        for (i = 0; i < document.levelform.Reports.length; i++)
        {
                if (document.levelform.Reports[i].disabled == false)
            	document.levelform.Reports[i].checked = isChk;
        }
    }
}

function checkAllAccountingReports(isChk)
{
    if (document.levelform.AccountingReports)
    {
        if (document.levelform.AccountingReports.disabled == false)
        	document.levelform.AccountingReports.checked = isChk;
        for (i = 0; i < document.levelform.AccountingReports.length; i++)
        {
                if (document.levelform.AccountingReports[i].disabled == false)
            	document.levelform.AccountingReports[i].checked = isChk;
        }
    }
}

function checkAllActivityLogs(isChk)
{
    if (document.levelform.ActivityLogs)
    {
        if (document.levelform.ActivityLogs.disabled == false)
        	document.levelform.ActivityLogs.checked = isChk;
        for (i = 0; i < document.levelform.ActivityLogs.length; i++)
        {
                if (document.levelform.ActivityLogs[i].disabled == false)
            	document.levelform.ActivityLogs[i].checked = isChk;
        }
    }
}

function checkAllAccountingHome(isChk)
{
    if (document.levelform.AccountingHome)
    {
        if (document.levelform.AccountingHome.disabled == false)
        	document.levelform.AccountingHome.checked = isChk;
        for (i = 0; i < document.levelform.AccountingHome.length; i++)
        {
                if (document.levelform.AccountingHome[i].disabled == false)
            	document.levelform.AccountingHome[i].checked = isChk;
        }
    }
}

function checkAllAccountingMaster(isChk)
{
    if (document.levelform.AccountingMaster)
    {
        if (document.levelform.AccountingMaster.disabled == false)
        	document.levelform.AccountingMaster.checked = isChk;
        for (i = 0; i < document.levelform.AccountingMaster.length; i++)
        {
                if (document.levelform.AccountingMaster[i].disabled == false)
            	document.levelform.AccountingMaster[i].checked = isChk;
        }
    }
}

function checkAllAccountingTransactions(isChk)
{
    if (document.levelform.AccountingTransactions)
    {
        if (document.levelform.AccountingTransactions.disabled == false)
        	document.levelform.AccountingTransactions.checked = isChk;
        for (i = 0; i < document.levelform.AccountingTransactions.length; i++)
        {
                if (document.levelform.AccountingTransactions[i].disabled == false)
            	document.levelform.AccountingTransactions[i].checked = isChk;
        }
    }
}

function checkAllAccountingReports(isChk)
{
    if (document.levelform.AccountingReports)
    {
        if (document.levelform.AccountingReports.disabled == false)
        	document.levelform.AccountingReports.checked = isChk;
        for (i = 0; i < document.levelform.AccountingReports.length; i++)
        {
                if (document.levelform.AccountingReports[i].disabled == false)
            	document.levelform.AccountingReports[i].checked = isChk;
        }
    }
}

function checkAllPdaPurchaseTransaction(isChk)
{
    if (document.levelform.PdaPurchaseTransaction)
    {
        if (document.levelform.PdaPurchaseTransaction.disabled == false)
        	document.levelform.PdaPurchaseTransaction.checked = isChk;
        for (i = 0; i < document.levelform.PdaPurchaseTransaction.length; i++)
        {
                if (document.levelform.PdaPurchaseTransaction[i].disabled == false)
            	document.levelform.PdaPurchaseTransaction[i].checked = isChk;
        }
    }
}

function checkAllPdaSalesTransaction(isChk)
{
    if (document.levelform.PdaSalesTransaction)
    {
        if (document.levelform.PdaSalesTransaction.disabled == false)
        	document.levelform.PdaSalesTransaction.checked = isChk;
        for (i = 0; i < document.levelform.PdaSalesTransaction.length; i++)
        {
                if (document.levelform.PdaSalesTransaction[i].disabled == false)
            	document.levelform.PdaSalesTransaction[i].checked = isChk;
        }
    }
}

function checkAllPdaInHouse(isChk)
{
    if (document.levelform.PdaInHouse)
    {
        if (document.levelform.PdaInHouse.disabled == false)
        	document.levelform.PdaInHouse.checked = isChk;
        for (i = 0; i < document.levelform.PdaInHouse.length; i++)
        {
                if (document.levelform.PdaInHouse[i].disabled == false)
            	document.levelform.PdaInHouse[i].checked = isChk;
        }
    }
}
</script>

<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />


<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                 <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='GroupSummaryAccounting.jsp'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
		
 
<%!ArrayList al;

	//Method for selecting the CheckBox as 'Checked'
	public String checkOption(String str)
	
			{
		for (int i = 0; i < al.size(); i++) {
			if (str.equalsIgnoreCase(al.get(i).toString()))
				return "checked";
		}
		return ""; 
	}%>
<%
	ub.setmLogger(mLogger);
	sl.setmLogger(mLogger);
	String caption = "Maintain";
	String delParam = "";
	String disabled = "";
	String disabledInView = "";
	String plant = ((String) session.getAttribute("PLANT")).trim();
	String plant1 = plant;
	if (plant.equalsIgnoreCase("track")) {
		plant = "";
	} else {
		plant = plant + "_";
	}
	Enumeration e = request.getParameterNames();
	while (e.hasMoreElements()) {
		String s = e.nextElement().toString();

		if (s.equalsIgnoreCase("view")) {
			caption = "View";
			disabledInView = "disabled";
		}

		else if (s.equalsIgnoreCase("del")) {
			caption = "Delete";
			delParam = "<input type=\"Hidden\" name=\"del\" value=\"\">"; //  To Indicate the delete function
			disabled = "disabled";
		}
	}
%>
<script>
function onUpdate()
{
	var chk=confirm('Are you sure you would like to save?');
	if(chk){
	document.levelform.action="userLevelAccountingSubmit.jsp?Action=Update";
	document.levelform.submit();}
	 else
	 {
		 return false;
	 }
}
</script>


<form name="form" class="form-horizontal" method="POST" action="maintUserLevelAccounting.jsp">
<br>
<%=delParam%>
<div class="form-group">
<label class="control-label col-sm-4">Company</label>
<div class="col-sm-4">
       <%
			String comp = plant1.toUpperCase();
  		%>
 <input value="<%=comp%>" class="form-control" readonly>
 </div>
 </div>

 <div class="form-group">
 <label class="control-label col-sm-4">Select User Access Rights Group</label>
 <div class="col-sm-4">
 <SELECT class="form-control" style="width: 100%" data-toggle="dropdown" data-placement="right" id="LEVEL_NAME" NAME="LEVEL_NAME" <%=disabledInView%> >
      <OPTION selected value=''> -----Choose User Access Rights Group----- </OPTION>
		<%
		  if (plant1.equalsIgnoreCase("track")) {
			%>
		<%=sl.getUserLevels("1")%>
			<%
			} else {
			%>
		<%=sl.getUserLevelsAccounting("0", plant, "DefaultGroup")%>
			<%
			}
			%>
		</SELECT>
		   </div>
		     
  <div class=form-inline>
   <div class="col-sm-2">
   <button type="submit" class="Submit btn btn-default" value="Go" <%=disabledInView%>>View</button>&nbsp;&nbsp;
   <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
   <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
    </div>
     </div>
     </div>
    
    <br>
    <%
		String level_name = request.getParameter("LEVEL_NAME");
    	if (level_name == null){
    		response.sendRedirect("maintUserLevelAccounting.jsp?LEVEL_NAME");
    		return;
    	}

		/*if (level_name.length() < 1) {
		out
		.write("<br><table width=\"100%\"><tr><td align=\"center\"><b>Please Select a GROUP</b></td></tr></table></FORM>");
		} else {*/
		if (level_name.length() > 1) {
		al = ub.getUserLevelLinksAccounting(level_name, plant);
		String authorise_by = al.get(al.size() - 2).toString();
		if ((authorise_by == null) || (authorise_by.length() <= 1))
		authorise_by = "Not Authorised";
	%>
    
   </form>
   
     
   
   
  <form class="form-horizontal" method="POST" name="levelform" action="userLevelAccountingSubmit.jsp">
   <INPUT type="Hidden" name="LEVEL_NAME" value="<%=level_name%>">
	<P align="center"><font face="Verdana" color="black" size="2"> <b>GROUPS</b></font><font face="Verdana" color="black" size="2"><b> - <%=new String(level_name).toUpperCase()%></b></font></P>
	 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;"  onclick="return checkAll(this.checked);">
                     <strong>   &nbsp; Select/Unselect All </strong> </div>
  </div>
	  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Home"  onclick="return checkAllHome(this.checked);">
                      &nbsp; Select/Unselect Dashboard </div>
  </div>
	
	 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Dashboard</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
                <TR>
                   <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homepurchasetotal" <%=disabled%> <%=checkOption("homepurchasetotal")%>>
                  &nbsp; Total Purchase
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homesaletotal" <%=disabled%> <%=checkOption("homesaletotal")%>>
                  &nbsp; Total Sales
                                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeallactivity" <%=disabled%> <%=checkOption("homeallactivity")%>>
                  &nbsp; See All Activity
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homereceivedproductgrid" <%=disabled%> <%=checkOption("homereceivedproductgrid")%> >
                  &nbsp; Top Received Products 
                  <TR>
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeissuedproductgrid" <%=disabled%> <%=checkOption("homeissuedproductgrid")%> >
                  &nbsp; Top Issued Products 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeexpiringproductgrid" <%=disabled%> <%=checkOption("homeexpiringproductgrid")%> >
                  &nbsp; Expiring Products 
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homestockproductgrid" <%=disabled%> <%=checkOption("homestockproductgrid")%> >
                  &nbsp; Stock Replenishment Products --%>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeaccountingmanaget" <%=disabled%> <%=checkOption("homeaccountingmanaget")%>>
                  &nbsp; Accounting Management</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Home" value="homeaccounting" <%=disabled%> <%=checkOption("homeaccounting")%>>
                  &nbsp; Accounting</label>
                                  
                 <%--  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Home" value="homepurchase" <%=disabled%> <%=checkOption("homepurchase")%>>
                  &nbsp; Purchase</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Home" value="homesales" <%=disabled%> <%=checkOption("homesales")%> >
                  &nbsp; Sales </label>
                  <TR>
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Home" value="homewarehouse" <%=disabled%> <%=checkOption("homewarehouse")%> >
                  &nbsp; Warehouse </label> --%>
                    
                      </TABLE>
         </div>
  </div>
	 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="UserAdmin"  onclick="return checkAllUserAdmin(this.checked);">
                     &nbsp; Select/Unselect User Admin  </div>
  </div>
	<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>User Admin</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px">
                 <TR>
                    <%if(plant1.equalsIgnoreCase("track")){%>
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="companySummry" <%=disabled%> <%=checkOption("companySummry")%>>
                      &nbsp; Summary &#45; Company Details</label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaMaintCpny" <%=disabled%> <%=checkOption("uaMaintCpny")%>>
                       &nbsp; Edit Your Company Profile</label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaCreateCpny" <%=disabled%> <%=checkOption("uaCreateCpny")%>>
                       &nbsp; Create Company Details</label>
                      
                       <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaMaintAcct" <%=disabled%> <%=checkOption("uaMaintAcct")%>>
                        &nbsp;  Summary &#45; User Access Rights Group Details</label>
                        <%}%>
                
                    <TR>
                     <TH WIDTH="20%" ALIGN = "LEFT">
                          <%if(!plant1.equalsIgnoreCase("track")){%>
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaMaintAcct"  <%=disabled%> <%=checkOption("uaMaintAcct")%>>
                        &nbsp;  Summary &#45; User Access Rights Group Details<%}%></label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="maintGroup" <%=disabled%> <%=checkOption("maintGroup")%>>
                      &nbsp;  Edit User Access Rights Group Details</label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" id="USER" value="uaNewAcct" <%=disabled%> <%=checkOption("uaNewAcct")%>>
                       &nbsp; Create User Access Rights Group Details</label>
                       
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaMaintLevel" <%=disabled%> <%=checkOption("uaMaintLevel")%>>
                       &nbsp; Summary &#45; User/Customer Details</label>
                        
                   <TR>
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="maintUser" <%=disabled%> <%=checkOption("maintUser")%>>
                        &nbsp;  Edit User Details</label>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaChngPwd" <%=disabled%> <%=checkOption("uaChngPwd")%>>
                        &nbsp; Edit Password Details</label>       
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="customerMaintCpny" <%=disabled%> <%=checkOption("customerMaintCpny")%>>
                    &nbsp; Edit Your Company Profile</label>
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <%if(plant1.equalsIgnoreCase("track")){%>
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaNewLevel" <%=disabled%> <%=checkOption("uaNewLevel")%>>
                      &nbsp; Create User Details</label>
                      
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaauthcmpy" <%=disabled%> <%=checkOption("uaauthcmpy")%>>&nbsp;Authorise Company Details<%}%></label>      
                    </TABLE>
                 </div>
        </div>   
        
         <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SystemMaster"  onclick="return checkAllSystemMaster(this.checked);">
                     &nbsp; Select/Unselect System Master </div>
  </div>  
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>System Master</strong></div>
<div class="panel-body">
  <TABLE>
             <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="itemsummry"  <%=disabled%> <%=checkOption("itemsummry")%>>
                  &nbsp; Summary &#45; Product Details</label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="alternateItemSummary"  <%=disabled%> <%=checkOption("alternateItemSummary")%>>   
                  &nbsp; Summary &#45; Alternate Products Details</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintItem" <%=disabled%>  <%=disabled%> <%=checkOption("maintItem")%>>
                  &nbsp;  Edit Product Details </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="uaItemMast"<%=disabled%> <%=checkOption("uaItemMast")%>>
                  &nbsp; Create Product Details</label>
                  
                 <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="alternatebrandsummary" <%=disabled%> <%=checkOption("alternatebrandsummary")%>> 
                  &nbsp; Summary/Edit &#45; Alternate Brand Product Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="createalternatbrandproduct"<%=disabled%> <%=checkOption("createalternatbrandproduct")%>>
                  &nbsp; Create Alternate Brand Product Details</label>
                  
                                               
             
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdClsSummary" <%=disabled%> <%=checkOption("prdClsSummary")%>>
                  &nbsp; Summary &#45; Product Class Details</label>
          
             
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintPrdCls" <%=disabled%> <%=checkOption("maintPrdCls")%>>   
                  &nbsp; Edit Product Class Details</label>
                <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="uaPrdCls" <%=disabled%> <%=checkOption("uaPrdCls")%>>
                  &nbsp; Create Product Class Details</label>
               
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdTypeSummary" <%=disabled%> <%=checkOption("prdTypeSummary")%>>
                  &nbsp; Summary &#45; Product Type Details</label> 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintPrdType" <%=disabled%> <%=checkOption("maintPrdType")%>>    
                  &nbsp; Edit Product Type Details</label>
                  
             
             	  <TH WIDTH="20%" ALIGN = "LEFT">
                  	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="uaPrdType" <%=disabled%> <%=checkOption("uaPrdType")%>> 
                  &nbsp; Create Product Type Details</label>
                 <TR>
                  	<TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdBrandSummary"  <%=disabled%> <%=checkOption("prdBrandSummary")%>>
	                  &nbsp; Summary &#45; Product Brand Details</label> 
	           
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintPrdBrand"  <%=disabled%> <%=checkOption("maintPrdBrand")%>> 
	                  &nbsp; Edit Product Brand Details</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="createPrdBrand"  <%=disabled%> <%=checkOption("createPrdBrand")%>> 
	                  &nbsp; Create Product Brand Details</label>
	                  
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="uomsummry" <%=disabled%> <%=checkOption("uomsummry")%>>    
                       &nbsp; Summary &#45; Unit of Measure (UOM) Details</label>
                 	<TR>
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintUom" <%=disabled%> <%=checkOption("maintUoM")%>>   
	                  &nbsp; Edit Unit of Measure (UOM) Details</label>
	                
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="uommst" <%=disabled%> <%=checkOption("uommst")%>>     
	                  &nbsp; Create Unit of Measure (UOM) Details</label>  
	                 
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="locsummry"  <%=disabled%> <%=checkOption("locsummry")%>>     
	                  &nbsp; Summary &#45; Location Details</label>  
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintLoc"   <%=disabled%> <%=checkOption("maintLoc")%>>   
	                  &nbsp; Edit Location Details</label>
                 <TR>
                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="msItemLoc" <%=disabled%> <%=checkOption("msItemLoc")%>>     
	                  &nbsp;  Create Location Details</label>  
	                  
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="loctypesummry"  <%=disabled%> <%=checkOption("loctypesummry")%>>    
	                  &nbsp; Summary &#45; Location Type Details</label>
	                  
	                 
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintLocType" <%=disabled%> <%=checkOption("maintLocType")%>>    
	                  &nbsp; Edit Location Type Details</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="createLocType"  <%=disabled%><%=checkOption("createLocType")%>>     
	                  &nbsp;  Create Location Type Details</label> 
                <TR>   
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="rsnsummry"  <%=disabled%> <%=checkOption("rsnsummry")%>>     
	                  &nbsp; Summary &#45; Reason Code Details</label>
	                  
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintReason"  <%=disabled%> <%=checkOption("maintReason")%>>      
	                  &nbsp; Edit Reason Code Details</label>
	                 
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="rsnmst" <%=disabled%><%=checkOption("rsnmst")%>>    
	                  &nbsp; Create Reason Code Details</label>
	         </TABLE>
                  </div>
        </div>  
          <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SystemAdmin"  onclick="return checkAllSystemAdmin(this.checked);">
                       &nbsp; Select/Unselect System Admin  </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>System Admin</strong></div>
<div class="panel-body">
    <TABLE>
             <TR>                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="vendorsummry" <%=disabled%><%=checkOption("vendorsummry")%>> 
                  &nbsp;  Summary &#45; Supplier Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="supplierdiscountsummary " <%=disabled%><%=checkOption("supplierdiscountsummary")%>> 
                  &nbsp; Summary &#45; Supplier Discount</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="maintVendor" <%=disabled%><%=checkOption("maintVendor")%>> 
                  &nbsp; Edit Supplier Details</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uaVendMst" <%=disabled%><%=checkOption("uaVendMst")%>> 
                  &nbsp; Create Supplier Details </label> 
                 <TR>    
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="supplierTypeSummary" <%=disabled%><%=checkOption("supplierTypeSummary")%>> 
	                  &nbsp; Summary/Edit &#45; Supplier Type Details</label>
	                   
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="createSupplierType" <%=disabled%><%=checkOption("createSupplierType")%>> 
	                  &nbsp; Create Supplier Type Details</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="customersummry" <%=disabled%><%=checkOption("customersummry")%>> 
	                  &nbsp; Summary &#45; Customer Details</label>
	                         
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="maintCustomer" <%=disabled%><%=checkOption("maintCustomer")%>> 
	                  &nbsp;  Edit Customer Details</label>
                 <TR>               
                                           
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uaCustMst" <%=disabled%><%=checkOption("uaCustMst")%>> 
	                  &nbsp; Create Customer Details </label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="customerdiscountsummary" <%=disabled%><%=checkOption("customerdiscountsummary")%>> 
	                   &nbsp; Summary &#45; Customer Discount</label>
	                  
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="ctsummry" <%=disabled%><%=checkOption("ctsummry")%>> 
	                 &nbsp; Summary &#45; Customer Type Details</label>
	                 
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uaCtMst" <%=disabled%><%=checkOption("uaCtMst")%>> 
	                  &nbsp; Edit Customer Type Details </label>
                  <TR>    
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="maintCt" <%=disabled%><%=checkOption("maintCt")%>> 
	                  &nbsp; Create Customer Type Details </label>
	               
	                <%--   <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="ualoanview" <%=disabled%><%=checkOption("ualoanview")%>> 
	                  &nbsp; Summary &#45; Rental Customer Details</label>
	                         
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	        		 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uamaintln" <%=disabled%><%=checkOption("uamaintln")%>> 
	                  &nbsp; Edit Rental Customer Details</label>
	             
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uacratln" <%=disabled%><%=checkOption("uacratln")%>> 
	                  &nbsp;  Create Rental Customer Details</label>
              <TR>         
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uatransview" <%=disabled%><%=checkOption("uatransview")%>> 
	                  &nbsp;  Summary &#45; Consignment Customer Details</label>
	               
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uamaintrans" <%=disabled%><%=checkOption("uamaintrans")%>> 
	                  &nbsp; Edit Consignment Customer Details</label>
	            
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uacrattrans" <%=disabled%><%=checkOption("uacrattrans")%>> 
	                  &nbsp; Create Consignment Customer Details </label> --%>
	               
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="employeesummry" <%=disabled%><%=checkOption("employeesummry")%>> 
	                  &nbsp; Employee</label>   
                   
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="importmasterdata" <%=disabled%><%=checkOption("importmasterdata")%>> 
	                  &nbsp; Import Master Data</label>
	              
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="importdata" <%=disabled%><%=checkOption("importdata")%>> 
	                  &nbsp; Import Inventory And Transaction Data</label>
                                
        	 </TABLE>
        	  </div>
        </div> 
        
            <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderAdmin"  onclick="return checkAllOrderAdmin(this.checked);">
                      &nbsp; Select/Unselect Order Admin </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Order Admin</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px">
        	 <TR>                 
                                        
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="ordtypesumry" <%=disabled%> <%=checkOption("ordtypesumry")%>>
                  &nbsp;  Summary &#45; Order Type Details  </label>
                  
                      
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="maintordtype" <%=disabled%> <%=checkOption("maintordtype")%>>
                  &nbsp; Edit Order Type Details</label>
  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="cratordtype" <%=disabled%> <%=checkOption("cratordtype")%>>
                  &nbsp; Create Order Type Details  </label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createpaymenttype" <%=disabled%> <%=checkOption("createpaymenttype")%>>
                      &nbsp; Summary &#45; Create Payment Type Details</label>
                 <TR>     
                   <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editpaymenttype" <%=disabled%> <%=checkOption("editpaymenttype")%>>
                      &nbsp; Edit Payment Type Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="gstSummry" <%=disabled%> <%=checkOption("gstSummry")%>>
                  &nbsp; Summary &#45; Vat Details  </label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="maintgsttype" <%=disabled%> <%=checkOption("maintgsttype")%>>
                  &nbsp; Edit Vat Details</label>
                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="gsttype" <%=disabled%> <%=checkOption("gsttype")%>>
                  &nbsp; Create Vat Details</label>
                   <TR>         
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="cursummry" <%=disabled%> <%=checkOption("cursummry")%>>
                  &nbsp; Summary &#45; Currency Details</label>
                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="maintCurency" <%=disabled%> <%=checkOption("maintCurency")%>>
                  &nbsp; Edit Currency Details</label>
              
                  <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="cratCurency" <%=disabled%> <%=checkOption("cratCurency")%>>
                  &nbsp; Create Currency Details</label>
                                    
                                
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createhscode" <%=disabled%> <%=checkOption("createhscode")%>>
                  &nbsp; Create HSCODE Details</label>
                   <TR> 
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="edithscode" <%=disabled%> <%=checkOption("edithscode")%>>
                  &nbsp; Edit HSCODE Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createcoo" <%=disabled%> <%=checkOption("createcoo")%>>
                  &nbsp; Create COO Details</label>
                  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editcoo" <%=disabled%> <%=checkOption("editcoo")%>>
                  &nbsp; Edit COO Details</label>
                  
                                
                     <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createorderremarks" <%=disabled%> <%=checkOption("createorderremarks")%>>
                  &nbsp; Create Remarks Details</label>
                   <TR> 
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editorderremarks" <%=disabled%> <%=checkOption("editorderremarks")%>>
                  &nbsp; Edit Remarks Details</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createorderincoterms" <%=disabled%> <%=checkOption("createorderincoterms")%>>
                  &nbsp; Create INCOTERM Details</label>
                  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editorderincoterms" <%=disabled%> <%=checkOption("editorderincoterms")%>>
                  &nbsp; Edit INCTORERM Details</label>
               
                     <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createorderfooter" <%=disabled%> <%=checkOption("createorderfooter")%>>
                  &nbsp; Create Footer Details</label>
                  
                   <TR>  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editorderfooter" <%=disabled%> <%=checkOption("editorderfooter")%>>
                  &nbsp; Edit Footer Details</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="packingMaster" <%=disabled%> <%=checkOption("packingMaster")%>>
                  &nbsp; Create Packing Details </label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="packingMasterSummary" <%=disabled%> <%=checkOption("packingMasterSummary")%>>
                  &nbsp; Edit Packing Details </label>
                 
                 </TABLE>
              </div>
        </div> 
                <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderConfiguration"  onclick="return checkAllOrderConfiguration(this.checked);">
                       &nbsp; Select/Unselect Printout Configuration </div>
  </div>  
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Printout Configuration</strong></div>
<div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px">
	             <TR>     
	              <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editibpt"  <%=disabled%> <%=checkOption("editibpt")%>>
                    &nbsp; Edit Purchase Order Printout </label>           
                 
       				 <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editibwithcostpt" <%=disabled%> <%=checkOption("editibwithcostpt")%>>
                    &nbsp; Edit Purchase Order Printout (with cost)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editInboundOrderMailMsg"  <%=disabled%> <%=checkOption("editInboundOrderMailMsg")%>>
                     &nbsp; Edit Purchase Order Email Message</label>
                     
                     <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editestwithpt" <%=disabled%> <%=checkOption("editestwithpt")%>>
                    &nbsp; Edit Sales Estimate Order Printout (with price)</label>
                    <TR> 
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editEstimateOrderMailMsg"  <%=disabled%> <%=checkOption("editEstimateOrderMailMsg")%>>
                    &nbsp; Edit Sales Estimate Order Email Message</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editobpt" <%=disabled%> <%=checkOption("editobpt")%>>
                    &nbsp; Edit Sales Order Printout</label>
                  
                       <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editobwithpt" <%=disabled%> <%=checkOption("editobwithpt")%>>
                    &nbsp; Edit Sales Order Printout (with price)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editOutboundOrderMailMsg"  <%=disabled%> <%=checkOption("editOutboundOrderMailMsg")%>>
                    &nbsp; Edit Sales Order Email Message</label>
                   
                   <TR>
               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editlopt" <%=disabled%> <%=checkOption("editlopt")%>>
                    &nbsp; Edit Rental Order Printout</label>
               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="edittopt" <%=disabled%> <%=checkOption("edittopt")%>>
                    &nbsp; Edit Consignment Order Printout</label>

                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editTransferOrderMailMsg"  <%=disabled%> <%=checkOption("editTransferOrderMailMsg")%>>
                    &nbsp; Edit Consignment Order Email Message</label>
                   
                   <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editTaxInvoiceMultiLanguage"  <%=disabled%> <%=checkOption("editTaxInvoiceMultiLanguage")%>>
                    &nbsp; Edit Invoice Printout</label>
                    
                    <TR>
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editRcptRecvHdr" <%=disabled%> <%=checkOption("editRcptRecvHdr")%>>
                    &nbsp; Edit Goods Receipt Printout</label>
                                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editRcptIssueHdr" <%=disabled%> <%=checkOption("editRcptIssueHdr")%>>
                    &nbsp; Edit Goods Issue Printout</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editMoveHdr" <%=disabled%> <%=checkOption("editMoveHdr")%>>
                    &nbsp; Edit Stock Move Printout </label>
                      </TABLE>
                </div>
                   </div>
                   
                   <%-- <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="EmailConfiguration"  onclick="return checkAllEmailConfiguration(this.checked);">
                     <strong>   &nbsp; Select/Unselect Approval/Email Configuration</strong> </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Approval/Email Configuration</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px">
        	 <TR>
        	 <TH WIDTH="20%" ALIGN = "LEFT">
                      <INPUT Type=Checkbox  style="border:0;" name="EmailConfiguration" value="editemailconfig" <%=disabled%> <%=checkOption("editemailconfig")%>>
                      &nbsp; Order Management Approval & Email Configuration
        	 </TABLE>
              </div>
        </div> --%>
                    
                              <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderManagement"  onclick="return checkAllOrderManagement(this.checked);">
                        &nbsp; Select/Unselect </div>
  </div> 
                   <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Order Management - Create Masters</strong></div>
    <div class="panel-body"> 
    	<TABLE>
    	   	
                    <TR>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popproduct" <%=disabled%> <%=checkOption("popproduct")%> > 
                  &nbsp; Create Product Details &#45; Purchase,Sales Estimate, Sales, Direct Tax Invoice, Rental And Consignmentr Order</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popsupplier" <%=disabled%> <%=checkOption("popsupplier")%> >
                  &nbsp; Create Supplier Details &#45; Purchase Order</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="suppliertypepopup" <%=disabled%> <%=checkOption("suppliertypepopup")%>>
                  &nbsp; Create Supplier Type - Purchase Order</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popcustomer" <%=disabled%> <%=checkOption("popcustomer")%> >
                  &nbsp; Create Customer Details &#45; Sales Estimate, Sales, Direct Tax Invoice, Rental And Consignment Order</label>
                
                 <TR> 
                 <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="customertypepopup" <%=disabled%> <%=checkOption("customertypepopup")%>>
                  &nbsp;Create Customer Type - Sales Estimate, Sales Order and Direct Tax Invoice.</label>
                 
				  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="ordertypepopup" <%=disabled%> <%=checkOption("ordertypepopup")%>>
                  &nbsp;Create Order Type- Purchase,Sales Estimate, Sales, Direct Tax Invoice,Rental And Consignment Order.</label>
				                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="paymenttypepopup" <%=disabled%> <%=checkOption("paymenttypepopup")%>>
                  &nbsp;Payment Type- Purchase,Sales Estimate, Sales, Direct Tax Invoice,Rental And Consignment Order</label>
                    
             </TABLE>
    </div>
</div> 
                             <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderManagement1"  onclick="return checkAllOrderManagement1(this.checked);">
                        &nbsp; Select/Unselect Purchase </div>
  </div>               
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Purchase</strong></div>
<div class="panel-body"> 
             <TABLE class="table1" style="font-size:14px">
             <TR> 
                                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibsummary" <%=disabled%> <%=checkOption("ibsummary")%>>
                    &nbsp; Summary &#45; Purchase Order Details    </label>                
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibsummarycost" <%=disabled%> <%=checkOption("ibsummarycost")%>>
                    &nbsp; Summary &#45; Purchase Order Details (with cost)</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="maintinb" <%=disabled%> <%=checkOption("maintinb")%>>
                    &nbsp; Edit Purchase Order Details</label>
                               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="msPurOrd" <%=disabled%> <%=checkOption("msPurOrd")%>>
                    &nbsp; Create Purchase Order Details</label>
                    
                    <TR>                     
                               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="orderClose" <%=disabled%> <%=checkOption("orderClose")%>>
                    &nbsp; Close Outstanding Purchase Order</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibrecvbulk" <%=disabled%> <%=checkOption("ibrecvbulk")%>>
                    &nbsp; Purchase Order Receipt </label>
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="inbrectbyrange"   <%=disabled%> <%=checkOption("inbrectbyrange")%>>
                        &nbsp; Purchase Order Receipt (by serial)</label>
                        
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="inbRecbyprd"   <%=disabled%> <%=checkOption("inbRecbyprd")%>>
                        &nbsp; Purchase Order Receipt (by product ID)</label>
                        
                        <TR>                                              
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibrecvmultiple" <%=disabled%> <%=checkOption("ibrecvmultiple")%>>
                        &nbsp; Purchase Order Receipt (multiple) </label>                       
                                             
                       <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="inbReverse"   <%=disabled%> <%=checkOption("inbReverse")%>>
                        &nbsp; Purchase Order Reversal --%>
                                                          
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="mulmiscrecipt"   <%=disabled%> <%=checkOption("mulmiscrecipt")%>>
                        &nbsp; Goods Receipt </label>
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="inbMiscRceiptByRange"   <%=disabled%> <%=checkOption("inbMiscRceiptByRange")%>>
                        &nbsp; Goods Receipt (by serial)</label>
                       
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="editexpiry" <%=disabled%> <%=checkOption("editexpiry")%>>
                        &nbsp; Edit Inventory Expire Date</label>
                        
                        <TR>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
		               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibordersumry" <%=disabled%> <%=checkOption("ibordersumry")%> >
		                &nbsp;  Purchase Order Summary Details</label>
		                </TH>	                         	
	                       
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibordersumrywreccost" <%=disabled%> <%=checkOption("ibordersumrywreccost")%> >
	                    &nbsp;  Purchase Order Summary Details(by cost)</label>
	                    </TH> 
                     
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="recvdibsummry"   <%=disabled%> <%=checkOption("recvdibsummry")%> >
	                    &nbsp;  Purchase Order Summary(by cost)</label>
	                    </TH>
	                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="printpo"   <%=disabled%> <%=checkOption("printpo")%> >
	                    &nbsp;  Purchase Order Details</label>
	                    </TH>
	                    
	                    <TR>
	                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="printpoinv"  <%=disabled%> <%=checkOption("printpoinv")%> >
	                    &nbsp;  Purchase Order Details (with cost)</label>
	                    </TH>
	                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="recvsummry"   <%=disabled%> <%=checkOption("recvsummry")%> >
	                    &nbsp;  Order Receipt Summary</label>
	                    </TH>
	                    
	                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="recvsummrywithcost"  <%=disabled%> <%=checkOption("recvsummrywithcost")%> >
	                    &nbsp;  Order Receipt Summary (with cost)</label>
	                    </TH>
	                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="expensesSummary"   <%=disabled%> <%=checkOption("expensesSummary")%> >
	                    &nbsp;  Expenses</label>
	                    </TH>
	                    
	                    <TR>
	                    
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="grnSummary"   <%=disabled%> <%=checkOption("grnSummary")%> >
	                    &nbsp;  Goods Receipt Summary</label>
	                    </TH>
	                    
	                    
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="billSummary"   <%=disabled%> <%=checkOption("billSummary")%> >
	                    &nbsp;  Bill</label>
	                    </TH>
	                          
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="billPaymentSummary"  <%=disabled%> <%=checkOption("billPaymentSummary")%> >
	                    &nbsp;  Payment</label>
	                    </TH>
	                    
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="pdcpaymentSummary"   <%=disabled%> <%=checkOption("pdcpaymentSummary")%> >
	                    &nbsp; PDC Payment</label>
	                    </TH>
	                    
	                    <TR>
	                    
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="POReturnSummary"   <%=disabled%> <%=checkOption("POReturnSummary")%> >
	                    &nbsp; Purchase Return</label>
	                    </TH>
	                    
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="supplierCreditNote"   <%=disabled%> <%=checkOption("supplierCreditNote")%> >
	                    &nbsp; Purchase Credit Notes</label>
	                    </TH>			
             </TABLE>
             </div>
        </div>
        
                           <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesEstimate"  onclick="return checkAllSalesEstimate(this.checked);">
                       &nbsp; Select/Unselect Sales Estimate </div>
  </div>     
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales Estimate</strong></div>
<div class="panel-body">
    <TABLE class="table1" style="font-size:14px">
            <TR>                       
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estsummarywithoutprice" <%=disabled%> <%=checkOption("estsummarywithoutprice")%>>
                    &nbsp; Summary &#45; Sales Estimate Order Details </label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estsummary" <%=disabled%> <%=checkOption("estsummary")%>>
                    &nbsp; Summary &#45; Sales Estimate Order Details (with price)</label>                    
                                              
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="maintest" <%=disabled%> <%=checkOption("maintest")%>>
                    &nbsp;  Edit Sales Estimate Order Details</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="convertOB" <%=disabled%> <%=checkOption("convertOB")%>>
                    &nbsp;  Edit Sales Estimate &#45; Convert to Sales(button)</label>
                    
                   <TR>
                                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="msestOrd" <%=disabled%> <%=checkOption("msestOrd")%>>
                    &nbsp; Create Sales Estimate Order Details</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estordsmrywithoutprice" <%=disabled%> <%=checkOption("estordsmrywithoutprice")%> >
	                &nbsp;  Sales Estimate Order Summary Details</label>
	                </TH>
	                        
	                <TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estordsmrywissprice" <%=disabled%> <%=checkOption("estordsmrywissprice")%> >
	                &nbsp;  Sales Estimate Order Summary Details(by price)</label>
	                </TH>

					<TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="summaryalternatbrandproduct"  <%=disabled%> <%=checkOption("summaryalternatbrandproduct")%> >
	                &nbsp; Sales Counter</label>
                    </TH>					
                    
                    <TR>                                       
                    	<TH WIDTH="20%" ALIGN = "LEFT">
		                <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="salescounterconvtoest" <%=disabled%> <%=checkOption("salescounterconvtoest")%> >
		                    &nbsp; Sales Counter Convert To Estimate</label>
	                    </TH>
                        
                     	<TH WIDTH="20%" ALIGN = "LEFT">
			              <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="salescounterconvtoinvc" <%=disabled%> <%=checkOption("salescounterconvtoinvc")%> >
			                 &nbsp; Sales Counter Convert To Invoice</label>
	                 	</TH>
	                 </TR>
	                                  
                   </TABLE>
                   </div>
                  </div>
                  
                  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesTransaction"  onclick="return checkAllSalesTransaction(this.checked);">
                       &nbsp; Select/Unselect Sales </div>
  </div>  
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px">
                    <TR>
			   
			        <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="printOB" <%=disabled%> <%=checkOption("printOB")%>>
                    &nbsp; Summary &#45; Sales Order</label>                    
               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="PrintOBInvoice" <%=disabled%> <%=checkOption("PrintOBInvoice")%>>
                    &nbsp; Summary &#45; Sales Order (with price)</label>
                
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obsummary" <%=disabled%> <%=checkOption("obsummary")%>>
                    &nbsp; Summary &#45; Sales Order Details </label>                  
				   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obsummaryprice" <%=disabled%> <%=checkOption("obsummaryprice")%>>
                    &nbsp; Summary &#45; Sales Order Details (with price)</label>
                     
                    <TR>
					
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="maintoub" <%=disabled%> <%=checkOption("maintoub")%>>
                    &nbsp;  Edit Sales Order Details</label>
                                                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="msSalesOrd" <%=disabled%> <%=checkOption("msSalesOrd")%>>
                    &nbsp; Create Sales Order Details</label>
					
					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="orderCloseSales" <%=disabled%> <%=checkOption("orderCloseSales")%>>
                    &nbsp; Close Outstanding Sales Order</label>
					     
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndmulPicking"   <%=disabled%> <%=checkOption("outbndmulPicking")%>>
	                        &nbsp;  Sales Order Pick</label>
	                        
	                         <TR> 
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickingbyRange"   <%=disabled%> <%=checkOption("obpickingbyRange")%>>
	                        &nbsp;  Sales Order Pick (by serial)</label>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndIssuing" <%=disabled%> <%=checkOption("outbndIssuing")%>>
	                        &nbsp; Sales Order Issue</label>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuebulk" <%=disabled%> <%=checkOption("obpickissuebulk")%>>
                            &nbsp;  Sales Order Pick & Issue </label>                     
                         
                           <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuebyprd" <%=disabled%> <%=checkOption("obpickissuebyprd")%>>
                           &nbsp; Sales Order Pick/Issue (by Product)</label>
	                        
	                        <TR>                            
                           
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuemultiple" <%=disabled%> <%=checkOption("obpickissuemultiple")%>>
	                        &nbsp;  Sales Order Pick & Issue (multiple)	 </label>                                              
	                      
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndPkrev"   <%=disabled%> <%=checkOption("outbndPkrev")%>>
		                    &nbsp; Sales Order Pick Return</label>
	                    
	                         <%-- <TH WIDTH="20%" ALIGN = "LEFT">
		                     <INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obissuereversal" <%=disabled%> <%=checkOption("obissuereversal")%>>
		                     &nbsp;  Sales Order Pick & Issue Reversal --%>
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="mulmiscissue"   <%=disabled%> <%=checkOption("mulmiscissue")%>>
		                    &nbsp; Goods Issue</label>
				  		    
		                   <TH WIDTH="20%" ALIGN = "LEFT">
		                   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="ubmiscissueByRange"   <%=disabled%> <%=checkOption("ubmiscissueByRange")%>>
		                   &nbsp; Goods Issue (by serial)</label>
		                     
		                     <TR>
		                   
		                   <TH WIDTH="20%" ALIGN = "LEFT">
                    	   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="creatednpl" <%=disabled%> <%=checkOption("creatednpl")%>>
                    	   &nbsp; Create Sales Packing List/Deliver Note (PL/DN)</label>
		                    
		                     <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="packingSummary" <%=disabled%> <%=checkOption("packingSummary")%> >
                        	&nbsp;  Summary/Edit Packing List and Deliver Note </label>
                        	</TH>
                        	
		                   <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obordersumry" <%=disabled%> <%=checkOption("obordersumry")%> >
                        	&nbsp;  Sales Order Summary Details</label>
                        	</TH>
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction"value="obsalessmry"  <%=disabled%> <%=checkOption("obsalessmry")%> >
                        	&nbsp;   Sales Order Sales Summary</label>
                            </TH>
                        	
                        	<TR>
                            
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="containerSummary" <%=disabled%> <%=checkOption("containerSummary")%> >
                        	&nbsp; Sales Order Summary(by container)</label>
                            </TH>
                                                        
                             <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invWithIssueReturn" <%=disabled%> <%=checkOption("invWithIssueReturn")%> >
                        	&nbsp; Sales Order Summary(by customer) </label>
                           </TH>
                           
                           <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obordersumrywissprice" <%=disabled%> <%=checkOption("obordersumrywissprice")%> >
                        	&nbsp;  Sales Order Summary Details(by price)</label>
                        	 </TH>                  	 
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obordersumrywavgcost" <%=disabled%> <%=checkOption("obordersumrywavgcost")%> >
                        	&nbsp;  Sales Order Summary Details (by average cost)</label>
                       		</TH>
                           
                           <TR>
                       		
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="issuedobsummry" <%=disabled%> <%=checkOption("issuedobsummry")%> >
                        	&nbsp; Sales Order Summary(by price)</label>
                           </TH>                        
                            
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="printdo"  <%=disabled%> <%=checkOption("printdo")%> >
	                        	&nbsp; Sales Order Details</label>
	                        </TH>
	                        	                          
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="printinvoice"  <%=disabled%> <%=checkOption("printinvoice")%> >
	                        	&nbsp; Sales Order Details (with price)</label>
	                       </TH>
	                         
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="issuesummry" <%=disabled%> <%=checkOption("issuesummry")%> >     
	                        	&nbsp;  Order Issue Summary</label>
	                        </TH>
	                        
	                        <TR>	                           
	                         
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="issuesummrywithprice"  <%=disabled%> <%=checkOption("issuesummrywithprice")%> >     
	                        	&nbsp;  Order Issue Summary (with price)</label>
	                        </TH>	                           
	                         
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="ginotoinvoiceSummary"  <%=disabled%> <%=checkOption("ginotoinvoiceSummary")%> >     
	                        	&nbsp;  Goods Issued</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoiceSummary"  <%=disabled%> <%=checkOption("invoiceSummary")%> >     
	                        	&nbsp;  Invoice</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoiceByOpen" <%=disabled%> <%=checkOption("invoiceByOpen")%>>     
	                        	&nbsp;  Invoice By Open</label>
	                        </TH>
	                        
	                        <TR>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoiceByDraft" <%=disabled%> <%=checkOption("invoiceByDraft")%>>     
	                        	&nbsp; Invoice By Draft</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoicePaymentSummary"  <%=disabled%> <%=checkOption("invoicePaymentSummary")%> >     
	                        	&nbsp;  Payment Received</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="pdcpaymentReceiveSummary"  <%=disabled%> <%=checkOption("pdcpaymentReceiveSummary")%> >     
	                        	&nbsp; PDC Payment Received</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="SOReturnSummary"  <%=disabled%> <%=checkOption("SOReturnSummary")%> >     
	                        	&nbsp;  Sales Return</label>
	                        </TH>
	                        
	                        <TR>
	                        		                   
		                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="customerCreditNote"  <%=disabled%> <%=checkOption("customerCreditNote")%> >     
	                        	&nbsp;  Credit Notes</label>
	                        </TH>
	                               
                        </TABLE>
                        </div>
                      </div>
                      
                      <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingAdmin"  onclick="return checkAllAccountingAdmin(this.checked);">
                        &nbsp; Select/Unselect Accounting </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Accounting</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="BankSummary" <%=disabled%> <%=checkOption("BankSummary")%>>
                      &nbsp; Banking</label>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="chartOfAccounts" <%=disabled%> <%=checkOption("chartOfAccounts")%>>
                      &nbsp; Chart of Accounts</label>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="journalsummary" <%=disabled%> <%=checkOption("journalsummary")%>>
                      &nbsp; Journal Entry</label>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="contrasummary" <%=disabled%> <%=checkOption("contrasummary")%>>
                      &nbsp; Contra Entry</label>
                      
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxreturnsettings" <%=disabled%> <%=checkOption("taxreturnsettings")%>>
                      &nbsp; Tax Settings  </label>  
                      
        	<TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxreturnpayments_acc" <%=disabled%> <%=checkOption("taxreturnpayments_acc")%> >
                            &nbsp; Tax Payments </label>
                       </TH>
            
            <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxAdjustmentSummary_acc" <%=disabled%> <%=checkOption("taxAdjustmentSummary_acc")%> >      
	                        &nbsp;  Tax Adjustments</label>
	                        </TH>            
                 
        	 </TABLE>
              </div>
        </div>
        
        
   <%--      
                      
                      <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="RentalTransaction"  onclick="return checkAllRentalTransaction(this.checked);">
                       &nbsp; Select/Unselect Rental / Consignment </div>
  </div>   
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Rental / Consignment</strong></div>
<div class="panel-body">
      <TABLE class="table1" style="font-size:14px">
      
      <TR>
					
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="lnordsummry" <%=disabled%> <%=checkOption("lnordsummry")%>>
                    &nbsp; Summary &#45; Rental Order Details</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="loanOrderSummaryWithPrice" <%=disabled%> <%=checkOption("loanOrderSummaryWithPrice")%>>
                    &nbsp; Summary &#45; Rental Order With Price Details                  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT"> 
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="maintlnord" <%=disabled%> <%=checkOption("maintlnord")%>>
                    &nbsp;  Edit Rental Order Details </label>
                               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="cratlnord" <%=disabled%> <%=checkOption("cratlnord")%>>
                    &nbsp; Create Rental Order Details</label>
					
					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="orderCloseRental" <%=disabled%> <%=checkOption("orderCloseRental")%>>
                    &nbsp; Close Outstanding Rental Orders</label>
                    
                    <TR>					
                                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="trordsummry" <%=disabled%> <%=checkOption("trordsummry")%>>
                    &nbsp; Summary &#45; Consignment Order Details</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">  
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="mainttrord" <%=disabled%> <%=checkOption("mainttrord")%>>
                    &nbsp; Edit Consignment Order Details</label>                    
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="crattrord" <%=disabled%> <%=checkOption("crattrord")%>>
                    &nbsp; Create Consignment Order Details</label>
					
					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="orderCloseTransfer" <%=disabled%> <%=checkOption("orderCloseTransfer")%>>
                    &nbsp; Close Outstanding Consignment Orders</label>
                    
                    <TR>					

					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="lnordReceiving"   <%=disabled%> <%=checkOption("lnordReceiving")%>>
                    &nbsp;Rental Order Receipt</label>                     
		                   
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="lnordPicking"  <%=disabled%> <%=checkOption("lnordPicking")%>>
		                    &nbsp;  Rental Order Pick & Issue</label>                         
		                 
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="bulkPickReceiveTO" <%=disabled%> <%=checkOption("bulkPickReceiveTO")%>>
		                    &nbsp;  Consignment Order Pick & Issue</label>
		               		                  		                  
	                 	    <TH WIDTH="20%" ALIGN = "LEFT">
	                      	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="toreversal" <%=disabled%> <%=checkOption("toreversal")%> >
	                      	&nbsp;  Consignment Order Reversal</label>
		                    
		                    <TR>	                      	
	                      	
							<TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="LoanOrderSummaryByPrice" <%=disabled%> <%=checkOption("LoanOrderSummaryByPrice")%> >
	                          &nbsp;  Rental Order Details Summary With Price
	                          </TH> 
							  
							  <TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="RentalOrderSummary" <%=disabled%> <%=checkOption("RentalOrderSummary")%> >
	                          &nbsp;  Rental Order Details Summary 
	                          </TH>
							
	                      	  <TH WIDTH="20%" ALIGN = "LEFT">
	                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="printloanorder" <%=disabled%> <%=checkOption("printloanorder")%> >
	                          &nbsp;  Rental Order Details</label>
	                          </TH>
							  
							  <TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="printLoanOrderInvoice" <%=disabled%> <%=checkOption("printLoanOrderInvoice")%> >
	                          &nbsp;  Rental Order Details Summary By Price
	                          </TH>	                          
                                                      
                           <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction"value="printto"  <%=disabled%> <%=checkOption("printto")%> >
                        	&nbsp;   Consignment Order Details</label>
                           </TH>
							
	              	 	 </TR>
      
      </TABLE>
                    </div>
                     </div>
                                     
                                       <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="InHouse"  onclick="return checkAllInHouse(this.checked);">
                       &nbsp; Select/Unselect In House </div>
  </div>   
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>In House</strong></div>
<div class="panel-body">
      <TABLE class="table1" style="font-size:14px">
                 
                 <TR>                   
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="mulloctransfer" <%=disabled%> <%=checkOption("mulloctransfer")%>>  
                         &nbsp; Stock Move</label>
                         
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="rptPSVsINV"   <%=disabled%> <%=checkOption("rptPSVsINV")%> >
                        	&nbsp; Stock Take Summary</label> </TH>
                        	
                       		<TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="InHouse" value="stocktakeWithPrice"  <%=disabled%> <%=checkOption("stocktakeWithPrice")%> >
                            &nbsp; Stock Take Summary (with price)</label>
                        	</TH>
                       
                      	    <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="stkreset"   <%=disabled%> <%=checkOption("stkreset")%> >
                        	&nbsp; Stock Take Reset </label></TH>
                        	
                        	<TR> 
                      
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kitdekit" <%=disabled%> <%=checkOption("kitdekit")%>>
                        &nbsp; Kitting De-Kitting</label>
                 
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kitdekitwithbom" <%=disabled%> <%=checkOption("kitdekitwithbom")%>>
                        &nbsp; Kitting De-Kitting(with ref BOM)</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="bulkkitdekit" <%=disabled%> <%=checkOption("bulkkitdekit")%>>
                        &nbsp; Kitting De-Kitting(bulk with ref BOM) </label>                       
                                               	 
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kittingSumm"   <%=disabled%> <%=checkOption("kittingSumm")%> >
                        	&nbsp; Kitting/De-Kitting Summary</label>
                        	</TH>                                                   
                        
                    </TABLE>
                    </div>
                     </div>
                      --%>
             
         <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Reports"  onclick="return checkAllReports(this.checked);">
                       &nbsp; Select/Unselect Inventory Reports </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Inventory Reports</strong></div>
<div class="panel-body">
             <TABLE >
                     <TR>
                    
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewinventoryprodmultiuom"   <%=disabled%> <%=checkOption("viewinventoryprodmultiuom")%>>
	                        	&nbsp; Inventory Summary With Total Quantity</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewinventorybatchmultiuom"   <%=disabled%> <%=checkOption("viewinventorybatchmultiuom")%>>
	                        	&nbsp; Inventory Summary With Batch/Sno</label> 
                         </TH>  
                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewInventoryByProd"  <%=disabled%> <%=checkOption("viewInventoryByProd")%>>
                        	&nbsp; Inventory Summary With Total Quantity (with pcs)</label>
                        </TH>             

                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvnopriceqty"   <%=disabled%> <%=checkOption("msInvnopriceqty")%>>
                        	&nbsp; Inventory Summary With Batch/Sno (with pcs)</label>
                        </TH>
                        <TR>       
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvList"  <%=disabled%> <%=checkOption("msInvList")%>>
                            &nbsp; Inventory Summary (with min/max/zero qty)</label> 
                       </TH>
                    
                         
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithExpireD"   <%=disabled%> <%=checkOption("msInvListwithExpireD")%>>
                        	&nbsp; Inventory Summary (with expiry date)</label>
                        </TH>
                                                                  
                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithcost"  <%=disabled%> <%=checkOption("msInvListwithcost")%>>
	                     &nbsp; Inventory Summary (with average cost)</label>
	                     </TH>     
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="inventoryAgingSummary"  <%=disabled%> <%=checkOption("inventoryAgingSummary")%>>
                            &nbsp; Inventory Aging Summary </label>
                       </TH>
                                         
                        </TABLE>
                   </div>
                          </div>
                              
<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingReports"  onclick="return checkAllAccountingReports(this.checked);">
                      &nbsp; Select/Unselect Accounting Reports </div>
  </div>
  
  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting Reports</strong></div>
    <div class="panel-body">
		<TABLE >
                    <TR>
                    
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="balancesheets" <%=disabled%> <%=checkOption("balancesheets")%> >
	                        	&nbsp; Balance Sheet</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="profitloss" <%=disabled%> <%=checkOption("profitloss")%> >
	                        	&nbsp; Profit and Loss </label>
                         </TH>
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseDetails" <%=disabled%> <%=checkOption("expenseDetails")%> >
	                        	&nbsp; Expense Details</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseByCategory" <%=disabled%> <%=checkOption("expenseByCategory")%> >
	                        	&nbsp; Expense by Category </label>
                         </TH>
                         
                         <TR>  
                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseBySupplier" <%=disabled%> <%=checkOption("expenseBySupplier")%> >
                        	&nbsp; Expense by Supplier</label>
                        </TH>             

                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseByCustomer" <%=disabled%> <%=checkOption("expenseByCustomer")%> >
                        	&nbsp; Expense by Customer</label>
                        </TH>      
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="billsdetails" <%=disabled%> <%=checkOption("billsdetails")%> >
                            &nbsp; Bill Details </label>
                       </TH>
                    
                         
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="paymentsmade" <%=disabled%> <%=checkOption("paymentsmade")%> >
                        	&nbsp; Payment Made</label>
                        </TH>
                                                                  
                        <TR>
                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="vendorbalances" <%=disabled%> <%=checkOption("vendorbalances")%> >
	                     &nbsp; Supplier Balances</label>
	                     </TH>
	                                      

                       	<TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="vendorcreditdetails" <%=disabled%> <%=checkOption("vendorcreditdetails")%> >
	                        	&nbsp; Supplier Credit Notes Details</label>
                         </TH>       
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="billsAgingSummary" <%=disabled%> <%=checkOption("billsAgingSummary")%> >
                            &nbsp; Supplier Aging Summary </label>
                       </TH>
                       
                                           	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="invoicedetails" <%=disabled%> <%=checkOption("invoicedetails")%> >
                        	&nbsp; Invoice Details</label>
                        </TH>	                                      

						<TR>

                       	<TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="paymentsreceived" <%=disabled%> <%=checkOption("paymentsreceived")%> >
	                        	&nbsp; Payment Received</label>
                         </TH>      
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="customerbalances" <%=disabled%> <%=checkOption("customerbalances")%> >
                            &nbsp; Customer Balances </label>
                       </TH>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="customercreditdetails" <%=disabled%> <%=checkOption("customercreditdetails")%> >
                            &nbsp; Customer Credit Notes Details </label>
                       </TH>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="invoiceAgingSummary" <%=disabled%> <%=checkOption("invoiceAgingSummary")%> >      
	                        &nbsp;  Customer Aging Summary</label>
	                        </TH>
	                        
	                        <TR>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="taxreturnsummary" <%=disabled%> <%=checkOption("taxreturnsummary")%> >      
	                        &nbsp;  Tax Return Summary</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                      <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="taxAdjustmentSummary" <%=disabled%> <%=checkOption("taxAdjustmentSummary")%> >      
	                        &nbsp;  Tax Adjustments Summary</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="taxreturnpayments" <%=disabled%> <%=checkOption("taxreturnpayments")%> >
                            &nbsp; Tax Payments Summary</label>
                       		</TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                    <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="generalledger" <%=disabled%> <%=checkOption("generalledger")%> >      
	                        &nbsp;  Detailed General Ledger</label>
	                        </TH>
	                        
	                        <TR>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                    <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="journalreport" <%=disabled%> <%=checkOption("journalreport")%> >      
	                        &nbsp;  Journal</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                    <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="trialbalance" <%=disabled%> <%=checkOption("trialbalance")%> >      
	                        &nbsp;  Trail Balance</label>
	                        </TH>
                       
                                           
                   </TABLE>
    </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="ActivityLogs"  onclick="return checkAllActivityLogs(this.checked);">
                    &nbsp; Select/Unselect Activity Logs </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Activity Logs</strong></div>
    <div class="panel-body">
		<TABLE >
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ActivityLogs" value="msPOList"  <%=disabled%> <%=checkOption("msPOList")%>>
	                        	&nbsp; Activity Logs</label>
                         </TH>
                    
                    </TABLE>
    </div>
</div>
                                
                              <%-- <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingHome"  onclick="return checkAllAccountingHome(this.checked);">
                     <strong>   &nbsp; Select/Unselect Accounting - Home Page</strong> </div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Home Page</strong></div>
    <div class="panel-body">
		<table>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="profitlosswidget" <%=disabled%> <%=checkOption("profitlosswidget")%>>&nbsp;Profit Loss Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="expenseswidget" <%=disabled%> <%=checkOption("expenseswidget")%>>&nbsp;Expense Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="incomewidget" <%=disabled%> <%=checkOption("incomewidget")%>>&nbsp;Income Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="bankaccountswidget" <%=disabled%> <%=checkOption("bankaccountswidget")%>>&nbsp;Bank Accounts Widget</th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="purchasewidget" <%=disabled%> <%=checkOption("purchasewidget")%>>&nbsp;Purchase Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="saleswidget" <%=disabled%> <%=checkOption("saleswidget")%>>&nbsp;Sales Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="movementhistorywidget" <%=disabled%> <%=checkOption("movementhistorywidget")%>>&nbsp;Movement History Widget</th>
				<th></th>
			</tr>
		</table>
</div>
                          </div>
                           <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingMaster"  onclick="return checkAllAccountingMaster(this.checked);">
                     <strong>   &nbsp; Select/Unselect Accounting - Masters</strong> </div>
  </div>                 
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Masters</strong></div>
    <div class="panel-body">
    	<table>		
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createbankbranch" <%=disabled%> <%=checkOption("createbankbranch")%>>&nbsp;Create bank Branch</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listbankbranches" <%=disabled%> <%=checkOption("listbankbranches")%>>&nbsp;List Bank Branches</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importbankbranchss" <%=disabled%> <%=checkOption("importbankbranchss")%>>&nbsp;Import Bank Branches</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createcustomer" <%=disabled%> <%=checkOption("createcustomer")%>>&nbsp;Create Customer</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listcustomers" <%=disabled%> <%=checkOption("listcustomers")%>>&nbsp;List Customers</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importcustomers" <%=disabled%> <%=checkOption("importcustomers")%>>&nbsp;Import Customers</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createsupplier" <%=disabled%> <%=checkOption("createsupplier")%>>&nbsp;Create Supplier</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listsuppliers" <%=disabled%> <%=checkOption("listsuppliers")%>>&nbsp;List Suppliers</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importsuppliers" <%=disabled%> <%=checkOption("importsuppliers")%>>&nbsp;Import Suppliers</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createaccount" <%=disabled%> <%=checkOption("createaccount")%>>&nbsp;Create Account</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listaccounts" <%=disabled%> <%=checkOption("listaccounts")%>>&nbsp;List Accounts</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importaccounts" <%=disabled%> <%=checkOption("importaccounts")%>>&nbsp;Import Accounts</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createdaybook" <%=disabled%> <%=checkOption("createdaybook")%>>&nbsp;Create Daybook</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listdaybooks" <%=disabled%> <%=checkOption("listdaybooks")%>>&nbsp;List Daybooks</th>
				<th></th><th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createcurrency" <%=disabled%> <%=checkOption("createcurrency")%>>&nbsp;Create Currency</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listcurrencies" <%=disabled%> <%=checkOption("listcurrencies")%>>&nbsp;List Currencies</th>
				<th></th><th></th>
			</tr>
	</table>
</div>
                          </div>
                       <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingTransactions"  onclick="return checkAllAccountingTransactions(this.checked);">
                     <strong>   &nbsp; Select/Unselect Accounting - Transactions</strong> </div>
  </div>     
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Transactions</strong></div>
    <div class="panel-body">
    	<table>					
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingTransactions" value="newfinancialtransaction" <%=disabled%> <%=checkOption("newfinancialtransaction")%>>&nbsp;New Financial Transaction</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingTransactions" value="listfinancialtransactions" <%=disabled%> <%=checkOption("listfinancialtransactions")%>>&nbsp;List Financial Transactions</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingTransactions" value="reconcilesalespurchase" <%=disabled%> <%=checkOption("reconcilesalespurchase")%>>&nbsp;Reconcile Sales &amp; Purchase</th>
				<th></th>
			</tr>
	</table>
</div>
                          </div>
                          <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingReports"  onclick="return checkAllAccountingReports(this.checked);">
                     <strong>   &nbsp; Select/Unselect Accounting - Reports</strong> </div>
  </div>     
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Reports</strong></div>
    <div class="panel-body">
    	<table>					
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="movementhistoryreport" <%=disabled%> <%=checkOption("movementhistoryreport")%>>&nbsp;Movement History Report</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="financialtransactionsreport" <%=disabled%> <%=checkOption("financialtransactionsreport")%>>&nbsp;Financial Transactions Report</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="profitlossstatement" <%=disabled%> <%=checkOption("profitlossstatement")%>>&nbsp;Profit Loss Statement</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="trailbalance" <%=disabled%> <%=checkOption("trailbalance")%>>&nbsp;Trail Balance</th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="balancesheet" <%=disabled%> <%=checkOption("balancesheet")%>>&nbsp;Balance sheet</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="customerwiseageing" <%=disabled%> <%=checkOption("customerwiseageing")%>>&nbsp;Customer-wise Ageing</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="supplierwiseageing" <%=disabled%> <%=checkOption("supplierwiseageing")%>>&nbsp;Supplier-wise Ageing</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="cashflowprojection">&nbsp;Cash Flow Projection</th>
			</tr>
        </table>
    </div>
</div> --%>                           
<!-- <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - MAIN MENU (STORE N TRACK)</strong></div>
<div class="panel-body">
                    <TABLE class="table1" style="font-size:14px">
                            <TR>
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaInBound"  <%=disabled%> <%=checkOption("pdaInBound")%>>
                                &nbsp; INBOUND
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaOutBound"  <%=disabled%> <%=checkOption("pdaOutBound")%>>
                                &nbsp; OUTBOUND
                             <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaWarehouse"  <%=disabled%> <%=checkOption("pdaWarehouse")%>>
                                &nbsp; IN PREMISES
                             
                            </TABLE>
                            </div>
 </div> -->
 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PdaPurchaseTransaction"  onclick="return checkAllPdaPurchaseTransaction(this.checked);">
                     <strong>   &nbsp; Select/Unselect PDA - Purchase Transaction</strong> </div>
  </div>     
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - Purchase Transaction</strong></div>
<div class="panel-body">
    	   <TABLE class="table1" style="font-size:14px">
                     <TR>
                         <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaPurchaseTransaction" value="pdainboundReceipt"  <%=disabled%> <%=checkOption("pdainboundReceipt")%>>
                          &nbsp; PURCHASE RECEIPT</label>
                          
                         <!--   <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaTransferReceipt"  <%=disabled%> <%=checkOption("pdaTransferReceipt")%>>
                          &nbsp; Transfer Receipt
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaLoanReceipt"  <%=disabled%> <%=checkOption("pdaLoanReceipt")%>>
                          &nbsp; Bulk Loan Receipt-->
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaPurchaseTransaction" value="pdaMiscReceipt"  <%=disabled%> <%=checkOption("pdaMiscReceipt")%>>
                           &nbsp;GOODS RECEIPT</label>
                           
                          </TABLE>
                           </div>
                                </div>
<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PdaSalesTransaction"  onclick="return checkAllPdaSalesTransaction(this.checked);">
                    &nbsp; Select/Unselect PDA - Sales Transaction</div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - Sales Transaction</strong></div>
<div class="panel-body">
    	<TABLE class="table1" style="font-size:14px">
    	
    	<TR>
                     
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaOutBoundIssue"  <%=disabled%> <%=checkOption("pdaOutBoundIssue")%>>
                        &nbsp; SALES PICK & ISSUE</label>
                        
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaMiscIssue"  <%=disabled%> <%=checkOption("pdaMiscIssue")%>>
                        &nbsp; GOODS ISSUE</label>
                 </TABLE>
                 </div>
                       </div>
                       <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PdaInHouse"  onclick="return checkAllPdaInHouse(this.checked);">
                      &nbsp; Select/Unselect PDA - In House </div>
  </div> 
 <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - In House</strong></div>
<div class="panel-body">
    	<TABLE class="table1" style="font-size:14px">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaInHouse" value="pdaInventoryQry"  <%=disabled%> <%=checkOption("pdaInventoryQry")%>>
                        &nbsp; INV QUERY</label>
                        
                        <!-- <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaPutAway"  <%=disabled%> <%=checkOption("pdaPutAway")%>>
                        &nbsp; Put Away-->
                       
                      <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaInHouse" value="pdaLocTransfer"  <%=disabled%> <%=checkOption("pdaLocTransfer")%>>
                        &nbsp; STOCK MOVE</label>
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaInHouse" value="pdaStockTake"  <%=disabled%> <%=checkOption("pdaStockTake")%>>
                        &nbsp; STOCK TAKE</label>
                        
                       
                     <!--   <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaTransferObOrder"  <%=disabled%> <%=checkOption("pdaTransferObOrder")%>>
                        &nbsp;  Outbound Transfer
                        
                    <TR> 
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaKitting"  <%=disabled%> <%=checkOption("pdaKitting")%>>
                        &nbsp; Kitting
                      </TH>
                      <TH WIDTH="20%" ALIGN = "LEFT">
                  <!--   <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaDeKitting"  <%=disabled%> <%=checkOption("pdaDeKitting")%>>
                        &nbsp; DeKitting -->
                      </TH> 
                         </TABLE>
                          </div>
                         </div>
<!--  <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA -  MAIN MENU (SELL N TRACK)</strong></div>
<div class="panel-body">
    	<TABLE class="table1" style="font-size:14px">
    	                <TR>
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSales" <%=disabled%> <%=checkOption("pdaMobileSales")%>>
                                &nbsp; Mobile Sales
                                
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaEnquiry"  <%=disabled%> <%=checkOption("pdaEnquiry")%>>
                                &nbsp; Enquiry
                                
                             <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaPos"  <%=disabled%> <%=checkOption("pdaPos")%>>
                                &nbsp; POS
                             
                            </TABLE>
                            </div>
</div>-->
<!--  <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - MOBILE SALES</strong></div>
<div class="panel-body">  
                <TABLE class="table1" style="font-size:14px">
                          <TR>
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSalesCreate" <%=disabled%> <%=checkOption("pdaMobileSalesCreate")%>>
                          &nbsp; Create
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSalesEdit"  <%=disabled%> <%=checkOption("pdaMobileSalesEdit")%>>
                          &nbsp; Edit
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSalesSummary"  <%=disabled%> <%=checkOption("pdaMobileSalesSummary")%>>
                          &nbsp; Summary
                          
                         </TABLE>
                         </div>
 </div>-->
<!-- <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - ENQUIRY</strong></div>
<div class="panel-body">  
     <TABLE class="table1" style="font-size:14px">
                    <TR>
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaEnquiryInvQryByProduct" <%=disabled%> <%=checkOption("pdaEnquiryInvQryByProduct")%>>
                        &nbsp; Query By Product
                        
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaEnquiryInvQryByLoc" <%=disabled%> <%=checkOption("pdaEnquiryInvQryByLoc")%>>
                        &nbsp; Query By Loc
                        
                        </TABLE>
                         </div>
</div> -->
                       
    
    <INPUT Type=Hidden name="URL" value="logout"> 
	<div class="form-group">
    <label class="control-label col-sm-4">Remarks</label>
      <div class="col-sm-4">
      <input name="REMARKS" <%=disabled%> value="<%=al.get(al.size() - 3).toString()%>"
	   size="50" MAXLENGTH=60 class="form-control">
	   </div>
		  </div>
		  
	<div class="form-group">
    <label class="control-label col-sm-4">Updated By</label>
      <div class="col-sm-4">
      <input  size="50" MAXLENGTH=60 disabled value="<%=authorise_by%>" class="form-control">
	   </div>
		  </div>
		  
    <div class="form-group">
    <label class="control-label col-sm-4">Updated On</label>
      <div class="col-sm-4">
      <input  size="50" MAXLENGTH=60 disabled value="<%=al.get(al.size() - 1).toString()%>" class="form-control">
	   </div>
		  </div>
           
           
     <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
     <!-- <button type="button" class="Submit btn btn-default" name="C" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
     <!-- <button type="button" class="Submit btn btn-default"  name="C" Value="Back" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
	 <button type="Submit" class="btn btn-success" name="upd" value="Save" onClick="return onUpdate();">Save</button>&nbsp;&nbsp;
	 <button type="Submit" class="Submit btn btn-default"  name="Action" Value="Delete" onClick="return confirm('Are you sure you would like to delete ?');"><b>Delete</b></button>&nbsp;&nbsp;
     
    </div>
    </div>
    
    
    <!--
    <%
    out
    .write("<INPUT Type=\"Submit\" name=\"Action\" Value=\"Delete\" onClick=\"return confirm('Are you sure you would like to delete ?');\">&nbsp;");
    %>  -->
     
    
 <%
	} //  Closing else
%>
</form>
</div>
</div>
</div>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
