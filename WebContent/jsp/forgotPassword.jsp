<%@page import="com.track.gates.Generator"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>SUNPRO Forgot Password</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="dist/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="dist/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="dist/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/AdminLTE.min.css">
  <!-- iCheck -->
 <!--  <link rel="stylesheet" href="dist/css/blue.css"> -->


  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->

  <!-- Google Font -->
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
   <link rel="stylesheet" href="dist/css/main-style.css">
<%
  Generator generator = new Generator();
  String dt = generator.getDate();
  Cookie cookies[] = request.getCookies();
  String sCompany="";
  if(cookies!=null)
  {
  	for(int i=0;i<cookies.length;i++)
  	{
  		if (cookies [i].getName().equals ("JDCCAUTION"))
 		 {
			Cookie selcookie = cookies[i];
			sCompany = selcookie.getValue();
			break;
						
  		}
  	}
 }
%>
<script type="text/javascript">
function validateForm(){

	 if(document.forgotPasswordForm.name.value == ""){
	    alert("Please Enter User ID !");
	    document.forgotPasswordForm.name.focus();
	    return false;
	 }
	 document.forgotPasswordForm.submit();
	}

	function focusOnLoad(){
	    document.getElementById("name").focus();
	}
</script>
</head>
<body class="hold-transition login-page">
  <div class="box-error">
<%
    Enumeration e = request.getParameterNames();
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
<div class="login-box">
  <!-- /.login-logo -->
  <div class="login-box-body">
   <div class="" style="text-align:center">
    <!-- <a href="#" class="login-logo"><img src="dist/img/Final-01.png"></a> -->
    <a href="#" class="login-logo"><img src="dist/img/logo-01.png"></a>
  </div>
  <hr style="border-top: 1px solid #e2e1e1;">
    <p class="login-box-msg" style="text-align:center">Forgot Password</p>

    <form action="checkUserForForgotPassword.jsp" method="post" class="fo-login" name="forgotPasswordForm">
      <div class="form-group has-feedback">
        <label class="slideup">Email or User ID</label>
        <input type="text" name="name" id="name" class="form-control" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)) {document.forgotPasswordForm.submit();}">
        <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
      </div>
      <div class="row">
        <!-- /.col -->
        <div class="col-xs-12">
          <button type="submit" class="btn btn-primary btn-block btn-flat fa fa-unlock">Recover</button>
        </div>
        <!-- /.col -->
      </div>
    </form>
    <a href="../login">Sign In</a><br>
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
<%if (!isWarnExist){%>
$('.box-error').css('display','none');
<%}%>
</script>
<!-- iCheck -->
<!-- <script src="dist/js/icheck.min.js"></script>
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