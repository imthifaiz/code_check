<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<title>Loc List</title>
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

<SCRIPT LANGUAGE="JavaScript">
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
	//added by deen
	function displayAddressSection(caller){
		var openerLoc = caller.location;
	  	if(openerLoc !="undefined" && openerLoc!=null && openerLoc.toString().indexOf("maint_loc.jsp")!=-1){
               	caller.DisplayAddress();
			}

    }
    //end
</Script>
<link rel="stylesheet" href="css/style.css">
</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">LOCATION LIST</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div>
<table id="myTable" class="table" >
<thead style="background: #eaeafa">
        <tr>
         <th>Loc Id</th>
         <th>Loc Desc</th>
         <th>Loc Type1</th>
         <th>Loc Type2</th>
        <th>IsActive</th>
      </tr>
    </thead>
    <tbody>

	<%

HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
    String plant= (String)session.getAttribute("PLANT");
   
    StrUtils strUtils = new StrUtils();
    String sLoc = strUtils.fString(request.getParameter("LOC_ID"));
    String sLoctype = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
    String sBGColor = "",extcond="";
    Hashtable ht = new Hashtable();
    try
    {
    	ArrayList arrCust=new ArrayList();
    	LocUtil _LocUtil = new LocUtil();
    	_LocUtil.setmLogger(mLogger);
    	
    		ht.put("LOC_TYPE_ID",sLoctype);
    	
    	if(sLoctype.length()>0){
    	extcond = "AND LOC_TYPE_ID='"+sLoctype+"'";}
   		if(sLoc=="" || sLoc == null){
    	 arrCust = _LocUtil.getAllLocDetails(plant,extcond,"");
   		}else{
     	 arrCust = _LocUtil.getLocDetails(sLoc,plant," ",ht);
   		}
  
    	//ArrayList arrCust = _LocUtil.getAllLocDetails(plant,"","");
    	
    	for(int i =0; i<arrCust.size(); i++) {
        	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        	Map arrCustLine = (Map)arrCust.get(i);
        	String sCustCode  = strUtils.fString((String)arrCustLine.get("LOC"));
        	String sCustName1 = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOCDESC")));
        	String sRemark   = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("USERFLD1")));
            String isactive  = strUtils.fString((String)arrCustLine.get("ISACTIVE"));
            //below lines added by deen
            String scomname =  strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("COMNAME")));
            String srcbno =  strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("RCBNO")));
            String sAddr1    =  strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD1")));
            String sAddr2    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD2")));
            String sAddr3    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD3")));
            String sAddr4    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD4")));
            String sState  = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("STATE")));
            String sCountry  = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("COUNTRY")));
            String sZip      = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ZIP")));
            String sTelNo    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("TELNO")));
            String sFax      = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("FAX")));
            String schkstatus = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("CHKSTATUS")));
            String sloc_type_id = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID")));
            String sloc_type_id2 = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID2")));
            //end
%>

	<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.LOC_ID.value='<%=sCustCode%>';
                     window.opener.form1.LOC_ID1.value='<%=sCustCode%>';
                     window.opener.form1.LOC_DESC.value='<%=sCustName1%>';
                     window.opener.form1.REMARKS.value='<%=sRemark%>';
                     setCheckedValue( window.opener.form1.CHK_ADDRESS,'<%=schkstatus%>');
                     window.opener.form1.COMNAME.value='<%=scomname%>';
                     window.opener.form1.RCBNO.value='<%=srcbno%>';
                     window.opener.form1.ADDR1.value='<%=sAddr1%>';
                     window.opener.form1.ADDR2.value='<%=sAddr2%>';
                     window.opener.form1.ADDR3.value='<%=sAddr3%>';
                     window.opener.form1.ADDR4.value='<%=sAddr4%>';
                     window.opener.form1.STATE.value='<%=sState%>';
                     window.opener.form1.COUNTRY.value='<%=sCountry%>';
                     window.opener.form1.ZIP.value='<%=sZip%>';
                     window.opener.form1.TELNO.value='<%=sTelNo%>';
                     window.opener.form1.FAX.value='<%=sFax%>';
                     window.opener.form1.LOC_TYPE_ID.value='<%=sloc_type_id%>';
                     
                     setCheckedValue( window.opener.form1.ACTIVE,'<%=isactive%>');
                     displayAddressSection(window.opener);
                     window.close();"><%=sCustCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
		<td align="left" class="main2">&nbsp;<%=sloc_type_id%></td>
		<td align="left" class="main2">&nbsp;<%=sloc_type_id2%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

	</TR>
    
	<%
		}
	}catch(Exception he){he.printStackTrace(); 
		System.out.println("Error in reterieving data");
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