<title>Thank You</title>
<jsp:useBean id="df"  class="com.track.gates.defaultsBean" />
<%
    session = request.getSession();
    df.insertToLog(session, "User Logout Information", "Logged out Successfully");   //  Inserting into the user log
    session.invalidate();
    Runtime.getRuntime().gc();
    String contextPath = request.getContextPath();
    if(contextPath.contains("track")){
    	response.sendRedirect("../login");	
    }else{
    	response.sendRedirect("../../signin");
    }
    
%>

<script type="text/javascript">

	$.ajax({
		type : "POST",
		url : "/inventory/jsp/logout.jsp",
		async : false,
		data : {
			
		},
		success : function(data) {
			
		}
	});
	$.ajax({
		type : "POST",
		url : "/accounting/jsp/logout.jsp",
		async : false,
		data : {},
		success : function(data) {
			
		}
	});
	$.ajax({
		type : "POST",
		url : "/payroll/jsp/logout.jsp",
		async : false,
		data : {},
		success : function(data) {
		
		}
	});
</script>