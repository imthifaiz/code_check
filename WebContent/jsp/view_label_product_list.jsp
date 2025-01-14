<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%
StrUtils strUtils = new StrUtils();
String sItemDesc = strUtils.fString(request.getParameter("ITEM_DESC"));
String sItem = strUtils.fString(request.getParameter("ITEM"));
String plant = strUtils.fString((String)session.getAttribute("PLANT"));

    
    //<a href="#" onClick="window.opener.form.ITEM.value='+item.PRODUCT+';window.close();">
%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>

<script language="javascript">


</script>




<Script language="JavaScript" type="text/javascript">



var urlStr = "/track/ItemMstServlet";

// Call the method of JQuery Ajax provided
$.ajax({type: "POST",url: urlStr, data: { ITEM: "<%=sItem%>" ,ITEM_DESC:"<%=sItemDesc%>", ACTION: "PRODUCT_LIST",PLANT:"<%=plant%>"},dataType: "json", success: callback });



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
window.opener.form1.ITEM.value =item;
window.opener.validateProduct();
window.close();
}

</script>
<title>Product List</title>

<link rel="stylesheet" href="css/style.css">




</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">

<div id="VIEW_RESULT_HERE"></div>
<div id="spinner" >
<p></p>
</div>

</form>	
 <SCRIPT LANGUAGE="JavaScript">
		document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
                document.getElementById('spinner').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
	
</SCRIPT>
</body>
</html>





