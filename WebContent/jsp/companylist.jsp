<%String title="Company List"; %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="headerWithoutSide.jsp" flush="true">
<jsp:param value="<%=title %>" name="title"/>
</jsp:include>
<%@ page import="com.track.db.util.*"%>
 <%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import="com.track.util.http.HttpUtils"%>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<jsp:useBean id="misc"  class="com.track.gates.miscBean" />
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<jsp:useBean id="df"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<%
String rootURI = HttpUtils.getRootURI(request);
PlantMstUtil _PlantMstUtil =new PlantMstUtil();
java.util.List arr   = new java.util.ArrayList();
StrUtils strUtils = new StrUtils();
MLogger mLogger=new MLogger();
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
String company="",action="",result="",url="";
int statusCode=0;
String user_id   = session.getAttribute("LOGIN_USER").toString();
String pwd   = session.getAttribute("IDS").toString();
String encrPwd  = eb.encrypt(pwd);
//System.out.println("userid...."+user_id );
action = StrUtils.fString(request.getParameter("action"));
arr= _PlantMstUtil.validateUser(user_id,encrPwd);
%>
<%
        Enumeration e = request.getParameterNames();
        while(e.hasMoreElements())
        {
            String s = e.nextElement().toString();

            if(s.equalsIgnoreCase("warn"))
                {
                    String msg = request.getParameter("warn");
                    
                    out.write("<br><center><font class='mainred'><b>"+msg+"</b></font</center>");
                }
        }
 %>
<style>
.content {
    width:50%;
    margin: 7% auto;
}
 @media (max-width: 767px) and (min-width: 320px){
	.content {
	    width:75%;
	    margin: 7% auto;
	}
}
</style>
    <!-- Main content -->
    <section class="content">
     	<div class="text-center">
     		<%-- <a href="#" class="login-logo"><img src="<%=rootURI%>/jsp/dist/img/Final-01.png"></a> --%>
     		<a href="#" class="login-logo"><img src="<%=rootURI%>/jsp/dist/img/logo-01.png"></a>
		<table id="tableAccount" class="table table-bordered table-hover dataTable no-footer" 
		role="grid" aria-describedby="tableAccount_info" width="100%">
			<thead>
	            <tr role="row">
	            	<th>Company Name</th>
	            </tr>
	        </thead>
	        <tbody>
	        <%
               if(arr.size()>0){ 
            	for (int i =0; i<arr.size(); i++){
            		 Map m = (Map) arr.get(i);
            		 System.out.println(m);
             %>
	        	<tr>
	        		<td class="text-center"><a href="javascript:{void(0);}" onclick="{reDirect('<%=(String)m.get("plant")%>');}" target="_top"><%=(String)m.get("plantname")%></a></td>
	        	</tr>
                 <% 
				 } //end of for loop
	   		   }// end of arr size checking
			%>
	        </tbody>
		</table>
	</div>       
    </section>
    <form name="form" method="post"><input type="hidden" name="PDA" value=""/></form>
    <script>

function reDirect(strCompany)
{
	document.form.action  = "<%=rootURI%>/jsp/checkUserMultiCompany.jsp?COMPANY="+strCompany;
	document.form.submit();
}
</script>
    <!-- /.content -->
    <jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>