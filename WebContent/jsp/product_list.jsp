<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
 
 <title>Product List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="../css/style.css">
 
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</head>
<body>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Product List</h3> 
</div>
</div>

<form method="post" name="form1">
<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH >Product</TH>
       <TH >Description</TH>
        <TH >UOM</TH>
        
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

try

    {
     // List listQry =  _LocMstDAO.getLocByWMS(PLANT,USERID,FROMLOC);
      for(int i =0; i<listQry.size(); i++) {
     //	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     	Map m=(Map)listQry.get(i);
     	 String item   =  strUtils.fString((String)m.get("ITEM"));
        String itemdesc    =  strUtils.fString((String)m.get("ITEMDESC"));
        String uom   =  strUtils.fString((String)m.get("STKUOM"));
        String isactive   =  strUtils.fString((String)m.get("ISACTIVE"));
        String productclass=strUtils.fString((String)m.get("PRD_CLS_ID"));
%>

<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.ITEM.value='<%=item%>';window.close();"><%=item%></a></td>
		<td align="left" class="main2">&nbsp;<%=itemdesc%></td>
		<td align="left" class="main2">&nbsp;<%=uom%></td>
		
	</TR>
	
   
<%
		}
	}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");
	}
%>
       
  </tbody>
</table>
<br>
  <div class="text-center">       
        <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
	     </div>    
</div>
</form>
</body>
</html>

<script>
$(document).ready(function(){
	$('#myTable').dataTable();
   
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script>


<%-- <%
StrUtils strUtils = new StrUtils();
String sItem = strUtils.fString(request.getParameter("ITEM"));
String plant = strUtils.fString((String)session.getAttribute("PLANT"));

%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>





<Script language="JavaScript" type="text/javascript">



var urlStr = "/track/ItemMstServlet";

// Call the method of JQuery Ajax provided
$.ajax({type: "POST",url: urlStr, data: { ITEM: "<%=sItem%>" ,COND:"", ACTION: "PRODUCT_LIST",PLANT:"<%=plant%>"},dataType: "json", success: callback });



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
            '<TH align="left"><font color="white">Product ID </font></TH>'+
            '<TH align="left"><font color="white">Description</font></TH>'+
            '<TH align="left"><font color="white">UOM</font></TH>'+
            '</TR>';	
                        
                     
       
}


function SubmitValue( json){
var item =json.PRODUCT;
window.opener.form1.ITEM.value =item;
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
</html> --%>





