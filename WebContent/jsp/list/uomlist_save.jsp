<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<html>
<head>
<title>UOM List</title>
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
<!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="../dist/css/bootstrap.min.css"/>
  <!-- DataTables -->
  <link rel="stylesheet" href="../dist/css/lib/datatables.min.css"/>
  <!-- jQuery 3 -->
  <script type="text/javascript" src="../dist/js/jquery.min.js"></script>
  <script type="text/javascript" src="../dist/js/jquery.dataTables.min.js"></script>
	<!-- jQuery UI -->
	<script src="../dist/js/jquery-ui-1.12.1.js"></script>
</head>
<body>
<script type="text/javascript" src="../js/general.js"></script>
<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "uomlist_save.jsp?action=Clear";
   document.form1.submit();
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
   var Display   = document.form1.Display.value;
   var QPUOM   = document.form1.QPUOM.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter UOM ");document.form1.ITEM_ID.focus(); return false; }
	
    else if(Display == "" || Display == null) 
    {
    	alert("Please Enter Display for UOM");
    	document.form1.Display.focus();
    	return false; 
    	}
    else if(!IsNumeric(document.form1.QPUOM.value))
    {
      alert("Please Enter valid Quantity Per UOM");
      form1.QPUOM.focus(); form1.QPUOM.select();
      return false;
    }
    else if(QPUOM == "" || QPUOM <= 0)
    {
        alert("Please Enter Quantity Per UOM");
        document.form1.QPUOM.focus();
        return false;
      }
   document.form1.action  = "uomlist_save.jsp?action=ADD&UOMTYPE="+document.form1.UOMTYPE.value;
   document.form1.submit();
}


function onGenID(){
     
   document.form1.action  = "uomlist_save.jsp?action=Auto_ID";
   document.form1.submit();
}

</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res = "";
ArrayList arrCust = new ArrayList();
String sNewEnb = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb = "disabled";
String sUpdateEnb = "enabled";
sAddEnb = "enabled";
String action = "";
String sItemId = "", sPrdClsId = "", sItemDesc = "",sDisplay = "",sQtyPerUom = "",ACTION_CMD="";

session = request.getSession();
StrUtils strUtils = new StrUtils();
UomUtil uomUtil = new UomUtil();
DateUtils dateutils = new DateUtils();
MLogger mLogger = new MLogger();
uomUtil.setmLogger(mLogger);

action = strUtils.fString(request.getParameter("action"));
String plant = (String) session.getAttribute("PLANT");
sItemId = strUtils.fString(request.getParameter("ITEM_ID"));
sItemDesc = strUtils.fString(request.getParameter("ITEM_DESC"));
sDisplay = strUtils.fString(request.getParameter("Display"));
sQtyPerUom = strUtils.fString(request.getParameter("QPUOM"));

String uomtype="";
if(request.getParameter("UOMTYPE")!=null)
uomtype=request.getParameter("UOMTYPE");


if (sItemId.length() <= 0)
	sItemId = strUtils.fString(request.getParameter("ITEM_ID1"));

