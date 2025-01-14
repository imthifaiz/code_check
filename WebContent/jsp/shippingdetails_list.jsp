<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<html>
<head>
<title>Shipping Customer List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/style.css">

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</head>
<body>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<SCRIPT LANGUAGE="JavaScript">


var subWin = null;
function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), '_blank', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){

   document.form.action  = "shippingdetails_list.jsp?action=NEW";
   document.form.submit();
}
function isNumber(evt) {	
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    
    if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
    	if( (charCode!=43 && charCode!=32 && charCode!=45))
    		{
    		
        alert("  Please enter only Numbers.  ");
        return false;
    		}
    	}
    return true;
}
function onAdd(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   var CUST_NAME   = document.form.CUST_NAME1.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE.focus(); return false; }
  /*  if(!IsNumeric(form.PMENT_DAYS.value))
   {
     alert(" Please Enter Days Input In Number");
     form.PMENT_DAYS.focus();  form.PMENT_DAYS.select(); return false;
   } */
   
   if(CUST_NAME == "" || CUST_NAME == null) {
   alert("Please Enter Customer Name"); 
   document.form.CUST_NAME1.focus();
   return false; 
   }
   
   document.form.action  = "shippingdetails_list.jsp?action=ADD";
   document.form.submit();
}

function onIDGen()
{
 document.form.action  = "shippingdetails_list.jsp?action=Auto-ID";
 document.form.submit(); 
}

</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "";
TblControlDAO _TblControlDAO =new TblControlDAO();
String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "enabled";
String sUpdateEnb = "enabled";
String sCustEnb   = "enabled";
String action     = "";
String sCustCode  = "",
       sCustName  = "",
       sCustNameL  = "",
       sAddr1     = "",
       sAddr2     = "",
       sAddr3     = "",
       sAddr4     = "",
 	   sState   = "",
       sCountry   = "",
       sZip       = "",
       sCons      = "Y";
       
String sContactName ="", sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="",sPayTerms="",sPayInDays="",customertypeid="",desc="",customerstatusid="",statusdesc="",sRcbno="";
StrUtils strUtils = new StrUtils();
CustUtil custUtil = new CustUtil();

MLogger mLogger = new MLogger();

custUtil.setmLogger(mLogger);
action            = strUtils.fString(request.getParameter("action"));
String plant = strUtils.fString((String)session.getAttribute("PLANT"));

sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
String taxbylabel= ub.getTaxByLable(plant);
DateUtils dateutils = new DateUtils();
if(sCustCode.length() <= 0) sCustCode  = strUtils.fString(request.getParameter("CUST_CODE1"));
sCustName  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME1")));
sCustNameL  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
sAddr1     = strUtils.fString(request.getParameter("ADDR1"));
sAddr2     = strUtils.fString(request.getParameter("ADDR2"));
sAddr3     = strUtils.fString(request.getParameter("ADDR3"));
sAddr4     = strUtils.fString(request.getParameter("ADDR4"));
sState   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("STATE")));
sCountry   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("COUNTRY")));
sZip       = strUtils.fString(request.getParameter("ZIP"));
sCons      = strUtils.fString(request.getParameter("CONSIGNMENT"));
sContactName      = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CONTACTNAME")));
sDesgination  = strUtils.fString(request.getParameter("DESGINATION"));
sTelNo  = strUtils.fString(request.getParameter("TELNO"));
sHpNo  = strUtils.fString(request.getParameter("HPNO"));
sFax  = strUtils.fString(request.getParameter("FAX"));
sEmail= strUtils.fString(request.getParameter("EMAIL"));
sRcbno= strUtils.fString(request.getParameter("RCBNO"));
sRemarks= strUtils.InsertQuotes(strUtils.fString(request.getParameter("REMARKS")));
sPayTerms=strUtils.InsertQuotes(strUtils.fString(request.getParameter("PAYTERMS")));
sPayInDays=strUtils.InsertQuotes(strUtils.fString(request.getParameter("PMENT_DAYS")));
customertypeid=strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
//List customertypelist=custUtil.getCustTypeList("",plant," AND ISACTIVE ='Y'");

customerstatusid=strUtils.fString(request.getParameter("CUSTOMER_STATUS_ID"));
List customerstatuslist=custUtil.getCustStatusList("",plant," AND ISACTIVE ='Y'");

