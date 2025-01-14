<%@ page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.gates.Generator"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%
String rootURI = HttpUtils.getRootURI(request);
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>SUNPRO Login</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/AdminLTE.min.css">
  <!-- iCheck -->
 <!--  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/blue.css"> -->


  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->

  <!-- Google Font -->
  <%--<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic"> --%>
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/google-fonts.css">
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/main-style.css">
<%
  Generator generator = new Generator();
  String dt = generator.getDate();
  Cookie cookies[] = request.getCookies();
  String sCompany="",userId="";
  if(cookies!=null)
  {
  	for(int i=0;i<cookies.length;i++)
  	{
  		System.out.println(cookies [i].getName());
  		if (cookies [i].getName().equals("userid"))
  		{
  			userId= cookies[i].getValue();
 		}else if (cookies [i].getName().equals ("JDCCAUTION")){
			Cookie selcookie = cookies[i];
			sCompany = selcookie.getValue();
  		}
  	}
 }
%>
<script type="text/javascript">
function validateForm(){

	 if(document.loginForm.name.value == ""){
	    alert("Please Enter User ID !");
	    document.loginForm.name.focus();
	    return false;
	 }
	 if(document.loginForm.pwd.value == ""){
	   alert("Please Enter Password");
	   document.loginForm.pwd.focus();
	   return false;
	 }
	 document.loginForm.submit();
	}

	function focusOnLoad(){
	    document.getElementById("name").focus();
	}
</script>
</head>
<div id="loader"></div>
<body class="hold-transition login-page">
<div class="login-box">
  
  <div class="login-box-body">
  	<div class="box-error">
<%
    Enumeration<?> e = request.getParameterNames();
	boolean isWarnExist = false;
    while(e.hasMoreElements())
    {
        String s = e.nextElement().toString();

        if(s.equalsIgnoreCase("warn"))
            {
     	   isWarnExist = true;
                String msg = request.getParameter("warn");
                out.write("<p>"+msg+"</p>");
            }
    }
 %>
 </div>
 <div class="" style="text-align:center">
    <%-- <a href="#" class="login-logo"><img src="<%=rootURI%>/jsp/dist/img/Final-01.png"></a> --%>
    <a href="#" class="login-logo"><img src="<%=rootURI%>/jsp/dist/img/logo-01.png"></a>
  </div>
  <hr style="border-top: 1px solid #e2e1e1;">
  <!-- /.login-logo -->
    <p class="login-box-msg" style="text-align:center">Sign In</p>

    <form action="<%=rootURI%>/authenticate" method="post" class="fo-login" name="loginForm">
      <div class="form-group has-feedback">
        <label class="slideup">Email or User ID</label>
        <input type="text" name="name" id="name"  value="<%=userId%>" class="form-control" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)) {document.loginForm.pwd.focus();}">
        <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
      </div>
      <div class="form-group has-feedback">
        <label class="slideup">Password</label>
        <input type="password" value="" name="pwd" class="form-control"  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){ validateForm();}">
        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
      </div>
      <div class="row">
        <div class="col-xs-12">
          <div class="checkbox">
            <label>
              <input name="remember_me" type="checkbox" value="true"> Remember my Email or User ID
            </label>
          </div>
        </div>
        <!-- /.col -->
        <div class="col-xs-12">
          <button type="submit" class="btn btn-primary btn-block btn-flat fa fa-unlock">Sign In</button>
        </div>
        <!-- /.col -->
      </div>
      <input type="hidden" name="PDA" value="">
    </form>
    <a href="<%=rootURI%>/jsp/forgotPassword.jsp">I forgot my password</a><br>
    <%--<a href="register.html" class="text-center"><span class="b-link">New to SUNPRO?</span>Create an account</a> --%>
  </div>
  <!-- /.login-box-body -->
</div>
<!-- /.login-box -->
<!-- jQuery 3 -->
<script src="<%=rootURI%>/jsp/dist/js/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="<%=rootURI%>/jsp/dist/js/bootstrap.min.js"></script>
<script src="<%=rootURI%>/jsp/dist/js/index.js"></script>
<script type="text/javascript">
<%if (!isWarnExist){%>
$('.box-error').css('display','none');
<%}%>
$(window).on('load', function() {
	  $("#loader").hide();
});
</script>
<!-- iCheck -->
<!-- <script src="<%=rootURI%>/jsp/dist/js/icheck.min.js"></script>
<script>
  $(function () {
    $('input').iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue',
      increaseArea: '20%' // optional
    });
  });
</script> -->
<script type="text/javascript">focusOnLoad();</script>
</body>
</html>