//1. >> New
if (action.equalsIgnoreCase("Clear")) {
	sItemId = "";
	sItemDesc = "";
	sPrdClsId = "";
	sDisplay ="";
	sQtyPerUom ="";

} else if (action.equalsIgnoreCase("Auto_ID")) {

	String minseq = "";
	String sBatchSeq = "";
	boolean insertFlag = false;
	String sZero = "";
	TblControlDAO _TblControlDAO = new TblControlDAO();
	_TblControlDAO.setmLogger(mLogger);
	Hashtable ht = new Hashtable();

	String query = " isnull(NXTSEQ,'') as NXTSEQ";
	ht.put(IDBConstants.PLANT, plant);
	ht.put(IDBConstants.TBL_FUNCTION, "UOM");
	try {
		boolean exitFlag = false;
		boolean resultflag = false;
		exitFlag = _TblControlDAO.isExisit(ht, "", plant);

		//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
		if (exitFlag == false) {

			Map htInsert = null;
			Hashtable htTblCntInsert = new Hashtable();

			htTblCntInsert.put(IDBConstants.PLANT, plant);

			htTblCntInsert.put(IDBConstants.TBL_FUNCTION, "UOM");
			htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "R");
			htTblCntInsert.put(IDBConstants.TBL_MINSEQ, "0000");
			htTblCntInsert.put(IDBConstants.TBL_MAXSEQ, "9999");
			htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
					(String) IDBConstants.TBL_FIRST_NEX_SEQ);
			htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
			htTblCntInsert.put(IDBConstants.CREATED_AT,
					(String) new DateUtils().getDateTime());
			insertFlag = _TblControlDAO.insertTblControl(
					htTblCntInsert, plant);

			sItemId = "U" + "0001";
		} else {
			//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

			Map m = _TblControlDAO.selectRow(query, ht, "");
			sBatchSeq = (String) m.get("NXTSEQ");

			int inxtSeq = Integer.parseInt(((String) sBatchSeq
					.trim().toString())) + 1;

			String updatedSeq = Integer.toString(inxtSeq);
			if (updatedSeq.length() == 1) {
				sZero = "000";
			} else if (updatedSeq.length() == 2) {
				sZero = "00";
			} else if (updatedSeq.length() == 3) {
				sZero = "0";
			}

			//System.out.print("..................................."+rtnBatch);
			Map htUpdate = null;

			Hashtable htTblCntUpdate = new Hashtable();
			htTblCntUpdate.put(IDBConstants.PLANT, plant);
			htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, "UOM");
			htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "U");
			StringBuffer updateQyery = new StringBuffer("set ");
			updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
					+ (String) updatedSeq.toString() + "'");

			boolean updateFlag = _TblControlDAO.update(updateQyery
					.toString(), htTblCntUpdate, "", plant);

			sItemId = "R" + sZero + updatedSeq;
		}
	} catch (Exception e) {
		mLogger.exception(true,
				"ERROR IN JSP PAGE - uomlist_save.jsp ", e);
	}
}
//2. >> Add
else if (action.equalsIgnoreCase("ADD")) {

	Hashtable ht = new Hashtable();
	ht.put(IDBConstants.PLANT, sPlant);
	ht.put(IDBConstants.UOMCODE, sItemId);
	if (!(uomUtil.isExistsUom(ht))) // if the Item  exists already
	{
		ht.put(IDBConstants.PLANT, sPlant);
		ht.put(IDBConstants.UOMCODE, sItemId);
		ht.put(IDBConstants.UOMDESC, sItemDesc);
		ht.put(IConstants.ISACTIVE, "Y");
		ht.put(IDBConstants.CREATED_AT, new DateUtils()
				.getDateTime());
		ht.put(IDBConstants.LOGIN_USER, sUserId);

		MovHisDAO mdao = new MovHisDAO(sPlant);
		mdao.setmLogger(mLogger);
		Hashtable htm = new Hashtable();
		htm.put(IDBConstants.PLANT, sPlant);
		htm.put(IDBConstants.DIRTYPE, TransactionConstants.ADD_UOM);
		htm.put("RECID", "");
		htm.put("ITEM",sItemId);
		htm.put(IDBConstants.UPBY, sUserId);
		htm.put(IDBConstants.REMARKS, sItemDesc);
		htm.put(IDBConstants.CREATED_BY, sUserId);
		htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
		htm.put(IDBConstants.TRAN_DATE, dateutils
				.getDateinyyyy_mm_dd(dateutils.getDate()));
		ht.put("Display",sDisplay);
		/* if(sQtyPerUom == "")
		{
			sQtyPerUom = "0";
		} */
		ht.put("QPUOM",sQtyPerUom);

		boolean itemInserted = uomUtil.insertUom(ht);
		boolean inserted = mdao.insertIntoMovHis(htm);
		if (itemInserted && inserted) {
			res = "<font class = " + IDBConstants.SUCCESS_COLOR
					+ ">UOM  Added Successfully</font>";
			ACTION_CMD="Added";
		} else {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Failed to add New UOM </font>";

		}
	} else {
		res = "<font class = " + IDBConstants.FAILED_COLOR
				+ ">UOM  Exists already. Try again</font>";

	}

}


%>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">UOM</h3> 
</div>
</div>


<form class="form-horizontal" method="post" name="form1">

 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

  <div id="target" style="display:none">
     <div class="form-group">
      <label class="control-label col-sm-2" for="UOM Master">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Unit Of Measure:</label>
      <div class="col-sm-3">
      <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
      <input name="ITEM_ID" type="TEXT" value="<%=sItemId%>"
			size="50" MAXLENGTH=14 class="form-control">
   				<INPUT type="hidden" name="ACTION_CMD" value="<%=ACTION_CMD%>">
   				<INPUT type="hidden" name="UOMTYPE" value="<%=uomtype%>">
  		  		<INPUT type="hidden" name="ITEM_ID1" value="">
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-3" for="UOM Description">Unit Of Measure Description:</label>
      <div class="col-sm-3">          
        <INPUT  class="form-control" name="ITEM_DESC" type="TEXT" value="<%=sItemDesc%>" style="width:100%"
			size="50" MAXLENGTH=50>
      </div>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Display">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Display:</label>
      <div class="col-sm-3">          
        <INPUT  class="form-control" name="Display" type="TEXT" value="<%=sDisplay%>"
			size="50" MAXLENGTH=14>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-3" for="Qty Per UOM">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Quantity Per UOM:</label>
      <div class="col-sm-3">          
        <INPUT  class="form-control" name="QPUOM" type="TEXT" value="<%=sQtyPerUom%>" style="width:100%"
			size="50" MAXLENGTH=50>
      </div>
    </div>
    </div>
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>
      </div>
    </div>
</div>

<div class="form-group">
      <div class="col-sm-4">
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Create UOM</a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Create UOM</a>
      </div>
       	  </div>
</div>
<div>
<table id="myTable" class="table">
<thead style="background: #eaeafa"> 
   <tr>
        <th>Unit Of Measure</th>
        <th>UOM Description</th>
		<th>Display</th>
        <th>Qty Per UOM</th>
      </tr>
    </thead>
    <tbody>
    

    
