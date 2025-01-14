<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<html>
<head>
<title>Customer Type List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
<link rel="stylesheet" href="css/style.css">

<!-- <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
<!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="../jsp/dist/css/bootstrap.min.css"/>
  <!-- DataTables -->
  <link rel="stylesheet" href="../jsp/dist/css/lib/datatables.min.css"/>
  <!-- jQuery 3 -->
  <script type="text/javascript" src="../jsp/dist/js/jquery.min.js"></script>
  <script type="text/javascript" src="../jsp/dist/js/jquery.dataTables.min.js"></script>
	<!-- jQuery UI -->
	<script src="../jsp/dist/js/jquery-ui-1.12.1.js"></script>
</head>
<body>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">

function setCheckedValue(radioObj, newValue) {
	if(!radioObj)
		return;
	var radioLength = radioObj.length;
	if(radioLength == undefined) {
		radioObj.checked = (radioObj.value == newValue.toString());
		return;
	}
	for(var i = 0; i < radioLength; i++) {
		radioObj[i].checked = false;
		if(radioObj[i].value == newValue.toString()) {
			radioObj[i].checked = true;
		}
	}
}

</Script>

<SCRIPT LANGUAGE="JavaScript">
function popWin(URL) {
	 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
function onGenID(){
      document.form1.action  = "customertypelistsave.jsp?action=Auto_ID";
	   document.form1.submit();
	}
function onAdd(){
	   var CUSTOMER_TYPE_ID   = document.form1.CUSTOMER_TYPE_ID1.value;
	   var CUSTOMER_TYPE_DESC = document.form1.CUSTOMER_TYPE_DESC.value;
	    if(CUSTOMER_TYPE_ID == "" || CUSTOMER_TYPE_ID == null) {alert("Please Enter Customer Type ID");document.form1.CUSTOMER_TYPE_ID.focus(); return false; }
	    if(CUSTOMER_TYPE_DESC == "" || CUSTOMER_TYPE_DESC == null) {alert("Please Enter Customer Type Description");document.form1.CUSTOMER_TYPE_DESC.focus(); return false; }
	   document.form1.action  = "customertypelistsave.jsp?action=ADD";
	   document.form1.submit();
	}
function onNew(){
	   document.form1.action  = "customertypelistsave.jsp?action=Clear";
	   document.form1.submit();
	}
</SCRIPT>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",customerTypeID="",customerTypeDesc="";
String sNewEnb    = "enabled";
String sAddEnb    = "enabled";
session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
MLogger mLogger = new MLogger();
CustUtil custutil = new CustUtil();
action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
customerTypeID  = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID1"));
customerTypeDesc  = strUtils.fString(request.getParameter("CUSTOMER_TYPE_DESC"));

//User Check 	
	boolean al=false;
	al = ub.isCheckVal("customertypepopup", plant,sUserId);
	if(al==true)
	{		
		System.out.println("customertypepopup");
	}

 if(action.equalsIgnoreCase("Clear")){
	 customerTypeID  = "";
	 customerTypeDesc  = "";
  }
 else if(action.equalsIgnoreCase("Auto_ID"))
 {
   String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
   TblControlDAO _TblControlDAO =new TblControlDAO();
   _TblControlDAO.setmLogger(mLogger);
       Hashtable  ht=new Hashtable();
       String query=" isnull(NXTSEQ,'') as NXTSEQ";
       ht.put(IDBConstants.PLANT,plant);
       ht.put(IDBConstants.TBL_FUNCTION,"CUSTOMERTYPE");
       try{
        boolean exitFlag=false; boolean resultflag=false;
       exitFlag=_TblControlDAO.isExisit(ht,"",plant);
       //--if exitflag is false than we insert batch number on first time based on plant,currentmonth
      if (exitFlag==false)
       { 
             Map htInsert=null;
             Hashtable htTblCntInsert  = new Hashtable();
             htTblCntInsert.put(IDBConstants.PLANT,plant);
             htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMERTYPE");
             htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"CT");
             htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"000");
             htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"999");
             htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
             htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
             htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
             insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
             customerTypeID="CT"+"001";
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
            htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"CUSTOMERTYPE");
            htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"CT");
            StringBuffer updateQyery=new StringBuffer("set ");
            updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
            boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
            customerTypeID="CT"+sZero+updatedSeq;
            }
            } catch(Exception e)
             {
         	   mLogger.exception(true,
    					"ERROR IN JSP PAGE -customertypelistsave.jsp ", e);
             }
 }
 //2. >> Add
 else if(action.equalsIgnoreCase("ADD")){
 	 Hashtable ht = new Hashtable();
    ht.put(IDBConstants.PLANT,sPlant);
    ht.put(IDBConstants.CUSTOMERTYPEID,customerTypeID);
     if(!(custutil.isExistsCustomerType(ht))) // if the Item  exists already
     {
           ht.put(IDBConstants.PLANT,sPlant);
           ht.put(IDBConstants.CUSTOMERTYPEID,customerTypeID);
           ht.put(IDBConstants.CUSTOMERTYPEDESC,customerTypeDesc); 
           ht.put(IConstants.ISACTIVE,"Y");
           ht.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
           ht.put(IDBConstants.LOGIN_USER,sUserId);
           MovHisDAO mdao = new MovHisDAO(sPlant);
           mdao.setmLogger(mLogger);
           Hashtable htm = new Hashtable();
           htm.put("PLANT",sPlant);
           htm.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
           htm.put("RECID","");
           htm.put("ITEM",customerTypeID);
           htm.put("REMARKS",customerTypeDesc);
           htm.put("UPBY",sUserId);   htm.put("CRBY",sUserId);
           htm.put("CRAT",dateutils.getDateTime());
           htm.put("UPAT",dateutils.getDateTime());
           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
           boolean itemInserted = custutil.insertCustomerTypeMst(ht);
           boolean  inserted = mdao.insertIntoMovHis(htm);
           if(itemInserted&&inserted) {
                     res = "<font class = "+IDBConstants.SUCCESS_COLOR +">Customer Type Added Successfully</font>";
                  
           } else {
                     res = "<font class = "+IDBConstants.FAILED_COLOR +">Failed to add New Customer Type </font>";
           }
     }else{
            res = "<font class = "+IDBConstants.FAILED_COLOR +">Customer Type  Exists already. Try again</font>";
           
     }
  
 }
