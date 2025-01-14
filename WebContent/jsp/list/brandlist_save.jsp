<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<html>
<head>
<title>Product Brand List</title>
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
	   document.form1.action  = "brandlist_save.jsp?action=Clear";
	   document.form1.submit();
	}
	function onAdd(){
	   var ITEM_ID   = document.form1.ITEM_ID.value;
	   var ITEM_DESC = document.form1.ITEM_DESC.value;
	    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Brand ID");document.form1.ITEM_ID.focus(); return false; }
	    if(ITEM_DESC == "" || ITEM_DESC == null) {alert("Please Enter Product Brand Description");document.form1.ITEM_DESC.focus(); return false; }
	   document.form1.action  = "brandlist_save.jsp?action=ADD";
	   document.form1.submit();
	}


	function onGenID(){
	     
	   document.form1.action  = "brandlist_save.jsp?action=Auto_ID";
	   document.form1.submit();
	}


function onClear(){
	document.form1.ITEM_ID.value = "";
	document.form1.ITEM_DESC.value = "";
}
</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");

String sNewEnb = "enabled";
String sAddEnb = "disabled";
sAddEnb = "enabled";
String action = "";
String sItemId = "", sPrdClsId = "", sItemDesc = "",ACTION_CMD="";

MLogger mLogger = new MLogger();
ArrayList arrCust = new ArrayList();
StrUtils strUtils = new StrUtils();
PrdBrandUtil prdbrandutil = new PrdBrandUtil();
String responseMsg = request.getParameter("response");
String productBrandID = request.getParameter("brandID");
String productBrandDesc = request.getParameter("brandDesc");
String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER"));
action    = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sItemId  = strUtils.fString(request.getParameter("ITEM_ID"));
sItemDesc  = strUtils.fString(request.getParameter("ITEM_DESC"));
if(sItemId.length() <= 0) sItemId  = strUtils.fString(request.getParameter("ITEM_ID1"));

//1. >> New
if(action.equalsIgnoreCase("Clear")){
    sItemId  = "";
    sItemDesc  = "";
    sPrdClsId ="";
}
else if(action.equalsIgnoreCase("Auto_ID"))
{

String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
TblControlDAO _TblControlDAO =new TblControlDAO();
_TblControlDAO.setmLogger(mLogger);
    Hashtable  ht=new Hashtable();
   
    String query=" isnull(NXTSEQ,'') as NXTSEQ";
    ht.put(IDBConstants.PLANT,plant);
    ht.put(IDBConstants.TBL_FUNCTION,"PRDBRAND");
      try{
     boolean exitFlag=false; boolean resultflag=false;
    exitFlag=_TblControlDAO.isExisit(ht,"",plant);
   
   //--if exitflag is false than we insert batch number on first time based on plant,currentmonth
   if (exitFlag==false)
    { 
                  
          Map htInsert=null;
          Hashtable htTblCntInsert  = new Hashtable();
         
          htTblCntInsert.put(IDBConstants.PLANT,plant);
        
          htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDBRAND");
          htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"PB");
           htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"000");
           htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"999");
          htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
          htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
          htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
          insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
          
      sItemId="PB"+"001";
    }
    else
    {
         //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
       
         Map m= _TblControlDAO.selectRow(query, ht,"");
        sBatchSeq=(String)m.get("NXTSEQ");
                  
         int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
        
         String updatedSeq=Integer.toString(inxtSeq);
          if(updatedSeq.length()==1)
         {
           sZero="00";
         }
         else if(updatedSeq.length()==2)
         {
           sZero="0";
         }
        
       
         Map htUpdate = null;
        
         Hashtable htTblCntUpdate = new Hashtable();
         htTblCntUpdate.put(IDBConstants.PLANT,plant);
         htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PRDBRAND");
         htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"PB");
         StringBuffer updateQyery=new StringBuffer("set ");
         updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
       
       
      boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
      
      
            sItemId="PB"+sZero+updatedSeq;
         }
         } catch(Exception e)
          {
      	   mLogger.exception(true,
 					"ERROR IN JSP PAGE - typelist_save.jsp ", e);
          }
}
//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
	

 Hashtable ht = new Hashtable(); 
	ht.put(IDBConstants.PLANT, plant);
	ht.put(IDBConstants.PRDBRANDID, sItemId);
	if (!(prdbrandutil.isExistsItemType(ht))) // if the Item exists already
	{
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.PRDBRANDID, sItemId);
		ht.put(IDBConstants.PRDBRANDDESC, sItemDesc);
		ht.put(IConstants.ISACTIVE, "Y");
		new DateUtils();
		ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		ht.put(IDBConstants.LOGIN_USER, userName);

		MovHisDAO mdao = new MovHisDAO(plant);
		mdao.setmLogger(mLogger);
		Hashtable<String, String> htm = new Hashtable<String, String>();
		htm.put(IDBConstants.PLANT, plant);
		htm.put("DIRTYPE", TransactionConstants.ADD_PRDBRAND);
		htm.put("RECID", "");
		htm.put("ITEM",sItemId);
		htm.put("REMARKS", sItemDesc);
		htm.put("UPBY", userName);
		htm.put("CRBY", userName);
		htm.put("CRAT", DateUtils.getDateTime());
		htm.put("UPAT", DateUtils.getDateTime());
		htm.put(IDBConstants.TRAN_DATE, DateUtils
				.getDateinyyyy_mm_dd(DateUtils.getDate()));

		boolean itemInserted = prdbrandutil.insertPrdBrandMst(ht);
		boolean inserted = mdao.insertIntoMovHis(htm);
		
		 if(itemInserted&&inserted) {
			 responseMsg = "<font class = "+IDBConstants.SUCCESS_COLOR +">Product Brand Added Successfully</font>";
			 ACTION_CMD="Added";
          
   } else {
	   responseMsg = "<font class = "+IDBConstants.FAILED_COLOR +">Failed to add New Product Brand </font>";
           
   }
	} else {
		responseMsg = "<font class = " + IDBConstants.FAILED_COLOR+ ">Product Brand  Exists already. Try again</font>";

}
}


