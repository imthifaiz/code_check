<%@page import="net.sf.json.JSONObject"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import ="javax.script.ScriptEngineManager"%>
<%@ page import ="javax.script.ScriptEngine"%>
<%@ page import ="javax.script.Invocable"%>

<%
String title = "Notification";

String sUserId = (String) session.getAttribute("LOGIN_USER");
String Plant = (String) session.getAttribute("PLANT");
DateUtils dateutils = new DateUtils();

String sql = "update USER_INFO set LAST_NOTFICATION_CHK="+dateutils.getDateTime()+
		"  where USER_ID='"+sUserId+"'"+"and dept='"+Plant+"'";
// System.out.println("Update user"+sql.toString());
	new sqlBean().insertRecords(sql);
	
	List listQry = new userBean().getNotificationList(Plant, sUserId);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript" src="js/general.js"></script>
 <script type="text/javascript" src="js/general.js"></script>
 <script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
 <style>
 .notification-detail{
 	padding: 20px;
 }
 .notification-date{
	padding: 5px 0 15px;
    font-size: 15px;
    color: #555;
 }
 .notification-heading{
    padding-bottom: 8px;
    color: #070607;
    font-size: 22px;
    font-weight: 600;
    line-height: 35px;
 }
 .notification-desc{
    font-size: 16px;
    line-height: 26px;
    color: #333;
 }
 </style>
 <%@ include file="header.jsp"%>


<div class="container-fluid m-t-20" style="background: #f4f8fd;">
	<div class="notification-body" style="width:60%;background:#fff;margin:25px auto;">
		<% for (int i =0; i<listQry.size(); i++){
			Map map = (Map) listQry.get(i);%>		
		<div class="notification-detail">
			<p class="notification-date"><%=(String) map.get("CRAT")%></p>
			<p class="notification-heading"><%=(String) map.get("Title")%></p>
			<p class="notification-desc"><%=(String) map.get("Description")%></p>
		</div>
		<% } %>
	</div>
</div>
    
<script>
        $(document).ready(function(){
        	var  d = document.getElementById("select_id").value;
            document.getElementById('TaxLabel').innerHTML = d + " No.:";
        }
        );
        
        
		function readURL(input) {
		    if (input.files && input.files[0]) {
		        var reader = new FileReader();

		        reader.onload = function (e) {
		        	
		        	
		            $('#item_img').attr('src', e.target.result);
		        }

		        reader.readAsDataURL(input.files[0]);
		    }
		}
		$(document).on('change', '#productImg', function (e) {
		    readURL(this);
		});
        </script>
	
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>