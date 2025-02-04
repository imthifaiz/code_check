<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<html>
<head>
<title>Product Category List</title>
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

function onNew(){
	   document.form1.action  = "classlist_save.jsp?action=Clear";
	   document.form1.submit();
	}
	function onAdd(){
	   var ITEM_ID   = document.form1.PRD_CLS_ID.value;
	   var PRD_CLS_DESC = document.form1.PRD_CLS_DESC.value;
	    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Category ID ");document.form1.PRD_CLS_ID.focus(); return false; }
	    if(PRD_CLS_DESC == "" || PRD_CLS_DESC == null) {alert("Please Enter Product Category Description");document.form1.PRD_CLS_DESC.focus(); return false; }
	   document.form1.action  = "classlist_save.jsp?action=ADD";
	   document.form1.submit();
	}


	function onGenID(){
	 
	   document.form1.action  = "classlist_save.jsp?action=Auto_ID";
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
String sItemId = "", sPrdClsId = "", sItemDesc = "",ACTION_CMD="";

session = request.getSession();
StrUtils strUtils = new StrUtils();
PrdClassUtil prdclsutil = new PrdClassUtil();
DateUtils dateutils = new DateUtils();
MLogger mLogger = new MLogger();
prdclsutil.setmLogger(mLogger);


action = strUtils.fString(request.getParameter("action"));
String plant = (String) session.getAttribute("PLANT");
sItemId = strUtils.fString(request.getParameter("PRD_CLS_ID"));
sItemDesc = strUtils.fString(request.getParameter("PRD_CLS_DESC"));
if (sItemId.length() <= 0)
	sItemId = strUtils.fString(request.getParameter("ITEM_ID1"));

//1. >> New
if (action.equalsIgnoreCase("Clear")) {
	sItemId = "";
	sItemDesc = "";
	sPrdClsId = "";
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
	ht.put(IDBConstants.TBL_FUNCTION, "PRDCLASS");
	try {
		boolean exitFlag = false;
		boolean resultflag = false;
		exitFlag = _TblControlDAO.isExisit(ht, "", plant);

		//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
		if (exitFlag == false) {
			Map htInsert = null;
			Hashtable htTblCntInsert = new Hashtable();

			htTblCntInsert.put(IDBConstants.PLANT, plant);

			htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
					"PRDCLASS");
			htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PC");
			htTblCntInsert.put(IDBConstants.TBL_MINSEQ, "000");
			htTblCntInsert.put(IDBConstants.TBL_MAXSEQ, "999");
			htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
					(String) IDBConstants.TBL_FIRST_NEX_SEQ);
			htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
			htTblCntInsert.put(IDBConstants.CREATED_AT,
					(String) new DateUtils().getDateTime());
			insertFlag = _TblControlDAO.insertTblControl(
					htTblCntInsert, plant);

			sItemId = "PC" + "001";
		} else {
			//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

			Map m = _TblControlDAO.selectRow(query, ht, "");
			sBatchSeq = (String) m.get("NXTSEQ");

			int inxtSeq = Integer.parseInt(((String) sBatchSeq
					.trim().toString())) + 1;

			String updatedSeq = Integer.toString(inxtSeq);
			if (updatedSeq.length() == 1) {
				sZero = "00";
			} else if (updatedSeq.length() == 2) {
				sZero = "0";
			}

			//System.out.print("..................................."+rtnBatch);
			Map htUpdate = null;

			Hashtable htTblCntUpdate = new Hashtable();
			htTblCntUpdate.put(IDBConstants.PLANT, plant);
			htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
					"PRDCLASS");
			htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PC");
			StringBuffer updateQyery = new StringBuffer("set ");
			updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
					+ (String) updatedSeq.toString() + "'");

			boolean updateFlag = _TblControlDAO.update(updateQyery
					.toString(), htTblCntUpdate, "", plant);

			sItemId = "PC" + sZero + updatedSeq;
		}
	} catch (Exception e) {
		mLogger.exception(true,
				"ERROR IN JSP PAGE - classlist_save.jsp ", e);
	}
}
//2. >> Add
else if (action.equalsIgnoreCase("ADD")) {

	Hashtable ht = new Hashtable();
	ht.put(IDBConstants.PLANT, sPlant);
	ht.put(IDBConstants.PRDCLSID, sItemId);
	if (!(prdclsutil.isExistsItemType(ht))) // if the Item  exists already
	{
		ht.put(IDBConstants.PLANT, sPlant);
		ht.put(IDBConstants.PRDCLSID, sItemId);
		ht.put(IDBConstants.PRDCLSDESC, strUtils.InsertQuotes(sItemDesc));
		ht.put(IConstants.ISACTIVE, "Y");
		ht.put(IDBConstants.CREATED_AT, new DateUtils()
				.getDateTime());
		ht.put(IDBConstants.LOGIN_USER, sUserId);

		MovHisDAO mdao = new MovHisDAO(sPlant);
		mdao.setmLogger(mLogger);
		Hashtable htm = new Hashtable();
		htm.put("PLANT", sPlant);
		htm.put("DIRTYPE", TransactionConstants.ADD_PRDCLS);
		htm.put("RECID", "");
		htm.put("ITEM",sItemId);
		htm.put("REMARKS",strUtils.InsertQuotes( sItemDesc));
		htm.put("UPBY", sUserId);
		htm.put("CRBY", sUserId);
		htm.put("CRAT", dateutils.getDateTime());
		htm.put("UPAT", dateutils.getDateTime());
		htm.put(IDBConstants.TRAN_DATE, dateutils
				.getDateinyyyy_mm_dd(dateutils.getDate()));

		boolean itemInserted = prdclsutil.insertPrdClsMst(ht);
		boolean inserted = mdao.insertIntoMovHis(htm);

		if (itemInserted && inserted) {
			res = "<font class = " + IDBConstants.SUCCESS_COLOR
					+ ">Product Category Added Successfully</font>";
			ACTION_CMD="Added";
					
		} else {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Failed to add New Product Category </font>";

		}
	} else {
		res = "<font class = " + IDBConstants.FAILED_COLOR
				+ ">Product Category Exists already.Try again</font>";

	}

}