<%

String PLANT = "", UOM = "";

HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());

mLogger.setLoggerConstans(loggerDetailsHasMap);
   
    PLANT = session.getAttribute("PLANT").toString();
	//UOM = strUtils.fString(request.getParameter("ITEM_ID"));
 
    String sBGColor = "";
	
	
   try{

    arrCust = uomUtil.getUomDetails("", PLANT, "");
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrCustLine = (Map)arrCust.get(i);
        String sUOMCode     = (String)arrCustLine.get("uom");
        String sUOMName     = strUtils.replaceCharacters2Send1((String)arrCustLine.get("uomdesc"));
		String sUOMDisplay             = strUtils.replaceCharacters2Send1((String)arrCustLine.get("Display"));
        String sUOMQtyPerUom             = strUtils.replaceCharacters2Send1((String)arrCustLine.get("QPUOM"));
        

%>
<TR>
	<%if(uomtype.equalsIgnoreCase("PURCHASEUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.PURCHASEUOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();"><%=sUOMCode%></a></td>
			
  <%} else if(uomtype.equalsIgnoreCase("SALESUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.SALESUOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();"><%=sUOMCode%></a></td>
			
<%}else if(uomtype.equalsIgnoreCase("RENTALUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.RENTALUOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();"><%=sUOMCode%></a></td>
			
<%}else if(uomtype.equalsIgnoreCase("SERVICEUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.SERVICEUOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();"><%=sUOMCode%></a></td>
			
<%}else if(uomtype.equalsIgnoreCase("INVENTORYUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.INVENTORYUOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();"><%=sUOMCode%></a></td>
			
<%}else if(uomtype.equalsIgnoreCase("PRDMODALUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;
			<a href="#" onClick=" window.opener.productForm.UOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();">
				<%=sUOMCode%>
			</a>
		</td>			
<%} else if(uomtype.equalsIgnoreCase("PRDMODALPURCHASEUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;
			<a href="#" onClick=" window.opener.productForm.PURCHASEUOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();">
				<%=sUOMCode%>
			</a>
		</td>
<%} else if(uomtype.equalsIgnoreCase("PRDMODALSALESUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;
		<a href="#" onClick=" window.opener.productForm.SALESUOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();">
			<%=sUOMCode%>
		</a>
		</td>
<%} else if(uomtype.equalsIgnoreCase("PRDMODALRENTALUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;
			<a href="#" onClick=" window.opener.productForm.RENTALUOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();">
				<%=sUOMCode%>
			</a>
		</td>
<%} else if(uomtype.equalsIgnoreCase("PRDMODALINVENTORYUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;
			<a href="#" onClick=" window.opener.productForm.INVENTORYUOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();">
				<%=sUOMCode%>
			</a>
		</td>
<%} else {%>
<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
onClick=" window.opener.form.UOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();"><%=sUOMCode%></a></td>
<%} %>

		<%-- <td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.UOM.value='<%=strUtils.insertEscp(sUOMCode)%>';window.close();"><%=sUOMCode%></a></td> --%>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sUOMName)%></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sUOMDisplay)%></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sUOMQtyPerUom)%></td>

	</TR>
    
   
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>


</tbody>
</table>
<br>
      
</div>
</form>
</body>
</html>

<script>
$(document).ready(function(){
	$('#myTable').dataTable();
    
    $('[data-toggle="tooltip"]').tooltip(); 

    if(document.form1.ACTION_CMD.value!="")
    {
    	var suomtype=document.form1.UOMTYPE.value;
    	if(suomtype=="PURCHASEUOM")
    	{
    		window.opener.form.PURCHASEUOM.value=document.form1.ITEM_ID.value;
        	window.close();	
    	} else if(suomtype=="SALESUOM") {
    		window.opener.form.SALESUOM.value=document.form1.ITEM_ID.value;
        	window.close();
    	} else if(suomtype=="RENTALUOM") {
    		window.opener.form.RENTALUOM.value=document.form1.ITEM_ID.value;
        	window.close();
    	} else if(suomtype=="SERVICEUOM") {
    		window.opener.form.SERVICEUOM.value=document.form1.ITEM_ID.value;
        	window.close();
    	} else if(suomtype=="INVENTORYUOM") {
    		window.opener.form.INVENTORYUOM.value=document.form1.ITEM_ID.value;
        	window.close();	
    	} else {
    		window.opener.form.UOM.value=document.form1.ITEM_ID.value;
    		window.close();
    	}
    }
//Below Jquery Script used for Show/Hide Function

$('#Show').click(function() {
    $('#target').show(500);
    $('#Show').hide(0);
    $('#Hide').show(0);
    $('#search_criteria_status').val('show');
});

$('#Hide').click(function() {
    $('#target').hide(500);
    $('#Show').show(0);
    $('#Hide').hide(0);
    $('#search_criteria_status').val('hide');
});
if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
	$('#Show').click();
}else{
	$('#Hide').click();
}	
});
</script>