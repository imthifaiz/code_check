<%@page import="com.track.gates.Generator"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@ page import="com.track.util.*"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>SUNPRO Employee Login</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<!-- Bootstrap 3.3.7 -->
<link rel="stylesheet" href="dist/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="dist/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="dist/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="dist/css/AdminLTE.min.css">

<link rel="stylesheet" href="dist/css/google-fonts.css">
<link rel="stylesheet" href="dist/css/main-style.css">
<link rel="stylesheet" href="css/emp_main.css">
<%
	StrUtils StrUtils = new StrUtils();
	String fieldDesc=StrUtils.fString(request.getParameter("result"));
%>
<script type="text/javascript">
	function validateForm() {

		if (document.loginForm.name.value == "") {
			alert("Please Enter User ID !");
			document.loginForm.name.focus();
			return false;
		}
		if (document.loginForm.pwd.value == "") {
			alert("Please Enter Password");
			document.loginForm.pwd.focus();
			return false;
		}

		return true;
	}

	/* function focusOnLoad() {
		document.getElementById("name").focus();
	} */
</script>
</head>
<div id="loader"></div>
<body class="hold-transition emp-login-page">
	<div class="login-box inlogin-box">
		<div class="" style="text-align: center;padding:8%;">
				<!-- <a href="#" class="login-logo"><img src="dist/img/Final-01.png"></a> -->
				<a href="#" class="login-logo"><img src="dist/img/logo-01.png"></a>
		</div>
		<div class="login-box-body">
			<div class="box-error" style="color:red;">
				<%=fieldDesc%>
			</div>
			
			<form  name="loginForm" action="/track/EmployeeLoginServlet?Submit=login"  method="post" onsubmit="return validateForm()">
				<div class="form-group has-feedback">
					<label>User ID</label> 
					<input type="text" name="name" id="name" value="" class="form-control">
					<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<label>Password</label> 
					<input type="password" name="pwd" value="" class="form-control">
					<span class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<div class="checkbox">
							<label> 
								<input name="remember_me" type="checkbox" value="true"> Remember my User ID
							</label>
						</div>
					</div>
					<!-- /.col -->
					<div class="col-xs-12">
						<button type="submit" class="btn btn-block emp-btn-fate fa fa-unlock">Sign In</button>
					</div>
					<!-- /.col -->
				</div>
			</form>
		</div>
		<!-- /.login-box-body -->
	</div>
	<!-- /.login-box -->
	<!-- jQuery 3 -->
	<script src="dist/js/jquery.min.js"></script>
	<!-- Bootstrap 3.3.7 -->
	<script src="dist/js/bootstrap.min.js"></script>
	<script src="dist/js/index.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$(".box-error").show().delay(3000).fadeOut();
	});

	</script>
 <script type="text/javascript">
		$(window).on('load', function() {
			$("#loader").hide();
		});
	</script>
		<!--
	<script type="text/javascript">
		focusOnLoad();
	</script> -->
</body>
</html>