if(responseMsg == null)
	responseMsg = "";




%>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Product Brand List</h3> 
</div>
</div>


<form class="form-horizontal" method="post" name="form1">

 <div class="box-body">
 
   <CENTER><strong><%=responseMsg%></strong></CENTER>

  <div id="target" style="display:none">
   <div class="form-group">
      <label class="control-label col-sm-2" for="Product Brand ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Brand ID:</label>
      <div class="col-sm-3">
      	<div class="input-group">
            <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />    		
    		<input name="ITEM_ID" type="TEXT" value="<%=sItemId%>"
			size="50" MAXLENGTH=100 class="form-control">
			<INPUT type="hidden" name="ACTION_CMD" value="<%=ACTION_CMD%>">
   		 	<span class="input-group-addon"  onClick="onGenID();" <%=sAddEnb%>>
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
      </div>
	  <div class="form-inline">
      <label class="control-label col-sm-3" for="Product Brand Description">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Brand Description:</label>
      <div class="col-sm-3">          
        <INPUT  class="form-control" name="ITEM_DESC" type="TEXT" value="<%=sItemDesc%>" style="width:100%"
			size="50" MAXLENGTH=100>
      </div> 
    </div>
    </div>
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">      
      	<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>
      </div>
    </div>
	
	</div>
	
	<div class="form-group">
      <div class="col-sm-4">
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Create Product Brand</a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Create Product Brand</a>
      </div>
       	  </div>
    
</div>

<div>
<table id="myTable" class="table">
<thead style="background: #eaeafa"> 
   <tr>
        <th>Product Type ID</th>
        <th>Product Type Description</th>
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
    String brandType="";
    if(request.getParameter("BRANDTYPE")!=null)
    	brandType=request.getParameter("BRANDTYPE");
   try{

    arrCust = prdbrandutil.getBrandList("", PLANT, "");
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrCustLine = (Map)arrCust.get(i);
        String sbraCode     = (String)arrCustLine.get("PRD_BRAND_ID");
        String sbraName     = strUtils.replaceCharacters2Send1((String)arrCustLine.get("PRD_BRAND_DESC"));
        

%>
<TR>
<%if(brandType.equalsIgnoreCase("PRDMODALUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.productForm.PRD_BRAND.value='<%=strUtils.insertEscp(sbraName)%>';window.close();"><%=sbraCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sbraName)%></td>
<%} else {%>			
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.PRD_BRAND.value='<%=strUtils.insertEscp(sbraCode)%>';window.opener.form.PRD_BRAND_DESC.value='<%=strUtils.insertEscp(sbraName)%>';window.close();"><%=sbraCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sbraName)%></td>
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
    	window.opener.form.PRD_BRAND.value=document.form1.ITEM_DESC.value;
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