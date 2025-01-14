<%@ page import="com.track.util.http.HttpUtils"%>
<% 
	String rootURI = HttpUtils.getRootURI(request);
%>
  <!-- /.content-wrapper -->
  <%if ("Dashboard".equals(request.getParameter("title"))){ %>
  <footer class="main-footer" style="margin: 0px;">
    <p>
        Version 25.01.06.01 Copyright Sunpro Inno Apps Pte Ltd &copy; 2024, All rights reserved.  
   <!-- Version 22.06.01.01 Copyright U-CLO Pte Ltd &copy; 2022, All rights reserved. --> 
    </p>
     <%--<strong><a href=""><img class="dash-logo" src="<%=rootURI%>/jsp/dist/img/logo.png"></a></strong> All rights
    reserved. --%>
  </footer>
   <%} %>
  <!-- Control Sidebar -->
  
<!-- </div> -->
<!-- ./wrapper -->

<!-- Bootstrap 3.3.7 -->
<script src="<%=rootURI%>/jsp/dist/js/bootstrap.min.js"></script>
<!-- DataTables -->
<!-- <script src="<%=rootURI%>/jsp/dist/js/jquery.dataTables.min.js"></script> -->
<%--<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.32/pdfmake.min.js"></script> --%>
<script src="<%=rootURI%>/jsp/dist/js/pdfmake.min-0.1.32.js"></script>
<%--<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.32/vfs_fonts.js"></script> --%>
<script src="<%=rootURI%>/jsp/dist/js/vfs_fonts-0.1.32.js"></script>
<%--
<script type="text/javascript" src="https://cdn.datatables.net/v/bs/jszip-2.5.0/dt-1.10.16/b-1.5.1/b-colvis-1.5.1/b-html5-1.5.1/fh-3.1.3/r-2.2.1/rg-1.0.2/sl-1.2.5/datatables.min.js"></script>
 --%>
<script src="<%=rootURI%>/jsp/dist/js/lib/datatables.min.js"></script>
<script src="<%=rootURI%>/jsp/dist/js/dataTables.bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="<%=rootURI%>/jsp/dist/js/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="<%=rootURI%>/jsp/dist/js/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="<%=rootURI%>/jsp/dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="<%=rootURI%>/jsp/dist/js/demo.js"></script>
<script src="<%=rootURI%>/jsp/dist/js/lib/numberutil.js"></script>
<!-- Select2 -->
<script src="<%=rootURI%>/jsp/dist/js/select2.full.min.js"></script>
<%if (request.getParameter("nobackblock") == null || !"1".equals(request.getParameter("nobackblock"))){ %>
<script type="text/javascript">
     //$('select').select2({ minimumResultsForSearch: -1 });
     //window.history.forward(1);
     window.history.pushState(null, "", window.location.href);        
     window.onpopstate = function() {
         window.history.pushState(null, "", window.location.href);
     };
</script>
<%} %>
<script>
var $datePickerRangePages = ['create_employee.jsp','maint_employee.jsp'];
var $currentPage = location.href.toString().substring(location.href.toString().lastIndexOf("/")).replace("/", "");
var $currentPageIndex = $.inArray($currentPage, $datePickerRangePages);
$(document).ready(function(){
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
	if (typeof postDatePickerInit != 'undefined'){
		postDatePickerInit();
	}
});
function showLoader(){
	try{$("html, body").animate({ scrollTop: 0 }, "slow");}catch(e){}
	$("#loader").show();
	$(".wrapper").css("opacity","0.1");
}

function hideLoader(){
	$("#loader").hide();
	$(".wrapper").css("opacity","1");
}
function getLocalStorageValue(key, defaultValue, controlId){
	var value = localStorage.getItem(key) == null ? defaultValue : localStorage.getItem(key);
	if (controlId != ''){
		$('#' + controlId).val(value);
	}
	return value;
}

function storeInLocalStorage(key, value){
	localStorage.setItem(key, value);
}
function updateSelectedCompaniesInLocalStorage(key){
	var selectedCompanies = '';
	$(".companyname").each(function( index  ) {
	  		if ($( this ).prop('checked')){
			  	selectedCompanies += $( this ).val() + ',';
	      }
	});    
	selectedCompanies = selectedCompanies.substring(0, selectedCompanies.length - 1);
	storeInLocalStorage(key, selectedCompanies);
}
function getNumberInMillionFormat(numberToConvert){
	return ("" + toFixed(numberToConvert)).replace(/\d(?=(\d{3})+\.)/g, "$&,");
}

function toFixed(x) {
	  if (Math.abs(x) < 1.0) {
	    var e = parseInt(x.toString().split('e-')[1]);
	    if (e) {
	        x *= Math.pow(10,e-1);
	        x = '0.' + (new Array(e)).join('0') + x.toString().substring(2);
	    }
	  } else {
	    var e = parseInt(x.toString().split('+')[1]);
	    if (e > 20) {
	        e -= 20;
	        x /= Math.pow(10,e);
	        x += (new Array(e+1)).join('0');
	    }
	  }
	  if (("" + x).indexOf(".") == -1){
		  x += ".00";
	  }
	  return x;
	}
</script>
</body>
</html>