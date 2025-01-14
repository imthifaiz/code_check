<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@page import="java.math.RoundingMode" %>
<%@ page import="java.text.DecimalFormat"%>
<%@page import="java.math.BigDecimal" %>
<%-- <%
StrUtils strUtils = new StrUtils();
String sItemDesc = strUtils.fString(request.getParameter("ITEM_DESC"));
String sItem = strUtils.fString(request.getParameter("ITEM"));
String plant = strUtils.fString((String)session.getAttribute("PLANT"));
String isCheckProduct = strUtils.fString(request.getParameter("isProductCheck"));
    
    //<a href="#" onClick="window.opener.form.ITEM.value='+item.PRODUCT+';window.close();">
%> --%>
<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="assets/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/custom.css">
<link rel="stylesheet" type="text/css" href="css/UbuntuFontFamily.css">
<link rel="stylesheet" type="text/css" href="css/font-awesome.min.css">
<link href="assets/css/datatablestyle.css" rel="stylesheet" type="text/css">
<style type="text/css">  .JColResizer{table-layout:fixed;} .JColResizer > tbody > tr > td, .JColResizer > tbody > tr > th{overflow:hidden}  .JPadding > tbody > tr > td, .JPadding > tbody > tr > th{padding-left:0!important; padding-right:0!important;} .JCLRgrips{ height:0px; position:relative;} .JCLRgrip{margin-left:-5px; position:absolute; z-index:5; } .JCLRgrip .JColResizer{position:absolute;background-color:red;filter:alpha(opacity=1);opacity:0;width:10px;height:100%;cursor: col-resize;top:0px} .JCLRLastGrip{position:absolute; width:1px; } .JCLRgripDrag{ border-left:1px dotted black;	} .JCLRFlex{width:auto!important;} .JCLRgrip.JCLRdisabledGrip .JColResizer{cursor:default; display:none;}</style>
<link rel="stylesheet" type="text/css" href="assets/css/bootstrap-multiselect.css"> -->

 <meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
<link rel="stylesheet" href="../css/style.css"> 
 
<!-- <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
 <link rel="stylesheet" href="../dist/css/bootstrap.min.css"/>
  <!-- DataTables -->
  <link rel="stylesheet" href="../dist/css/lib/datatables.min.css"/>
  <!-- jQuery 3 -->
  <script type="text/javascript" src="../dist/js/jquery.min.js"></script>
  <script type="text/javascript" src="../dist/js/jquery.dataTables.min.js"></script>
	<!-- jQuery UI -->
	<script src="../dist/js/jquery-ui-1.12.1.js"></script>
</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Product List</h3> 
</div>
</div>
<form method="post" name="form1">
<table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH >Product</TH>
       <TH >Description</TH>
        <TH >UOM</TH>
        <TH >Product Type</TH>
        <TH >IsActive</TH>
    </TR>
    </thead>
    <tbody>
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
StrUtils strUtils = new StrUtils();
ItemMstUtil itemUtil = new ItemMstUtil();
String plant =(String)session.getAttribute("PLANT");
System.out.println("plant"+plant);
String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
if(sItemDesc.length()>0){
sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
}
sItemDesc =sItemDesc+sExtraCond ;
ArrayList listQry = itemUtil.getMasterProductList(sItem,plant,sItemDesc);
/* String formtype="";
if(request.getParameter("FORMTYPE")!=null)
formtype=request.getParameter("FORMTYPE"); */
try

    {	
     // List listQry =  _LocMstDAO.getLocByWMS(PLANT,USERID,FROMLOC);
      for(int i =0; i<listQry.size(); i++) {
     //	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     	Map m=(Map)listQry.get(i);
     	 String item   =  strUtils.fString((String)m.get("ITEM"));
        String itemdesc    =  strUtils.fString((String)m.get("ITEMDESC"));
        String uom   =  strUtils.fString((String)m.get("STKUOM"));
        String itemtype   =  strUtils.fString((String)m.get("ITEMTYPE"));
        String isactive   =  strUtils.fString((String)m.get("ISACTIVE"));
        String isCheckProduct = strUtils.fString(request.getParameter("isProductCheck"));
        //String unitprice = strUtils.fString((String)m.get("UNITPRICE"));
        /* double prval = Double.valueOf(unitprice); 
		   BigDecimal bd = new BigDecimal(prval);
		 System.out.println(bd);
		 DecimalFormat format = new DecimalFormat("#.#####");		
		 format.setRoundingMode(RoundingMode.FLOOR);
		String priceval = format.format(bd); */
%>


<TR>

<%-- <%if(formtype.equalsIgnoreCase("taxinvoice")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="
			window.opener.form.UNITPRICE.value='<%=priceval%>';
			window.opener.form.ITEM.value='<%=item%>';window.close();"><%=item%></a></td>
			<%} else{%> --%>
			<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.ITEM.value='<%=item%>';window.opener.validateProduct();window.close();"><%=item%></a></td>
			<%-- <%} %> --%>
		<td align="left" class="main2">&nbsp;<%=itemdesc%></td>
		<td align="left" class="main2">&nbsp;<%=uom%></td>
		<td align="left" class="main2">&nbsp;<%=itemtype%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

	</TR>
	
   
<%
		}
	}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");
	}
