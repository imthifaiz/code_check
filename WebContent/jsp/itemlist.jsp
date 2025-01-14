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
       <TH >Brand</TH>
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
String type= StrUtils.fString(request.getParameter("TYPE"));
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
        //String uom   =  strUtils.fString((String)m.get("STKUOM"));
        //String isactive   =  strUtils.fString((String)m.get("ISACTIVE"));
        //String productclass=strUtils.fString((String)m.get("PRD_CLS_ID"));
        String productbrand=strUtils.fString((String)m.get("BRAND"));
        String vinno =strUtils.fString((String)m.get("VINNO"));
        String model =strUtils.fString((String)m.get("MODEL"));
        if(type.equalsIgnoreCase("MAINITEM")){
%>

<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.ITEM.value='<%=item%>';window.opener.form.DESC.value='<%=itemdesc%>';window.opener.form.PRD_BRAND_ID.value='<%=productbrand%>';window.opener.form.PRD_BRAND_ID.value='<%=productbrand%>';window.opener.form.VINNO.value='<%=vinno%>';window.opener.form.MODEL.value='<%=model%>';window.close();"><%=item%></a></td>
			<td align="left" class="main2">&nbsp;<%=itemdesc%></td>
		<td align="left" class="main2">&nbsp;<%=productbrand%></td>

	</TR>
	
   
<%   }else{%>
		<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
		onClick="window.opener.form.ALTERNATEBRANDITEM.value='<%=item%>';window.opener.form.ALTERNATEBRANDDESC.value='<%=itemdesc%>';window.opener.form.ALTERNATE_PRD_BRAND_ID.value='<%=productbrand%>';window.opener.form.ALTERNATE_PRD_BRAND_ID.value='<%=productbrand%>';window.opener.form.ALTERNATEVINNO.value='<%=vinno%>';window.opener.form.ALTERNATEMODEL.value='<%=model%>';window.close();"><%=item%></a></td>
			<td align="left" class="main2">&nbsp;<%=itemdesc%></td>
		<td align="left" class="main2">&nbsp;<%=productbrand%></td>

	</TR>
      <%} // end type check
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
    /* $('#myModal1').click(function(){
    	if(document.getElementById("alertValue").value!="")
    	{
    		//$("#myModal").modal();
    		document.getElementById('myModal').style.display = "block";
    	}
    }); */
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script>