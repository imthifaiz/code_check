<%@page import="java.util.ArrayList"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%String title = "Generate Barcode"; %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
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
ArrayList<String> businesslist = new ArrayList<String>();
businesslist.add("Generate Receipt Barcode");
businesslist.add("Generate Location Barcode");
businesslist.add("Generate Product Barcode");
businesslist.add("Generate Manual Barcode");

%>
 <!-- Main content -->
 <div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Generate Barcode</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
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
    <h4>  <!-- <i class="fa fa-shopping-bag" aria-hidden="true"></i> --> Generate Barcode </h4>
    <%java.util.Hashtable htMenuItems = (java.util.Hashtable)menuListWithSequence.get(67); //	Sixty Eight row
    java.util.Map htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
          					java.util.Iterator iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					boolean chkmenu11 = false;boolean chkmenu12 = false;
          					for (int i = 0; i < businesslist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=businesslist.get(i);          						
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
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(menuItemTitle.equalsIgnoreCase("Generate Receipt Barcode"))
          							menuItemTitle="By Goods Receipt";
          						else if(menuItemTitle.equalsIgnoreCase("Generate Location Barcode"))
          							menuItemTitle="By Location";
          						else if(menuItemTitle.equalsIgnoreCase("Generate Product Barcode"))
          							menuItemTitle="By Product";
          						else if(menuItemTitle.equalsIgnoreCase("Generate Manual Barcode"))
          							menuItemTitle="By Manual";
          						if(!menuItemURL.equalsIgnoreCase("null")){
          						%>
				                  <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						} else{
				               %>
					              <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %> 				              
    </div>    
    </div>
    
    </section>
    <jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>