%>
       
  </tbody>
</table>
<div class="text-center">       
        <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
	     </div>    
</form>
</body>
</html>
<script>
$(document).ready(function(){
	$('#myTable').dataTable();
    
});
</script>

<%-- <Script language="JavaScript" type="text/javascript">

var urlStr = "/track/ItemMstServlet";

$.ajax({type: "POST",url: urlStr, data: { ITEM: "<%=sItem%>" ,ITEM_DESC:"<%=sItemDesc%>", ACTION: "PRODUCT_LIST",PLANT:"<%=plant%>"},dataType: "json", success: callback });
// changes done by deen




function callback(data){
		
		var outPutdata = getTable();
		var ii = 0;
		var errorBoo = false;
		$.each(data.errors, function(i,error){
			if(error.ERROR_CODE=="99"){
				errorBoo = true;
			}
		});
		
		if(!errorBoo){
	        $.each(data.items, function(i,item){
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#dddddd";
                      
               

                         outPutdata = outPutdata+
                                    '<TR bgcolor="'+bgcolor+'">'+
                                             '<TD class="main2"><a href="javascript:SubmitValue( {&quot;PRODUCT&quot;: &quot;'+item.PRODUCT+'&quot;}); ">'+item.PRODUCT+'</a></TD>'+
                                             '<TD class="main2">'+item.DESC+'</TD>'+
                                             '<TD class="main2">'+item.UOM+'</TD>'+
                                             '<TD class="main2">'+item.ITEMTYPE+'</TD>'+
                                             '<TD class="main2">'+item.ACTIVE+'</TD>'+
                                    '</TR>';
                   
	        	ii++;
	            
	          });
		}else{
		//	outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="6"><BR><CENTER><B><FONT COLOR="RED">No details found!</FONT></B></CENTER></TD></TR>';
		}
        outPutdata = outPutdata +'</TABLE>';
        document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
        $('#spinner').html("");
   }
function getTable(){
            return '<TABLE border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">'+
            '<TR BGCOLOR="#000066">' +
            ' <TH align="left"><font color="white">Product ID </font></TH>'+
            '<TH align="left"><font color="white">Description</font></TH>'+
            '<TH align="left"><font color="white">UOM</font></TH>'+
            '<TH align="left"><font color="white">Product Type</font></TH>'+
            '<TH align="left"><font color="white">IsActive</font></TH>'
            '</TR>';	
                        
                     
       
}


function SubmitValue( json){
var item =json.PRODUCT;
var check ="<%=isCheckProduct%>";
window.opener.form.ITEM.value =item;
if(check=="true"){
window.opener.validateProduct();
}
window.close();
}

</script>
<title>Product List</title>

<link rel="stylesheet" href="../css/style.css">





<body bgcolor="#ffffff">
<form method="post" name="form1">

<div id="VIEW_RESULT_HERE"></div>
<div id="spinner" >
<p></p>
</div>

</form>	
 <SCRIPT LANGUAGE="JavaScript">
		document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
                document.getElementById('spinner').innerHTML ='<br><br><p align=center><img src="../images/spinner.gif"  > </p> ';
	
</SCRIPT> --%>






