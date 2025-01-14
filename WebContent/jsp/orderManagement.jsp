<%String title="Order Management"; %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param value="<%=title %>" name="title"/>
</jsp:include>
<%
java.util.ArrayList menulist = (java.util.ArrayList)session.getAttribute("DROPDOWN_MENU");
%>
    <!-- Main content -->
    <section class="content">
            
     <div class="box-border-in col-xs-12">
                   
      <div class="col-xs-12 pad-tb20 ">
        <div class="nav-tabs-custom setting-nav">  <!-- Custom Tabs -->
            <ul class="nav nav-tabs">
              <li class="active"><a href="#tab_1" id="a_IBO" data-toggle="tab">Purchase</a></li>
              <li><a href="#tab_2" id="a_EO" data-toggle="tab">Sales Estimate</a></li>
              <li><a href="#tab_3" id="a_OBO" data-toggle="tab">Sales</a></li>
              <li><a href="#tab_4" id="a_LBO" data-toggle="tab">Rental And Service</a></li>
              <li><a href="#tab_5" id="a_TBO" data-toggle="tab">Transfer</a></li>
              <li><a href="#tab_6" id="a_CO" data-toggle="tab">Force Close Orders</a></li>
            </ul>
            
            
            <div class="tab-content">
              <div class="tab-pane active" id="tab_1">
                <%java.util.Hashtable htMenuItems = (java.util.Hashtable)menulist.get(3); 
                	java.util.List list = new java.util.ArrayList(htMenuItems.keySet());
                	for (int listIndex = list.size() - 1; listIndex >=0; listIndex --){
                		java.util.Hashtable htMenuItemDetail = (java.util.Hashtable)htMenuItems.get(list.get(listIndex)); 
                		String menuItemTitle = "" + htMenuItemDetail.keys().nextElement();
                		if (menuItemTitle.indexOf("Purchase") == -1){
                			continue;
                		}
                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
                %>
                  <div class="col-xs-12 col-sm-6 col-md-4 pad-tb20"><p><strong onclick="location.href='<%=menuItemURL%>'"><%=menuItemTitle %></strong></p></div>
               <%} %>
              </div>
              <!-- /.tab-pane -->
              <div class="tab-pane" id="tab_2">
                <%htMenuItems = (java.util.Hashtable)menulist.get(3); 
                	list = new java.util.ArrayList(htMenuItems.keySet());
                	for (int listIndex = list.size() - 1; listIndex >=0; listIndex --){
                		java.util.Hashtable htMenuItemDetail = (java.util.Hashtable)htMenuItems.get(list.get(listIndex)); 
                		String menuItemTitle = "" + htMenuItemDetail.keys().nextElement();
                		if (menuItemTitle.indexOf("Sales Estimate") == -1){
                			continue;
                		}
                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
                %>
                  <div class="col-xs-12 col-sm-6 col-md-4 pad-tb20"><p><strong onclick="location.href='<%=menuItemURL%>'"><%=menuItemTitle %></strong></p></div>
               <%} %>
              </div>
              <!-- /.tab-pane -->
              <div class="tab-pane" id="tab_3">
                <%htMenuItems = (java.util.Hashtable)menulist.get(3); 
                	list = new java.util.ArrayList(htMenuItems.keySet());
                	for (int listIndex = list.size() - 1; listIndex >=0; listIndex --){
                		java.util.Hashtable htMenuItemDetail = (java.util.Hashtable)htMenuItems.get(list.get(listIndex)); 
                		String menuItemTitle = "" + htMenuItemDetail.keys().nextElement();
                		if (menuItemTitle.indexOf("Sales") == -1 || menuItemTitle.indexOf("Sales Estimate") != -1){
                			continue;
                		}
                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
                %>
                  <div class="col-xs-12 col-sm-6 col-md-4 pad-tb20"><p><strong onclick="location.href='<%=menuItemURL%>'"><%=menuItemTitle %></strong></p></div>
               <%} %>
              </div>
              <!-- /.tab-pane -->
               <div class="tab-pane" id="tab_4">
                <%htMenuItems = (java.util.Hashtable)menulist.get(3); 
                	list = new java.util.ArrayList(htMenuItems.keySet());
                	for (int listIndex = list.size() - 1; listIndex >=0; listIndex --){
                		java.util.Hashtable htMenuItemDetail = (java.util.Hashtable)htMenuItems.get(list.get(listIndex)); 
                		String menuItemTitle = "" + htMenuItemDetail.keys().nextElement();
                		if (menuItemTitle.indexOf("Rental And Service") == -1){
                			continue;
                		}
                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
                %>
                  <div class="col-xs-12 col-sm-6 col-md-4 pad-tb20"><p><strong onclick="location.href='<%=menuItemURL%>'"><%=menuItemTitle %></strong></p></div>
               <%} %>
              </div>
              <!-- /.tab-pane -->
               <div class="tab-pane" id="tab_5">
                <%htMenuItems = (java.util.Hashtable)menulist.get(3); 
                	list = new java.util.ArrayList(htMenuItems.keySet());
                	for (int listIndex = list.size() - 1; listIndex >=0; listIndex --){
                		java.util.Hashtable htMenuItemDetail = (java.util.Hashtable)htMenuItems.get(list.get(listIndex)); 
                		String menuItemTitle = "" + htMenuItemDetail.keys().nextElement();
                		if (menuItemTitle.indexOf("Transfer") == -1){
                			continue;
                		}
                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
                %>
                  <div class="col-xs-12 col-sm-6 col-md-4 pad-tb20"><p><strong onclick="location.href='<%=menuItemURL%>'"><%=menuItemTitle %></strong></p></div>
               <%} %>
              </div>
              <!-- /.tab-pane -->
              <div class="tab-pane" id="tab_6">
                <%htMenuItems = (java.util.Hashtable)menulist.get(3); 
                	list = new java.util.ArrayList(htMenuItems.keySet());
                	for (int listIndex = list.size() - 1; listIndex >=0; listIndex --){
                		java.util.Hashtable htMenuItemDetail = (java.util.Hashtable)htMenuItems.get(list.get(listIndex)); 
                		String menuItemTitle = "" + htMenuItemDetail.keys().nextElement();
                		if (menuItemTitle.indexOf("Outstanding") == -1){
                			continue;
                		}
                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
                %>
                 	<div class="col-xs-12 col-sm-6 col-md-4 pad-tb20"><p><strong onclick="location.href='<%=menuItemURL%>'"><%=menuItemTitle %></strong></p></div>
               <%} %>
              </div>
              <!-- /.tab-pane -->
            </div>
            <!-- /.tab-content -->
          </div>
          <!-- nav-tabs-custom -->
        </div>
    </div>
    </section>
<script type="text/javascript">
		$(function(){
			<%
				if (request.getParameter("t") != null){
			%>
				$('#a_<%=request.getParameter("t")%>').click();
			<%	
				}
			%>
		});
	</script>
    <!-- /.content -->
    <jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>