<%String title="Select Apps"; %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="headerWithoutSide.jsp" flush="true">
<jsp:param value="<%=title %>" name="title"/>
</jsp:include>
<%@page import="com.track.util.http.HttpUtils"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
String plant   = session.getAttribute("PLANT").toString();
arr = _PlantMstUtil.getPlantMstDetails(plant);
String isInv="0", isAcc="0", isPyrl="0", 
invClass = "contents-disabled", isAccClass = "contents-disabled",
isPyrlClass = "contents-disabled";
if(arr.size()>0){
	for (int i =0; i<arr.size(); i++){
		 Map m = (Map) arr.get(i);
		 isInv = (String)m.get("ENABLE_INVENTORY");
		 isAcc = (String)m.get("ENABLE_ACCOUNTING");
		 isPyrl = (String)m.get("ENABLE_PAYROLL");
	}
}
invClass = (isInv.equalsIgnoreCase("1"))? "inventrycontents" : invClass;
isAccClass = (isAcc.equalsIgnoreCase("1"))? "accountcontents" : isAccClass;
isPyrlClass = (isPyrl.equalsIgnoreCase("1"))? "payrollcontents" : isPyrlClass;
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
.contents:hover {
    border-color: #5a6771;
}

.contents{
	cursor: pointer;
	-webkit-box-shadow: 0 5px 20px 2px rgba(0,0,0,.12);
    box-shadow: 0 5px 20px 2px rgba(0,0,0,.12);
    border: 1px solid transparent;
    border-radius: .2rem;
    overflow: hidden;
    padding: 15px;
    margin: 15px;
    -webkit-transition: all .3s;
    transition: all .3s;
    height:180px;
}
.contents-disabled {
    opacity:0.5;
	-webkit-box-shadow: 0 5px 20px 2px rgba(0,0,0,.12);
    box-shadow: 0 5px 20px 2px rgba(0,0,0,.12);
    border: 1px solid transparent;
    border-radius: .2rem;
    overflow: hidden;
    padding: 15px;
    margin: 15px;
    -webkit-transition: all .3s;
    transition: all .3s;
    height:180px;
}

.contents .category-icon, .contents-disabled .category-icon{
	font-size: 25px;
}

.contents .category-name, .contents-disabled .category-name{
	color: #4a4a4a;
    font-size: 16px;
    text-align: center;
    font-weight: 600;
}


@media (max-width: 767px) and (min-width: 320px){
	.content {
	    width:100%;
	    margin: 7% auto;
	}
	.contents, .contents-disabled {
	    height: 90px;
	    margin: 7px;
	    padding: 10px;
	}
}
</style>
    <!-- Main content -->
    
    <div class="container" style="margin: 3% auto;">
    	<div class="row text-center">
    		<div class="col-sm-12">
    			<%-- <a href="#" class="login-logo"><img src="<%=rootURI%>/jsp/dist/img/Final-01.png"></a> --%>
    			<a href="#" class="login-logo"><img src="<%=rootURI%>/jsp/dist/img/logo-01.png"></a>
   			</div>
    		<div class=" col-xs-offset-3 col-sm-offset-2 col-sm-10">
    			
    			<div class="col-xs-6 col-sm-3 contents <%=invClass%>">
	    			<img src="<%=rootURI%>/jsp/dist/img/ordermgt.jpg" style="width: 25%;margin-top: 10%;">
					<h3 class="category-name" style="margin-top: 5px;">Inventory/Order Management</h3>
	    		</div>
	    		<div class="col-xs-6 col-sm-3 contents <%=isAccClass%>">
	    			<img src="<%=rootURI%>/jsp/dist/img/accounting.jpg" style="width: 30%;margin-top: 10%;">
					<h3 class="category-name" style="margin-top: 5px;">Accounting</h3>
	    		</div>
	    		<div class="col-xs-6 col-sm-3 contents <%=isPyrlClass%>">
	    			<img src="<%=rootURI%>/jsp/dist/img/payroll.jpg" style="width: 30%;margin-top: 10%;">
					<h3 class="category-name" style="margin-top: 5px;">Payroll</h3>
	    		</div>
   			</div>
    		
    	</div>
    </div>
    <form name="form" method="post"><input type="hidden" name="PDA" value=""/></form>
    <script>

function reDirect(strCompany)
{
	document.form.action  = "checkUserMultiCompany.jsp?COMPANY="+strCompany;
	document.form.submit();
}
$(".inventrycontents").click(function(){
	//window.location.href='../home';
	window.location.href = "<%=rootURI%>/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=INVENTORY";
});

$(".accountcontents").click(function(){
	//window.location.href='../home';
	window.location.href = "<%=rootURI%>/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=ACCOUNTING";
});

$(".payrollcontents").click(function(){
	//window.location.href='PayrollHome.jsp';
	window.location.href = "<%=rootURI%>/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=PAYROLL";
});
</script>
    <!-- /.content -->
    <jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>