%>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Customer Type List</h3> 
</div>
</div>
  
<form class="form-horizontal" method="post" name="form1">
<div class="box-body">
<CENTER><strong><%=res%></strong></CENTER>

<div id="target" style="display:none">
 <div class="form-group">
      <label class="control-label col-sm-2" for="Customer Class ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer Type ID:</label>
      <div class="col-sm-3">
      	<div class="input-group">
            <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />    		
    		<input name="CUSTOMER_TYPE_ID1" type="TEXT" value="<%=customerTypeID%>"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();" <%=sAddEnb%>>
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title=Auto-Generate>
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		
      </div>
	  <div class="form-inline">
      <label class="control-label col-sm-3" for="Product Type Description">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer Type Description:</label>
      <div class="col-sm-3">          
        <INPUT  class="form-control" name="CUSTOMER_TYPE_DESC" type="TEXT" value="<%=customerTypeDesc%>" style="width:100%"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    </div>
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
	  <button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
	   		<% if (al) { %>
	   	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	 <% } else { %>
      	 <button disabled="disabled" type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	  <% } %>
      	 <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>
      	      </div>
    </div>
	</div>
    
 <div class="form-group">
      <div class="col-sm-4">
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Create Customer Type</a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Create Customer Type</a>
      </div>
       	  </div>
    </div>  
     
<div >
<table id="myTable" class="table">
<thead style="background: #eaeafa">
        <tr>
          <th>Customer Type ID</th>
          <th>Customer Type Description</th>
          <th>IsActive</th>
      </tr>
    </thead>
    <tbody>


<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
//MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
//StrUtils strUtils = new StrUtils();
CustUtil CustUtil = new CustUtil();
DateUtils  dateUtil = new DateUtils();
CustUtil.setmLogger(mLogger);
 
  String customer_type_id = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
  String type  = strUtils.fString(request.getParameter("TYPE"));
  //String plant = (String)session.getAttribute("PLANT");
  String sBGColor = "";
 try{
  List listQry = CustUtil.getCustTypeList(customer_type_id,plant,"");
  for (int i =0; i<listQry.size(); i++){
  Map map = (Map) listQry.get(i);
  sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
   String customertypeid        = strUtils.fString((String) map.get("CUSTOMER_TYPE_ID"));
   String desc             = strUtils.fString((String) map.get("CUSTOMER_TYPE_DESC"));
    String isactive             = strUtils.fString((String) map.get("ISACTIVE"));
if(type.equalsIgnoreCase("customersummary")){
	%>
	   <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.CUSTOMER_TYPE_ID.value='<%=customertypeid%>';window.close();"><%=customertypeid%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

	</TR>
	<%}else if(type.equalsIgnoreCase("movhis")){
%>
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.CUSTOMER_TYPE_ID.value='<%=customertypeid%>';window.close();"><%=customertypeid%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

	</TR>
	
	<%}else if(type.equalsIgnoreCase("CUSTMODAL")){
%>
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.customerForm.CUSTOMER_TYPE_ID.value='<%=customertypeid%>'; window.close();"><%=customertypeid%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

	</TR>
	
	<%}else{
%>
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.CUSTOMER_TYPE_ID.value='<%=customertypeid%>';window.opener.form.CUSTOMER_TYPE_DESC.value='<%=desc%>'; window.close();"><%=customertypeid%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

	</TR>
   
<%}
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
   
 </tbody>
</table>
    
</div>
</form>
</body>
</html>

<script>
$(document).ready(function(){
	$('#myTable').dataTable();
	
	$('[data-toggle="tooltip"]').tooltip(); 
    
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