%>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Product Category List</h3> 
</div>
</div>


<form class="form-horizontal" method="post" name="form1">

 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

  <div id="target" style="display:none">
     <div class="form-group">
      <label class="control-label col-sm-2" for="Product Class ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Category ID:</label>
      <div class="col-sm-3">
      	<div class="input-group">
            <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />    		
    		<input name="PRD_CLS_ID" type="TEXT" value="<%=sItemId%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();" <%=sAddEnb%>>
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="ACTION_CMD" value="<%=ACTION_CMD%>">
  		<INPUT type="hidden" name="ITEM_ID1" value="">
      </div>
	  <div class="form-inline">
      <label class="control-label col-sm-3" for="Product Class Description">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Category Description:</label>
      <div class="col-sm-3">          
        <INPUT  class="form-control" name="PRD_CLS_DESC" type="TEXT" value="<%=sItemDesc%>" style="width:100%"
			size="50" MAXLENGTH=100>
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
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Create Product Category</a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Create Product Category</a>
      </div>
       	  </div>
</div>

<div>
<table id="myTable" class="table">
<thead style="background: #eaeafa"> 
   <tr>
        <th>Product Category ID</th>
        <th>Product Category Description</th>
      </tr>
    </thead>
    <tbody>
    

    
<%

String PLANT = "", Class = "";

HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());

mLogger.setLoggerConstans(loggerDetailsHasMap);
   
    PLANT = session.getAttribute("PLANT").toString();
	
 
    String sBGColor = "";
    String classType="";
    if(request.getParameter("CLASSTYPE")!=null)
    	classType=request.getParameter("CLASSTYPE");
   try{

    arrCust = prdclsutil.getPrdTypeList("", PLANT, "");
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrCustLine = (Map)arrCust.get(i);
        String sclsCode     = (String)arrCustLine.get("prd_cls_id");
        String sclsName     = strUtils.replaceCharacters2Send1((String)arrCustLine.get("prd_cls_desc"));
        

%>
<TR>
<%if(classType.equalsIgnoreCase("PRDMODALUOM")){%>
	<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.productForm.PRD_CLS_ID.value='<%=strUtils.insertEscp(sclsName)%>';window.close();"><%=sclsCode%></a></td>
	<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sclsName)%></td>
<%} else {%>		
	<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.PRD_CLS_ID.value='<%=sclsCode%>';window.opener.form.PRD_CLS_DESC.value='<%=strUtils.insertEscp(sclsName)%>';window.close();"><%=sclsCode%></a></td>
	<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sclsName)%></td>
<%} %>
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
    	window.opener.form.PRD_CLS_ID.value=document.form1.PRD_CLS_DESC.value;
    	window.close();
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