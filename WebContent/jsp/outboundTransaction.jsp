<%String title="Sales Transaction"; %>
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
              <li class="active"><a href="#tab_1" data-toggle="tab">Sales</a></li>
            </ul>
            <div class="tab-content">
              <div class="tab-pane active" id="tab_1">
                <%java.util.Hashtable htMenuItems = (java.util.Hashtable)menulist.get(10); 
                	java.util.List list = new java.util.ArrayList(htMenuItems.keySet());
                	for (int listIndex = list.size() - 1; listIndex >=0; listIndex --){
                		java.util.Hashtable htMenuItemDetail = (java.util.Hashtable)htMenuItems.get(list.get(listIndex)); 
                		String menuItemTitle = "" + htMenuItemDetail.keys().nextElement();
                		if (menuItemTitle.indexOf("Point of Sales") != -1){
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
    <!-- /.content -->
    <jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>