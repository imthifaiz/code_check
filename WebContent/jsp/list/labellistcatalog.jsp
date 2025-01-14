<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%><html>
<head>
<title>Item List</title>
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

<script type="text/javascript">
function setCheckedValue(radioObj, newValue) {
	if (!radioObj)
		return;
	var radioLength = radioObj.length;
	if (radioLength == undefined) {
		radioObj.checked = (radioObj.value == newValue.toString());
		return;
	}
	for ( var i = 0; i < radioLength; i++) {
		radioObj[i].checked = false;
		if (radioObj[i].value == newValue.toString()) {
			radioObj[i].checked = true;
		}
	}
}
</script>

</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">ITEM LIST</h3> 
</div>
</div>

<body>
<form method="post" name="form">
<div>
<table id="myTable" class="table">
<thead style="background: #eaeafa"> 
        <tr>
          <th>Product ID</th>
          <th>Description</th>
      </tr>
    </thead>
    <tbody>

<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);

   CatalogUtil _catlogutil = new CatalogUtil();
    StrUtils strUtils = new StrUtils();
    
    _catlogutil.setmLogger(mLogger);
    String extraCond ="",sDesc="";
    String sItem = strUtils.fString(request.getParameter("ITEM"));
    String sDescr = strUtils.fString(request.getParameter("DESCR"));
    
    String plant = strUtils.fString((String)session.getAttribute("PLANT"));
    String sBGColor = "",sDesc2="",sDesc3="",price="",catlogpath="",isactive="";
   try{
	   Hashtable ht = new Hashtable();
	   ht.put(IDBConstants.PLANT,plant);
	   if(sDescr.length()>0){
		   sDescr = new StrUtils().InsertQuotes(sDescr);
		   sDescr= " AND DESCRIPTION1 like '%"+sDescr+"%'";
           }
       
    List listQry = _catlogutil.listCatalogs("distinct CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISACTIVE ",ht," AND PRODUCTID like '"+sItem+"%' "+sDescr+" ORDER BY PRODUCTID ");
    for(int i =0; i<listQry.size(); i++) {
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map itemMap   = (Map)listQry.get(i);
    String sItem1     = (String)itemMap.get(IDBConstants.PRODUCTID);
    sDesc      = strUtils.replaceCharacters2Send1((String)itemMap.get(IDBConstants.DESCRIPTION1));
    sDesc2  = (String)itemMap.get(IDBConstants.DESCRIPTION2);//artist
    sDesc3  = (String)itemMap.get(IDBConstants.DESCRIPTION3);
    price = StrUtils.currencyWtoutCommSymbol((String)itemMap.get(IDBConstants.CATLOGPRICE));
    String sDesc1 = strUtils.insertEscp(sDesc);
    catlogpath = (String)itemMap.get(IDBConstants.CATLOGPATH);
    isactive = (String)itemMap.get(IConstants.ISACTIVE);
    //System.out.println("catlogpath"+catlogpath);    
      catlogpath = catlogpath.replace('/','\\');
%>
  <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			 onClick="window.opener.form.PRODUCTID.value='<%=sItem1%>';window.opener.form.DESCR.value='<%=sDesc1%>';
                      window.opener.form.PRICE.value='<%=price%>';setCheckedValue( window.opener.form.ISACTIVE,'<%=isactive%>');window.close();"><%=sItem1%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sDesc)%></td>

	</TR>
    
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
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