//User Check 	
	boolean al=false;
	al = ub.isCheckVal("popcustomer", plant,sUserId);
	if(al==true)
	{		
		System.out.println("popcustomer");
	}

if(action.equalsIgnoreCase("NEW")){
     
      sCustCode  = "";
      sCustName  = "";
      sCustNameL="";
      sAddr1     = "";
      sAddr2     = "";
      sAddr3     = ""; sAddr4     = "";
	  sState   = "";
      sCountry   = "";
      sZip       = "";
      sCons      = "Y";
      sContactName =""; 
      sDesgination="";
      sTelNo="";
      sHpNo="";
      sFax="";
      sEmail="";
      sRemarks="";
      sPayTerms="";
      sPayInDays="";
      sRcbno="";
      customertypeid="";
}
else if(action.equalsIgnoreCase("Auto-ID"))
{

String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
//TblControlDAO _TblControlDAO =new TblControlDAO();
_TblControlDAO.setmLogger(mLogger);
      Hashtable  ht=new Hashtable();
     
      String query=" isnull(NXTSEQ,'') as NXTSEQ";
      ht.put(IDBConstants.PLANT,plant);
      ht.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
      try{
      		boolean exitFlag=false; boolean resultflag=false;
      		exitFlag=_TblControlDAO.isExisit(ht,"",plant);
     
     
    	 	if (exitFlag==false)
      		{ 
                    
            	Map htInsert=null;
            	Hashtable htTblCntInsert  = new Hashtable();
           
            	htTblCntInsert.put(IDBConstants.PLANT,plant);
          
            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"C");
             	htTblCntInsert.put("MINSEQ","0000");
             	htTblCntInsert.put("MAXSEQ","9999");
            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
            	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
            
        		sCustCode="C"+"0001";
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
               	  sZero="000";
               }
           	   else if(updatedSeq.length()==2)
          	   {
             	  sZero="00";
           	   }
           	   else if(updatedSeq.length()==3)
           	   {
                  sZero="0";
               }
           
          
           		Map htUpdate = null;
          
           		Hashtable htTblCntUpdate = new Hashtable();
           		htTblCntUpdate.put(IDBConstants.PLANT,plant);
           		htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
           		htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"C");
           		StringBuffer updateQyery=new StringBuffer("set ");
           		updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
         

        		//boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
              	sCustCode="C"+sZero+updatedSeq;
           		}
           	} catch(Exception e)
            {
           		mLogger.exception(true,
    					"ERROR IN JSP PAGE - customer_list_order.jsp ", e);
            }

}
//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
  if(!custUtil.isExistCustomer(sCustCode,plant) && !custUtil.isExistCustomerName(sCustName, plant)) // if the Customer exists already
    {
	  
          Hashtable ht = new Hashtable();
          ht.put(IConstants.PLANT,plant);
          ht.put(IConstants.CUSTOMER_CODE,sCustCode);
          ht.put("USER_ID",sCustCode);
          ht.put(IConstants.CUSTOMER_NAME,sCustName);
          ht.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
          ht.put(IConstants.ADDRESS1,sAddr1);
          ht.put(IConstants.ADDRESS2,sAddr2);
          ht.put(IConstants.ADDRESS3,sAddr3);
          ht.put(IConstants.ADDRESS4,sAddr4);
          ht.put(IConstants.COUNTRY,sCountry);
          ht.put(IConstants.STATE,sState);
          ht.put(IConstants.ZIP,sZip);
          ht.put(IConstants.USERFLG1,sCons);
          ht.put(IConstants.NAME,sContactName);
          ht.put(IConstants.DESGINATION,sDesgination);
          ht.put(IConstants.TELNO,sTelNo);
          ht.put(IConstants.HPNO,sHpNo);
          ht.put(IConstants.FAX,sFax);
          ht.put(IConstants.EMAIL,sEmail);
          ht.put(IConstants.REMARKS,strUtils.InsertQuotes(sRemarks));
          ht.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
		  ht.put(IConstants.PAYINDAYS, sPayInDays);
          ht.put(IConstants.CREATED_AT,new DateUtils().getDateTime());
          ht.put(IConstants.CREATED_BY,sUserId);
          ht.put(IConstants.ISACTIVE,"Y");
          ht.put(IConstants.CUSTOMERTYPEID,customertypeid);
          ht.put(IConstants.CUSTOMERSTATUSID,customerstatusid);
          ht.put(IConstants.RCBNO,sRcbno);
          
          MovHisDAO mdao = new MovHisDAO(plant);
          mdao.setmLogger(mLogger);
          Hashtable htm = new Hashtable();
          htm.put(IDBConstants.PLANT,plant);
          htm.put(IDBConstants.DIRTYPE,TransactionConstants.ADD_CUST);
          htm.put("RECID","");
          htm.put("ITEM",sCustCode);
          htm.put(IDBConstants.CREATED_BY,sUserId);
         
          if(!sRemarks.equals(""))
		  {
			htm.put(IDBConstants.REMARKS, sCustName+","+sRemarks);
		  }
		  else
		  {
			htm.put(IDBConstants.REMARKS, sCustName);
		  }
          
          htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
          
		  boolean updateFlag;
		if(sCustCode!="C0001")
  		  updateFlag=_TblControlDAO.updateSeqNo("CUSTOMER",plant);
		  
          boolean custInserted = custUtil.insertCustomer(ht);
          boolean  inserted = mdao.insertIntoMovHis(htm);
          if(custInserted&&inserted) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Customer Added Successfully</font>";
                    
                  //clear data automatically when click save button
                    sCustCode  = "";
                    sCustName  = "";
                    sCustNameL="";
                    sAddr1     = "";
                    sAddr2     = "";
                    sAddr3     = "";
                    sAddr4     = "";
              	    sState   = "";
                    sCountry   = "";
                    sZip       = "";
                    sCons      = "Y";
                    sContactName =""; 
                    sDesgination="";
                    sTelNo="";
                    sHpNo="";
                    sFax="";
                    sEmail="";
                    sRemarks="";
                    sPayTerms="";
                    sPayInDays="";
                    sRcbno="";
                    customertypeid="";
                  //automatic clear end
                    
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to add New Customer</font>";

          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Customer ID Or Name Exists already. Try again with diffrent Customer ID Or Name.</font>";

    }
}

