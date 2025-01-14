<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Sales Order List</title>
<link rel="stylesheet" href="../css/style.css">

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

<script>
function onGo(){
	var status      = document.form1.CMBSSTATUS.value;
    document.form1.CHOSESTATUS.value = status;
    document.form1.submit();
}
</script>

</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Sales Order List</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<%

	  HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	  loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
	  		.getAttribute("PLANT"));
	  loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
	  		(String) session.getAttribute("LOGIN_USER")).trim());
	  MLogger mLogger = new MLogger();
	  mLogger.setLoggerConstans(loggerDetailsHasMap);
	     // ItemUtil itemUtil = new ItemUtil();
	       DOUtil _DOUtil = new DOUtil();
	      _DOUtil.setmLogger(mLogger);
	      StrUtils strUtils = new StrUtils();
	      String strStatus ="",STATUS="",CHOSESTATUS="";
	      String dono = strUtils.fString(request.getParameter("DONO"));
	      String invoiceno = strUtils.fString(request.getParameter("INVOICENO"));
	      STATUS = strUtils.fString(request.getParameter("STATUS"));
	      String pickissue = strUtils.fString(request.getParameter("PICKISSUE"));
	      String sBGColor = "",dtCondStr="",extCond="";
	      /*if(!dono.equals("")){
	    	  STATUS="1";
	      }
	      else
	      {
	    	  STATUS="";
	      }*/
	      
	      String afrmDate=StrUtils.fString(request.getParameter("FROMDATE"));
	      String atoDate=StrUtils.fString(request.getParameter("TODATE"));
	      String fdate="",tdate="";
	      

	      if (afrmDate.length() > 5)
	  		fdate = afrmDate.substring(6) + "-"+ afrmDate.substring(3, 5) +  "-"+ afrmDate.substring(0, 2);
	  	if (atoDate == null)
	  		atoDate= "";
	  	else
	  		atoDate= atoDate.trim();
	  	if (atoDate.length() > 5)
	  		 tdate = atoDate.substring(6) +  "-"+ atoDate.substring(3, 5)+  "-"+ atoDate.substring(0, 2);
	       
	     if(pickissue.equalsIgnoreCase("Y")){ 
	   	 	dtCondStr =    "  AND  ISNULL(issuedate,'')<>'' AND CAST((SUBSTRING(issuedate, 7, 4) + '-' + SUBSTRING(issuedate, 4, 2) + '-' + SUBSTRING(issuedate, 1, 2)) AS date)"; 
	     }
	     else
	     {
	    	 dtCondStr =    "  AND  ISNULL(CollectionDate,'')<>'' AND CAST((SUBSTRING(CollectionDate, 7, 4) + '-' + SUBSTRING(CollectionDate, 4, 2) + '-' + SUBSTRING(CollectionDate, 1, 2)) AS date)"; 
 
	     }
	       if (fdate.length() > 0) {
	  		  extCond = extCond + dtCondStr + "  >= '" 
	  				+ fdate
	  				+ "'  ";
	  		if (atoDate.length() > 0) {
	  			extCond = extCond +dtCondStr+ " <= '" 
	  			+  tdate
	  			+ "'  ";
	  		}
	  	} else {
	  		if (tdate.length() > 0) {
	  			extCond = extCond +dtCondStr+ " <= '" 
	  			+ tdate
	  			+ "'  ";
	  		}
	  	}   
	      	      
	     Hashtable htStatus=new Hashtable();
	     session = request.getSession();
	     String plant=(String)session.getAttribute("PLANT");
	     
	     htStatus.put("PLANT",plant);
	     if(dono.length()>0) extCond=" AND plant='"+plant+"'";
	     extCond=extCond+" and  plant <> ''";
%>
<!--  
<table border="0" width="100%" cellspacing="1" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<tr>            <td width="26%"> </td> 
					<TH ALIGN="rigt" >&nbsp;Status : </TH>
					 <TD><select id = "CMBSTATUS" name="CMBSSTATUS" size='1'>
          				<option selected value="">Choose</option>
          				<option VALUE="N">N</option>
          				<option VALUE="O">O</option>
        				<option VALUE="C">C</option>
          		 </select></TD>
          		   <INPUT type="hidden" name="CHOSESTATUS" id="CHOSESTATUS" value="<%=CHOSESTATUS%>">
          		   <TD ALIGN="left"><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
          		    <td width="35%"> </td> 
         </TR>
 </table>-->
 <div>
<table id="myTable" class="table">
 <thead style="background: #eaeafa"> 
        <tr>
          <th>Order No</th>
          <th>Customer Name</th>
      </tr>
    </thead>
    <tbody>
	<%

   try{
     Hashtable ht=new Hashtable();
     session = request.getSession();
     /*CHOSESTATUS =  strUtils.fString(request.getParameter("CHOSESTATUS")); 
   
     ht.put("PLANT",plant);
     if(dono.length()>0) extCondData=" AND plant='"+plant+"' and dono like '"+dono+"%'";
     
     if(pickissue.equalsIgnoreCase("Y")){
    	 extCondData=extCondData+" AND status <>'N' ";
     }
     
     if(!dono.equals("") &&  !STATUS.equals("")){
    	   extCondData=extCondData+" and plant <>''"; 
     }
     else  if(STATUS.equals("")&& dono.equals("") && CHOSESTATUS.equals("")){
  	   extCondData=extCondData+" and status <>'C'"; 
     }
     
      else 
     {
    	 extCondData=extCondData+" and status = '" + CHOSESTATUS + "'"; 
     }
     extCondData=extCondData+" order by dono desc";
     */
     ht.put("PLANT",plant);
     ArrayList listQry= new ArrayList();
     if(pickissue.equalsIgnoreCase("Y")){
     		extCond=extCond  + " and plant='"+plant+"' and dono like '"+dono+"%' and invoiceno like '" + invoiceno + "%' and dono in(select dono from [" + plant + "_" + "DOHDR" + "] where status like '"+STATUS+"%' ) ";
     }
     else{
    	 extCond=extCond  + " and plant='"+plant+"' and dono like '"+dono+"%' and invoiceno like '" + invoiceno + "%' and status like '"+STATUS+"%' ";
     }
     
     extCond=extCond+" order by dono desc";
     
     if(pickissue.equalsIgnoreCase("Y")){
      		listQry = _DOUtil.getOBIssueList(" distinct dono,cname as custName",ht,extCond);
     }
     else{
      		listQry = _DOUtil.getDoHdrDetails("dono,custName",ht,extCond);
     }
     for(int i =0; i<listQry.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map m=(Map)listQry.get(i);
        dono     = (String)m.get("dono");
        String custName    = (String)m.get("custName");
        
    %>
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.DONO.value='<%=dono%>';
                     window.close();"><%=dono%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(custName)%></td>
		
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
