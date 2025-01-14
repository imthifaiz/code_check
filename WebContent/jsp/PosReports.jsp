<%@page import="java.util.ArrayList"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%String title = "Reports"; %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.POS_REPORT%>"/>
</jsp:include>
<style>
 .select2drop
 {
 width:487px !important;
 }
 </style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<%
java.util.ArrayList menulist = (java.util.ArrayList)session.getAttribute("DROPDOWN_MENU");
java.util.ArrayList menuListWithSequence = (java.util.ArrayList)session.getAttribute("DROPDOWN_MENU_WITH_SEQUENCE");
StrUtils strUtils = new StrUtils();
String region = strUtils.fString((String) session.getAttribute("REGION"));
ArrayList<String> addlist = new ArrayList<String>();
ArrayList<String> poslist = new ArrayList<String>();
poslist.add("POS Sales Report");
poslist.add("Void Sales Report");
poslist.add("Return Sales Report");
poslist.add("Discount");
poslist.add("FOC");
poslist.add("Expense");
poslist.add("Shift Close");
poslist.add("POS Revenue Report");

%>
 <!-- Main content -->
 <div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 26.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>POS Reports</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 26.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title">POS <%=title%></h1>
			<div class=" pull-right">
			<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>	
			</div>
			</div>
			</div>
    <section class="content">
    <INPUT type="Hidden" name="region" value="<%=region%>">
    <div class="row report-section">
    <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-shopping-bag" aria-hidden="true"></i> --> POS  </h4>
     <%java.util.Hashtable htMenuItems = (java.util.Hashtable)menuListWithSequence.get(71); //	thirty two row
    java.util.Map htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
          					java.util.Iterator iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					boolean chkmenu11 = false,chkmenu12 = false;
          					for (int i = 0; i < poslist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=poslist.get(i);          						
          						while(iterator.hasNext()){
          							chkmenu12 = true;
          							String menuItemTitle = "" + iterator.next();
          							chkmenu11 = false;
          							if(ListmenuItemTitle.equalsIgnoreCase(menuItemTitle)){
          								addlist.add(menuItemTitle);
          								chkmenu11=true;
          							}
          							else
          							{
          								addlist.add(ListmenuItemTitle);
          								chkmenu11=true;
          							}
          						}
          						if(!chkmenu12){
          							addlist.add(ListmenuItemTitle);          							
          						}
          					}
          					ArrayList<String> newaddList = new ArrayList<String>();   
          			        for (String element : addlist) {  
          			            if (!newaddList.contains(element)) { 
          			  
          			            	newaddList.add(element); 
          			            } 
          			        }
//           			      	newaddList.add("Statement Of Cash Flows");
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(!menuItemURL.equalsIgnoreCase("null")){
          						%>
				                  <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						}
          						else{
				               %>
					              <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>  
          		<!-- <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../posreports/salesreports'">Sales Reports</span></div></div>
          		<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../posreports/shiftclose'">Shift Close</span></div></div>
          		<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../posreports/voidsales'">Void Sales</span></div></div>
          		<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../posreports/expenses'">Expenses</span></div></div>
         		<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../posreports/return'">Return</span></div></div>
          		<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../posreports/discount'">Discount</span></div></div>
          		<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../posreports/FOC'">FOC</span></div></div> --> 
    </div>
    </div>
    </section>
    <jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>