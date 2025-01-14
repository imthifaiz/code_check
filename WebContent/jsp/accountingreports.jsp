<%@page import="java.util.ArrayList"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%String title = "Reports"; %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
    <jsp:param name="submenu" value="<%=IConstants.ACCOUNTING_SUB_MENU%>"/>
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
businesslist.add("Balance Sheet");
businesslist.add("Consolidated Balance Sheet");
businesslist.add("Profit and Loss");
businesslist.add("Consolidated Profit and Loss");
businesslist.add("Statement Of Cash Flows");

ArrayList<String> expenselist = new ArrayList<String>();
expenselist.add("Expense Details");
expenselist.add("Expense by Category");
expenselist.add("Expense by Supplier");
expenselist.add("Expense by Customer");

ArrayList<String> purchasepaylist = new ArrayList<String>();
purchasepaylist.add("Bill Details");
purchasepaylist.add("Payment Made");
purchasepaylist.add("Supplier Balances");
purchasepaylist.add("Supplier Credit Notes Details");
purchasepaylist.add("Supplier Aging Summary");

ArrayList<String> paymentsreclist = new ArrayList<String>();
paymentsreclist.add("Invoice Details");
paymentsreclist.add("Payment Received");
paymentsreclist.add("Customer Balances");
paymentsreclist.add("Customer Credit Notes Details");
paymentsreclist.add("Customer Aging Summary");

ArrayList<String> taxlist = new ArrayList<String>();
taxlist.add("Tax Return Summary");
taxlist.add("Tax Adjustments Summary");
taxlist.add("Tax Payments Summary");

ArrayList<String> acclist = new ArrayList<String>();
acclist.add("Bank Reconciliation");
acclist.add("Detailed General Ledger");
acclist.add("Journal");
acclist.add("Consolidated Journal");
acclist.add("Trail Balance");
acclist.add("Consolidated Trail Balance");

ArrayList<String> projectlist = new ArrayList<String>();
projectlist.add("Project Profit and Loss");
projectlist.add("Project Trial Balance");
projectlist.add("Consolidated Project Trial Balance");

%>
 <!-- Main content -->
 <div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 26.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Accounting Reports</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 26.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title">Accounting <%=title%></h1>
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
    <h4>  <!-- <i class="fa fa-shopping-bag" aria-hidden="true"></i> --> Business Overview  </h4>
    <%java.util.Hashtable htMenuItems = (java.util.Hashtable)menuListWithSequence.get(32); //	thirty three row
    java.util.Map htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
          					java.util.Iterator iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					boolean chkmenu11 = false,chkmenu12 = false;
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
<!--           					 <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../businessoverview/parentbalance'">Consolidated Balance Sheet</span></div></div> -->
<!--    							<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../businessoverview/parentprofitloss'">Consolidated Profit and Loss</span></div></div> -->
    </div>
    <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> Expenses  </h4>
    <%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < expenselist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=expenselist.get(i);          						
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
				                  <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						} else{
				               %>
					              <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>
    </div>
    <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> Payable </h4>
    <%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(3);//	Third column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < purchasepaylist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=purchasepaylist.get(i);          						
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
    <div class="row report-section">
    <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-list-alt" aria-hidden="true"></i> --> Receivable  </h4>
    <% htMenuItems = (java.util.Hashtable)menuListWithSequence.get(33); //	thirty four row
     htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
     if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
 	iterator = htMenuItemDetail.keySet().iterator();
		addlist = new ArrayList<String>();
		chkmenu11 = false; chkmenu12 = false;
		for (int i = 0; i < paymentsreclist.size(); i++) {
			chkmenu12=false;
			String ListmenuItemTitle=paymentsreclist.get(i);          						
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
           <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
        <%
			} else{
        %>
           <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
         <%
			}
		} %>     					 
    </div>
    <%-- <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> Tax  </h4>
    <%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
         					 iterator = htMenuItemDetail.keySet().iterator();
         					addlist = new ArrayList<String>();
         					chkmenu11 = false; chkmenu12 = false;
         					for (int i = 0; i < taxlist.size(); i++) {
         						chkmenu12=false;
         						String ListmenuItemTitle=taxlist.get(i);          						
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
         							//Tax Region based Tax Summary
         							if(menuItemTitle.equalsIgnoreCase("Tax Return Summary")||menuItemTitle.equalsIgnoreCase("Tax Payments Summary"))
         							{
         								System.out.print("Region "+region);
         								if(region.equalsIgnoreCase("GCC"))
         									menuItemURL="uae-"+menuItemURL;
         							}
         						%>
				                  <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
         						} else{
				               %>
					              <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
         						}
         					} %>
          					
    </div> --%>
    <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> Accountant  </h4>
<!--     <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/bankreconcilationsummary'">Bank Reconciliation</span></div></div> -->
    <%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(3);//	Third column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < acclist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=acclist.get(i);          						
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
				                  <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						}
          						
  
          						
          						else{
				               %>
					              <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
					            <%
          						}
          					} %>
<!--           					<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/parentjournalreport'">Consolidated Journal</span></div></div> -->
<!--           					<div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/parenttrialbalance'">Consolidated Trail Balance</span></div></div> -->
    </div>
    
    
    
        <div class="col-md-4">
    <h4>  <!-- <i class="fa fa-indent" aria-hidden="true"></i> --> Project  </h4>
<!--     <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../accountant/bankreconcilationsummary'">Bank Reconciliation</span></div></div> -->
    <%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(4);//	Third column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
          					addlist = new ArrayList<String>();
          					chkmenu11 = false; chkmenu12 = false;
          					for (int i = 0; i < projectlist.size(); i++) {
          						chkmenu12=false;
          						String ListmenuItemTitle=projectlist.get(i);          						
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
				                  <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="mspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-1" onclick="location.href='../<%=menuItemURL%>'"><%=menuItemTitle %></span></div></div>
				               <%
          						}
          						
  
          						
          						else{
				               %>
					              <div class="col-md-12" style="height: 28px;padding: 0px;" ><div class="bspan"><i class="fa fa-star-o" aria-hidden="true"></i><span class="menu-link pl-2" ><%=menuItemTitle %></span></div></div>
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
    
    </section>
    <jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>