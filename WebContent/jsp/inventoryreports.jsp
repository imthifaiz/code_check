<%@page import="java.util.ArrayList"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.dao.ParentChildCmpDetDAO"%>
<%String title = "Reports"; %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
    <jsp:param name="submenu" value="<%=IConstants.INVENTORY%>"/>
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
ArrayList<String> quantitylist = new ArrayList<String>();
quantitylist.add("Inventory Summary With Total Quantity (group by location)");
quantitylist.add("Inventory Summary With Total Quantity (group by product)");
quantitylist.add("Inventory Summary With Total Quantity (with pcs)");
quantitylist.add("Inventory Summary (with min quantity)");
quantitylist.add("Inventory Summary (with expiry date)");
quantitylist.add("Inventory Valuation Summary With Total Quantity");

ArrayList<String> qtybatchlist = new ArrayList<String>();
qtybatchlist.add("Inventory Summary With Batch/Sno");
qtybatchlist.add("Inventory Summary With Batch/Sno (with pcs)");


ArrayList<String> stocklist = new ArrayList<String>();
stocklist.add("Inventory Summary (with average cost)");
stocklist.add("Inventory Summary Opening/Closing Stock");
stocklist.add("Inventory Summary Opening/Closing Stock (with average cost)");


ArrayList<String> invgoodslist = new ArrayList<String>();
invgoodslist.add("Inventory Summary (with goods receipt / goods issue)");


ArrayList<String> costandsaleslist = new ArrayList<String>();
costandsaleslist.add("Inventory Summary (With Purchase Cost / Sales Price)");


ArrayList<String> movementlist = new ArrayList<String>();
movementlist.add("Inventory Movement Report");


ArrayList<String> invrevenuelist = new ArrayList<String>();
invrevenuelist.add("Revenue Risk Due to Inventory Shortage Summary");

ArrayList<String> avlblelist = new ArrayList<String>();
avlblelist.add("Inventory Summary Available Quantity");


ArrayList<String> aging = new ArrayList<String>();
aging.add("Inventory Aging Summary");

ArrayList<String> forecast = new ArrayList<String>();
forecast.add("Sales Forecasting");
String plant=(String)session.getAttribute("PLANT");
String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENTBYCHILD(plant);
if(PARENT_PLANT==null)
	PARENT_PLANT="";