%>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Shipping Customer List</h3> 
</div>
</div>

<form method="post" name="form" method="post">
<div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>
   <input name="CUSTOMER_TYPE_DESC" type="hidden" value="<%=desc%>">
   <div id="target" style="display:none">
  <div class="row">
  <div class="col-sm-3" style="padding:6px;  align-text:right">
      <label for="Create Customer ID">&nbsp;&nbsp;&nbsp;&nbsp;
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>Customer ID:</label></div>
      <div class="col-sm-3">
      
      	<div class="input-group">  
        <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />  		
      	  	<input name="CUST_CODE" type="TEXT" value="<%=sCustCode%>"
			size="50" MAXLENGTH=50 class="form-control" width="50">
   		 	<span class="input-group-addon"  onClick="onIDGen();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="CUST_CODE1" value="<%=sCustCode%>">
      	</div>
      
      <div class="col-sm-3" style="padding:6px; align:right">
      <label for="Customer Name">&nbsp;&nbsp;
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>Customer Name:</label></div>
     <div class="col-sm-3">
      <INPUT  class="form-control" name="CUST_NAME1" type="TEXT" value="<%=sCustName%>"
			style="width:100%" MAXLENGTH=100>
      
    </div>
    </div>
    
     <div class="row">
     <div class="col-sm-3" style="padding:6px ; align-text:right">
      <label  for="customer type">&nbsp;&nbsp;&nbsp;&nbsp;Customer Type:</label></div>
      <div class="col-sm-3">           	
  	<div class="input-group">    
    		<input name="CUSTOMER_TYPE_ID" type="TEXT" value="<%=customertypeid%>"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon" 
   		  onClick="javascript:popUpWin('customertypelistsave.jsp?CUSTOMER_TYPE_ID='+form.CUSTOMER_TYPE_ID.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  	</div>
  
  </div>
  	 <div class="col-sm-3" style="padding:6px; align:right">
         <label  for="Contact Name">&nbsp;&nbsp;Contact Name:</label></div>
      <div class="col-sm-3">                 
       <INPUT name="CONTACTNAME" type="TEXT" class="form-control" style="width:100%"
			value="<%=sContactName%>"  MAXLENGTH="30" >
      </div>
  	<%-- <div class="col-sm-3" style="padding:6px; align:right">
      <label  for="customer status">&nbsp;&nbsp;Customer Status:</label></div>
      <div class="col-sm-3">         	
  	
      <SELECT NAME="CUSTOMER_STATUS_ID" class="form-control" data-toggle="dropdown" data-placement="right" style="width:100%" size="1">
			<OPTION selected value="NOCUSTOMERSTATUS">Choose</OPTION>
			<%       for (int i =0; i<customerstatuslist.size(); i++){
  			Map map = (Map) customerstatuslist.get(i);
   			customerstatusid         = (String) map.get("CUSTOMER_STATUS_ID");
  			statusdesc   = (String) map.get("CUSTOMER_STATUS_DESC");%>
			<OPTION value="<%=customerstatusid %>"><%=statusdesc %></OPTION>
			<%}%>
			
		</SELECT>
  
  	</div> --%>
   </div> 
    
	<INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
   
   <div class=row>
   <div class="col-sm-3">
         <label for="TRN NO" id="TaxLabel" style="padding:6px; align-text:right"></label></div>
     <div class="col-sm-3">
                 
       <INPUT name="RCBNO" type="TEXT" class="form-control"
			value="<%=sRcbno%>"  MAXLENGTH="50" >
      </div>
      <div class="col-sm-3" style="padding:6px; align-text:right">
      <label for="Designation">&nbsp;&nbsp;Designation:</label></div>
      
         <div class="col-sm-3">       
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>"  MAXLENGTH="30">
      </div>
    
        </div>
   
    <div class=row>
   <div class="col-sm-3" style="padding:6px; align:right">
      <label for="Telephone No">&nbsp;&nbsp;&nbsp;&nbsp;Telephone:</label></div>
      <div class="col-sm-3">  
      <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" style="width:100%" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
       <div class="col-sm-3" style="padding:6px; align:right">
      <label for="Fax">&nbsp;&nbsp;Fax:</label></div>
       <div class="col-sm-3">
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" style="width:100%" onkeypress="return isNumber(event)"
			MAXLENGTH="30" class="form-control">
      </div>
        </div>
   
     <div class=row>
   <div class="col-sm-3" style="padding:6px; align-text:right">
      <label  for="Hand Phone">&nbsp;&nbsp;&nbsp;&nbsp;Mobile:</label></div>
     <div class="col-sm-3">
              
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
      <div class="col-sm-3" style="padding:6px; align-text:right">
      <label for="Email">&nbsp;&nbsp;Email:</label></div>
       <div class="col-sm-3">
       <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50" MAXLENGTH="50" class="form-control">
      </div>
     
    </div>
    
   
    <div class=row>
    <div class="col-sm-3" style="padding:6px; align:right">
      <label  for="Unit No">&nbsp;&nbsp;&nbsp;&nbsp;Unit No:</label></div>
      <div class="col-sm-3">
        <INPUT name="ADDR1" type="TEXT" value="<%=sAddr1%>" style="width:100%" MAXLENGTH=50 class="form-control">
      </div>
      <div class="col-sm-3" style="padding:6px; align-text:right">
      <label for="Street">&nbsp;&nbsp;Street:</label></div>
      <div class="col-sm-3">
      <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" style="width:100%"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    
   
   <div class=row>
   <div class="col-sm-3" style="padding:6px; align-text:right">
      <label for="Building">&nbsp;&nbsp;&nbsp;&nbsp;Building:</label></div>
      <div class="col-sm-3">
              
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
       <div class="col-sm-3" style="padding:6px; align-text:right">
      <label for="State">&nbsp;&nbsp;State:</label></div>
      <div class="col-sm-3">           
        <INPUT name="STATE" type="TEXT" value="<%=sState%>" style="width:100%"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    
    
    <div class="row">
    <div class="col-sm-3" style="padding:6px; align-text:right">
      <label for="City">&nbsp;&nbsp;&nbsp;&nbsp;City:</label></div>
      <div class="col-sm-3"> 
                
        <INPUT name="ADDR4" type="TEXT" value="<%=sAddr4%>" size="50"
			MAXLENGTH=80  class="form-control">
      </div>
      <div class="col-sm-3" style="padding:6px; align-text:right">
      <label for="Country">&nbsp;&nbsp;Country:</label></div>
      <div class="col-sm-3">  
             
       <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control">
      </div>
     
    </div>
    
    
     <div class="row">
      <div class="col-sm-3" style="padding:6px; align-text:right">
          <label for="Payment Terms">&nbsp;&nbsp;&nbsp;&nbsp;Payment Terms:</label></div>
      <div class="col-sm-3"> 
        <div class="input-group">
    		<INPUT class="form-control" name="PAYTERMS" type="TEXT" value="<%=sPayTerms%>" size="20" MAXLENGTH=100 readonly>
		   		<span class="input-group-addon"  onClick="javascript:popUpWin('list/paymenttypelist_save.jsp?paymenttype='+form.PAYTERMS.value);">
		   		<a href="#" data-toggle="tooltip" data-placement="top" title="Payment Type">
		   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span></div>
   		 	 </div>
   
    <div class="col-sm-3" style="padding:6px; align-text:right">
      <label for="Postal Code">&nbsp;&nbsp;Postal Code:</label></div>
      <div class="col-sm-3">
                
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" style="width:100%"
			MAXLENGTH=10 class="form-control">
      </div>
      
          </div>
    
    
    <!-- <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">  -->
               
        <INPUT name="REMARKS" type="hidden" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100 class="form-control ">
      <!-- </div>
    </div> -->
          
           <div class="row">
            <div class="col-sm-3" style="padding:6px; align-text:right">
    		<label class="control-label  col-sm-1" for="Payment Terms">Days:</label></div> 
    		<div class="col-sm-3">    		
    		<input name="PMENT_DAYS" type="text" value="<%=sPayInDays%>"
			size="5" MAXLENGTH=10  class="form-control"><!-- (For Ageing Report) -->
  	</div></div>
  	
    <div class="form-group">        
     <div class="col-sm-8" >
     <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;     
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<% if (al) { %>
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"<%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	<% } else { %>
      	<button disabled="disabled" type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"<%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	<% } %>
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
      	
      </div>
    </div> 
   </div>
   
   <div class="row">
      <div class="col-sm-4">
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Create Shipping Customer</a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Create Shipping Customer</a>
      </div>
       	  </div>
   </div>
   
<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Customer ID</TH>
      <TH>Contact Name</TH>
     
     </TR>
     </thead>
     <tbody>
<%

	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
//	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);
	 MasterUtil  _MasterUtil = new MasterUtil();
	 _MasterUtil.setmLogger(mLogger);
    //StrUtils strUtils = new StrUtils();
    //String plant = (String)session.getAttribute("PLANT");
    String shippingcustomer = strUtils.fString(request.getParameter("SHIPPINGCUSTOMER"));
    String formName = strUtils.fString(request.getParameter("FORMNAME"));
     session=request.getSession();
      String sBGColor = "";
   try{
	  // '"+ item	+"%'"+
	   ArrayList arrList = _MasterUtil.getShippingDetailsList(plant, " cname like  '"+shippingcustomer+"%'");
	   System.out.println("arraysize"+arrList.size());
	    for (int iCnt =0; iCnt<arrList.size(); iCnt++){
			int id=iCnt+1;
            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
            Map lineArr = (Map) arrList.get(iCnt);
            String shippingid  = strUtils.replaceCharacters2Send((String)lineArr.get("CUSTNO"));
            String customername = strUtils.replaceCharacters2Send((String)lineArr.get("CUSTOMERNAME"));
           	if(formName.equalsIgnoreCase("")){	
%>

<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.SHIPPINGCUSTOMER.value='<%=strUtils.insertEscp(customername)%>';
			         window.opener.form.SHIPPINGID.value='<%=strUtils.insertEscp(shippingid)%>';
                     window.close();"><%=strUtils.insertEscp(shippingid)%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.insertEscp(customername)%></td>
	</TR>

    
<%
    }else{
%>
	<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.<%=formName%>.SHIPPINGCUSTOMER.value='<%=strUtils.insertEscp(customername)%>';
			         window.opener.<%=formName%>.SHIPPINGID.value='<%=strUtils.insertEscp(shippingid)%>';
	                    window.close();"><%=strUtils.insertEscp(shippingid)%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.insertEscp(customername)%></td>
	</TR>  		    
<%         		
 	}
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

//Below code is used for auto change Tax By Label
    
    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.:";
    
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