%>
 <!-- Main content -->
 <div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 26.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Inventory Reports</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 26.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title">Inventory <%=title%></h1>
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
    <h4>  <!-- <i class="fa fa-shopping-bag" aria-hidden="true"></i> --> With Quantity </h4>
    <%java.util.Hashtable htMenuItems = (java.util.Hashtable)menuListWithSequence.get(14); //	Fifteenth row
    java.util.Map htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
          					java.util.Iterator iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					boolean chkmenu11 = false,chkmenu12 = false;
          					for (int i = 0; i < quantitylist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=quantitylist.get(i);          						
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
							%>
			                  <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../inventory/inventorysummary'">Inventory Summary</span></div></div>
			               	<%
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(!menuItemURL.equalsIgnoreCase("null")){
          							if(!PARENT_PLANT.equalsIgnoreCase("")){
          							if(menuItemURL.equalsIgnoreCase("inventory/minqty")) {
          								//menuItemURL="inventory/minwithprtqty";
          								%>
      				                  <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../inventory/minwithprtqty'">Inventory Summary (with min qty by outlet and warehouse)</span></div></div>
      				               		<%	
          							}
          							}
          						%>
				                  <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						}
          						else{
				               %>
					              <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %> 

    </div>
    <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> With Quantity And Batch/Sno  </h4>
    <%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < qtybatchlist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=qtybatchlist.get(i);          						
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
          					newaddList = new ArrayList<String>();   
          			        for (String element : addlist) {  
          			            if (!newaddList.contains(element)) { 
          			  
          			            	newaddList.add(element); 
          			            } 
          			        } 
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(!menuItemURL.equalsIgnoreCase("null")){
          						%>
				                  <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						} else{
				               %>
					              <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>
    </div>
    <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> Average, Opening And Closing Stock </h4>
    <%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(3);//	Third column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < stocklist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=stocklist.get(i);          						
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
          					newaddList = new ArrayList<String>();   
          			        for (String element : addlist) {  
          			            if (!newaddList.contains(element)) { 
          			  
          			            	newaddList.add(element); 
          			            } 
          			        } 
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(!menuItemURL.equalsIgnoreCase("null")){
          						%>
				                  <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						} else{
				               %>
					              <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>
    </div>
    </div>
    <div class="row report-section">
    <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-list-alt" aria-hidden="true"></i> --> Goods Receipt And Goods Issue  </h4>
    <% htMenuItems = (java.util.Hashtable)menuListWithSequence.get(14); //	Fifteenth row
     htMenuItemDetail = (java.util.Map)htMenuItems.get(4);//	Four column 
     if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
 	iterator = htMenuItemDetail.keySet().iterator();
		addlist = new ArrayList<String>();
		chkmenu11 = false; chkmenu12 = false;
		for (int i = 0; i < invgoodslist.size(); i++) {
			chkmenu12=false;
			String ListmenuItemTitle=invgoodslist.get(i);          						
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
		newaddList = new ArrayList<String>();   
       for (String element : addlist) {  
           if (!newaddList.contains(element)) { 
 
           	newaddList.add(element); 
           } 
       } 
		for (int i = 0; i < newaddList.size(); i++) {
			String menuItemTitle=newaddList.get(i);
			String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
			if(!menuItemURL.equalsIgnoreCase("null")){
			%>
           <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
        <%
			} else{
        %>
           <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
         <%
			}
		} %>     					 
    </div>
  
    <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> Purchase Cost And Sales Price  </h4>
<!--     <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/bankreconcilationsummary'">Bank Reconciliation</span></div></div> -->
    <%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(5);//	five column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < costandsaleslist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=costandsaleslist.get(i);          						
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
          					newaddList = new ArrayList<String>();   
          			        for (String element : addlist) {  
          			            if (!newaddList.contains(element)) { 
          			  
          			            	newaddList.add(element); 
          			            } 
          			        }
          			       
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(!menuItemURL.equalsIgnoreCase("null")){
          						%>
				                  <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						}
          						
  
          						
          						else{
				               %>
					              <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>
<!--           					<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/parentjournalreport'">Consolidated Journal</span></div></div> -->
<!--           					<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/parenttrialbalance'">Consolidated Trail Balance</span></div></div> -->
    </div>
    
    
    
        <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> -->Inventory Movement  </h4>
<!--     <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/bankreconcilationsummary'">Bank Reconciliation</span></div></div> -->
    <%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(6);//	Six column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < movementlist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=movementlist.get(i);          						
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
          					newaddList = new ArrayList<String>();   
          			        for (String element : addlist) {  
          			            if (!newaddList.contains(element)) { 
          			  
          			            	newaddList.add(element); 
          			            } 
          			        }
          			       
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(!menuItemURL.equalsIgnoreCase("null")){
          						%>
				                  <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						}
          						
  
          						
          						else{
				               %>
					              <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>
    </div>
    </div>
    
        <div class="row report-section">
        
        <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> Inventory Revenue  </h4>
<!--     <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/bankreconcilationsummary'">Bank Reconciliation</span></div></div> -->
    <%
    						htMenuItems = (java.util.Hashtable)menuListWithSequence.get(14);// Fifteenth row
    						htMenuItemDetail = (java.util.Map)htMenuItems.get(9);//	Nine column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < invrevenuelist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=invrevenuelist.get(i);          						
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
          					newaddList = new ArrayList<String>();   
          			        for (String element : addlist) {  
          			            if (!newaddList.contains(element)) { 
          			  
          			            	newaddList.add(element); 
          			            } 
          			        }
          			       
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(!menuItemURL.equalsIgnoreCase("null")){
          						%>
				                  <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						}
          						
  
          						
          						else{
				               %>
					              <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>
    </div>
        
        <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> Available  </h4>
<!--     <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/bankreconcilationsummary'">Bank Reconciliation</span></div></div> -->
    <%
   						 
    					htMenuItemDetail = (java.util.Map)htMenuItems.get(7);//	seven column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < avlblelist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=avlblelist.get(i);          						
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
          					newaddList = new ArrayList<String>();   
          			        for (String element : addlist) {  
          			            if (!newaddList.contains(element)) { 
          			  
          			            	newaddList.add(element); 
          			            } 
          			        }
          			       
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(!menuItemURL.equalsIgnoreCase("null")){
          						%>
				                  <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						}
          						
  
          						
          						else{
				               %>
					              <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>
    </div>
        <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> Aging  </h4>
<!--     <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/bankreconcilationsummary'">Bank Reconciliation</span></div></div> -->
    <%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(8);//	Eight column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < aging.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=aging.get(i);          						
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
          					newaddList = new ArrayList<String>();   
          			        for (String element : addlist) {  
          			            if (!newaddList.contains(element)) { 
          			  
          			            	newaddList.add(element); 
          			            } 
          			        }
          			       
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(!menuItemURL.equalsIgnoreCase("null")){
          						%>
				                  <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						}
          						
  
          						
          						else{
				               %>
					              <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>
    </div>
    
<!--     <div class="col-md-4"> -->
    
<!--      <h4>  <i class="fa fa-indent" aria-hidden="true"></i> Project  </h4> -->
<!--      <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../businessoverview/projectprofitloss'">Project Profit and Loss</span></div></div> -->
<!--    	  <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../projecttrial/trialbalance'">Project Trial Balance</span></div></div>  -->
<!--    	 <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../projecttrial/parenttrialbalance'">Consolidated Project Trial Balance</span></div></div>  -->
<!--     </div> -->
    
    
    
    </div>
        <div class="row report-section">
        
        <div class="col-md-4">
    <h4>  Forecasting  </h4>
    <%
    						htMenuItems = (java.util.Hashtable)menuListWithSequence.get(14);// Fifteenth row
    						htMenuItemDetail = (java.util.Map)htMenuItems.get(10);//	Nine column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < forecast.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=forecast.get(i);          						
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
          					newaddList = new ArrayList<String>();   
          			        for (String element : addlist) {  
          			            if (!newaddList.contains(element)) { 
          			  
          			            	newaddList.add(element); 
          			            } 
          			        }
          			       
          					for (int i = 0; i < newaddList.size(); i++) {
          						String menuItemTitle=newaddList.get(i);
          						String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
          						if(!menuItemURL.equalsIgnoreCase("null")){
          						%>
				                  <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						}
          						
  
          						
          						else{
				               %>
					              <div class="col-md-12" style="height: 38px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>
    </div>
        
        <div class="col-md-4">
    </div>
        <div class="col-md-4">

    </div>
    
    </div>
    
    </section>
    <jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>