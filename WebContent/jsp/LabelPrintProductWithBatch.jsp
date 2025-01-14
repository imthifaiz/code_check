<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%>
<%@page import="com.track.constants.IDBConstants"%>
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script src="js/JsBarcode.all.js"></script>
<script src="js/jspdf.js"></script>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Label Setting', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function checkAll(isChk)
{
	var len = document.form1.chkdLnNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form1.chkdLnNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkdLnNo.checked = isChk;
               	}
              	else{
              		document.form1.chkdLnNo[i].checked = isChk;
              	}
            	
        }
    }
}

function onPrint()
{
	 var checkFound = false;  
	 var orderLNo;
	 var len = document.form1.chkdLnNo.length; 
	 if(len == undefined) len = 1;
	for (var i = 0; i < len ; i++)
    {
    	if(len ==1 && document.form1.chkdLnNo.checked)  
    	{
    		chkstring = document.form1.chkdLnNo.value;
     	}
    	else
    	{
    		chkstring = document.form1.chkdLnNo[i].value;
     	}
    	chkdvalue = chkstring.split(',');
		if(len == 1 && (!document.form1.chkdLnNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form1.chkdLnNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
	    	 
	    }
	
	     else {
		     if(document.form1.chkdLnNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
		    	 
		    }
		   
	     }
              	     
    }
	
    if (checkFound!=true) {
    	alert("Please check at least one checkbox.");
		return false;
	}
    
   //document.form1.action ="/track/LabelPrintServlet?action=PrintProductWithBatch";
   //document.form1.submit();
   
    var formData = $('form#LabelPrintWithBatchForm').serialize();
	$.ajax({
		
       type: 'post',
       url: '/track/LabelPrintServlet?action=PrintProductWithBatch', 
       dataType:'html',
       data:  formData,
       async:    false,
       success: function (data) {
       	$('#labelSettingPopUpContent').html(data);
       	
	         
       },
       error: function (data) {	        	
           alert(data.responseText);
       }
   });
 
 }


 
 
function onViewReport()
{
  document.form1.action ="/track/LabelPrintServlet?action=ViewReport";
  document.form1.submit();

}
 
function isNumericInput(strString) {
	var strValidChars = "0123456789";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for (i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
} 


function onGo(){
   var flag    = "false";
     var DIRTYPE        = document.form1.DIRTYPE.value;
   //var USER           = document.form1.CUSTOMER.value;
   var ITEM         = document.form1.ITEM.value;
   var PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
   var PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
   var PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
   
  
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   if(flag == "false"){ alert("Please select the Dirtype"); return false;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
  // if(USER != null    && USER != "") { flag = true;}
   if(ITEM != null     && ITEM != "") { flag = true;}
   if(PRD_BRAND_ID != null     && PRD_BRAND_ID != "") { flag = true;}
   if(PRD_TYPE_ID != null     && PRD_TYPE_ID != "") 
   { flag = true;}
   if(PRD_CLS_ID != null     && PRD_CLS_ID != "") { flag = true;}
   var recsPerPage = document.form1.RECS_PER_PAGE.value;
   if (isNumericInput(recsPerPage) == false) {
       alert("Entered Records display Per Page as Number!");
       document.form1.RECS_PER_PAGE.value = "";
       document.form1.RECS_PER_PAGE.focus();
       return false;
   }
       
  document.form1.action="LabelPrintProductWithBatch.jsp";
  document.form1.submit();
}
/*function onGoPagination(){
	document.form1.action ="LabelPrintProductWithBatch.jsp";
var recsPerPage = document.form1.RECS_PER_PAGE.value;
 if (isNumericInput(recsPerPage) == false) {
                    alert("Entered Records display Per Page as Number!");
                    document.form1.RECS_PER_PAGE.value = "";
                    document.form1.RECS_PER_PAGE.focus();
                    return false;
} 
  document.form1.submit();*/


</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Label Print (Product ID & Description & Batch)</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils _strUtils     = new StrUtils();
	Generator generator   = new Generator();
	HTReportUtil movHisUtil       = new HTReportUtil();
	movHisUtil.setmLogger(mLogger);
	DateUtils _dateUtils = new DateUtils();
	ArrayList movQryList  = new ArrayList();
	ArrayList movItemQryList  = new ArrayList();
	InvMstDAO invmstdao = new InvMstDAO();
	invmstdao.setmLogger(mLogger);
	String  fieldDesc="",errorDesc="",loc="",expiredate="",item="",batch="",REASONCODE="",chkString ="",MODULETYPE ="";
	session= request.getSession();
	String plant 	= (String)session.getAttribute("PLANT");
    String userID 	= (String)session.getAttribute("LOGIN_USER");
	String FROM_DATE ="",  TO_DATE = "", DIRTYPE ="",BATCH ="",USER="",ITEM="",ISPOPUP="",
	fdate="",tdate="",JOBNO="",ORDERNO="",ORDERTYPE="",CUSTOMER="",CUSTOMER_TO="",CUSTOMER_LO="",ITEMDESC="",TRANSACTIONDATE,strTransactionDate,PGaction="";
	PGaction        = _strUtils.fString(request.getParameter("PGaction")).trim();
	String html 	= "",cntRec ="false",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",LOC="",LOC_TYPE_ID="";
	String PLANT 	= "", OrderNo = "", Cname="",Loc = "",Batch="",Item= "",ItemDesc= "",Qty = "",Uom = "",Status="",REFNO="",PrintStatus="";
	String action   = su.fString(request.getParameter("action")).trim();
	String sFrom_Date="",PRINTTYPE="",PRD_CLS_DESC="",PRD_CLS_ID1,PRD_TYPE_DESC="",PRD_TYPE_ID1="",PRD_BRAND_DESC="",ACTIVE="",PRINTSTATUS="",RECS_PER_PAGE="";
	FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE   	  = _strUtils.fString(request.getParameter("TO_DATE"));
	
	String value=null,allChecked = "";
	String bgcolor="";
	int iColor=0;
	
	double iordertotal=0;
	double ipicktotal=0;
	double iissuetotal=0;
	double ireversetotal=0;
	int k=0;
	 long listRec=0;

	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	String curDate =_dateUtils.getDate();
	if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	if (FROM_DATE.length()>5)
	fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
	
	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	if (TO_DATE.length()>5)
	tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
	
	DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
	USER          = _strUtils.fString(request.getParameter("USER"));
	ITEM        = _strUtils.fString(request.getParameter("ITEM"));
	BATCH         = _strUtils.fString(request.getParameter("BATCH"));
	ITEMDESC      = _strUtils.fString(request.getParameter("DESC"));
	PRD_TYPE_ID = _strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	PRD_BRAND_ID = _strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	PRD_CLS_ID = _strUtils.fString(request.getParameter("PRD_CLS_ID"));
	LOC = _strUtils.fString(request.getParameter("LOC"));
	LOC_TYPE_ID = _strUtils.fString(request.getParameter("LOC_TYPE_ID"));
	MODULETYPE            = _strUtils.fString(request.getParameter("MODULENAME"));
	PRD_CLS_DESC=_strUtils.fString(request.getParameter("PRD_CLS_DESC"));
	PRD_CLS_ID1=_strUtils.fString(request.getParameter("PRD_CLS_ID1"));
	PRD_TYPE_DESC=_strUtils.fString(request.getParameter("PRD_TYPE_DESC"));
	PRD_TYPE_ID1=_strUtils.fString(request.getParameter("PRD_TYPE_ID1"));
	PRD_BRAND_DESC=_strUtils.fString(request.getParameter("PRD_BRAND_DESC"));
	ACTIVE=_strUtils.fString(request.getParameter("ACTIVE"));
	PRINTSTATUS=_strUtils.fString(request.getParameter("PRINTSTATUS"));
	RECS_PER_PAGE = _strUtils.fString(request.getParameter("RECS_PER_PAGE"));
	String listRecSize =_strUtils.fString(request.getParameter("listRecSize")); 
	if(listRecSize.length()==0){
		listRecSize ="0";
		}
	  int curPage      = 1;
	 if(RECS_PER_PAGE.length()==0) RECS_PER_PAGE = "100";
	    int recPerPage   = Integer.parseInt(RECS_PER_PAGE);
	    long totalRec     = 0;
	    String currentPage        = _strUtils.fString(request.getParameter("cur_page"));
	    String isDisabled ="disabled";
	    
	if(PRINTSTATUS.equals(""))
    {
		PRINTSTATUS="N";
	}
	//ISPOPUP=_strUtils.fString(request.getParameter("ISPOPUP"));
	
	if(DIRTYPE.length()<=0){
		DIRTYPE = "LABEL PRINT PRODUCT WITH BATCH";
	}
	ItemMstDAO itemdao = new ItemMstDAO();
	itemdao.setmLogger(mLogger);
	if(PGaction.equalsIgnoreCase("View")){
  	try{
  		
  		 Hashtable ht = new Hashtable();

         if(_strUtils.fString(ITEM).length() > 0)            ht.put("ITEM",ITEM);
        if(_strUtils.fString(BATCH).length() > 0)              ht.put("BATCH",BATCH);
        if(_strUtils.fString(PRD_TYPE_ID).length() > 0) 	   ht.put("ITEMTYPE",PRD_TYPE_ID);
        if(_strUtils.fString(PRD_BRAND_ID).length() > 0)       ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
        if(_strUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("PRD_CLS_ID",PRD_CLS_ID);
         if(_strUtils.fString(LOC).length() > 0)         	   ht.put("LOC",LOC);
        if(_strUtils.fString(LOC_TYPE_ID).length() > 0)        ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
       // if(PRINTSTATUS.equals("N") || PRINTSTATUS.equals("C"))  ht.put("PRINTSTATUS",PRINTSTATUS); 
        
       currentPage="1";
        listRec=movHisUtil.getLabelPrintProductWithBatchCount(ht,"LABEL PRINT PRODUCT WITH BATCH",plant,ITEMDESC);
        System.out.println(" listRec"+ listRec);
       int start =  Integer.parseInt(currentPage);
       int end = Integer.parseInt(currentPage)*recPerPage;
       
       
     	   movQryList = movHisUtil.getLabelPrintProductWithBatch(ht,"LABEL PRINT PRODUCT WITH BATCH",plant,ITEMDESC,start,end);
        REFNO=(String)request.getSession().getAttribute("refNo");
        ISPOPUP=(String)request.getSession().getAttribute("ISPOPUP");
            
     	errorDesc=(String)request.getSession().getAttribute("RESULTERROR");
     	
     	 if(movQryList.size()== 0)
  	      {
  	        fieldDesc="Data Not Found";
  	      }
   		 if (listRec >0 )  {         totalRec = listRec; listRecSize=new Long(totalRec).toString();
               isDisabled="";
         }
         if (currentPage.length() > 0)
         {
         try  {   curPage = (new Integer(currentPage)).intValue(); 
          System.out.println("curPage :: "+curPage); 
         }
           catch (Exception e)   {   curPage = 1;                                      }
       }
               
               
    	if(errorDesc.length() != 0 && !errorDesc.equals(null))
    	{
        	fieldDesc = "<font class='mainred'>" + errorDesc + "</font>";
      		allChecked = su.fString(request.getParameter("allChecked"));
    	}
    	else
    	{    
    		System.out.println("ISPOPUP"+ISPOPUP);
    		if(ISPOPUP.equals("OPENED")){
    		if(REFNO.length()>0  )
    		{
    			
    			PrintWriter outWriter = response.getWriter();
    			outWriter.println("<script type=\"text/javascript\">");
    			outWriter.println("window.open('LabelSettings.jsp?PRINTTYPE=2','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')");
    		    outWriter.println("</script>");
    		  }
    		}
    		
    	}
  request.getSession().setAttribute("RESULT","");
  request.getSession().setAttribute("RESULTERROR","");
     
 }catch(Exception e) { 
	  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
	  if(e.getMessage()==null)
	  {
		  fieldDesc="";
	  }
 }
 	
}
	if(PGaction.equalsIgnoreCase("PREVORNEXT")){
		 try{

		int start = (new Integer(currentPage)-1)*recPerPage;
		int end = (new Integer(currentPage)*recPerPage)+1;
		   Hashtable ht = new Hashtable();
		      movQryList = movHisUtil.getLabelPrintProductWithBatch(ht,"LABEL PRINT PRODUCT WITH BATCH",plant,ITEMDESC,start,end);
			  if (movQryList != null)  {         totalRec = movQryList.size();
		     isDisabled="";
		     }
		     if (currentPage.length() > 0)
		     {
		     
		    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
		    catch (Exception e)   {   curPage = 1;                                      }
		    }
		     
		   
		   
		 }catch(Exception e) {System.out.println("Exception :getStockTakeList"+e.toString()); }
		}

		 int totalPages = (Integer.parseInt(listRecSize) + recPerPage -1)/recPerPage;
		    if (curPage > Integer.parseInt(listRecSize)) curPage = 1;                    // Out of range

		    System.out.println("totalPages :: "+totalPages);
		    System.out.println("curPage :: "+curPage);
	

%>
<SCRIPT LANGUAGE="JavaScript">

    var cur_page     = <%=curPage%>;                            // Current display page
    var total_pages   = <%=totalPages%>;                         // The total number of records

    // Display previous page of user list
    function onPrev()
    {
	if (cur_page <= 1)  return false;
	cur_page = parseInt(cur_page) -1;
	document.form1.cur_page.value = cur_page;
       document.form1.PGaction.value="PREVORNEXT";
      document.form1.action = "LabelPrintProductWithBatch.jsp";
	document.form1.submit();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Display next page of user list
    function onNext()
    {
    if (cur_page >= total_pages)  {
      return false;
    }else{
      cur_page = parseInt(cur_page) + 1;
      document.form1.cur_page.value = cur_page;
      document.form1.PGaction.value="PREVORNEXT";
      document.form1.action = "LabelPrintProductWithBatch.jsp";
      document.form1.submit();
      }
    }
 </SCRIPT>
 
<%@ include file="body.jsp"%>
<img id="productBarCode"/ hidden>
<img id="batchBarCode"/ hidden>
<FORM name="form1" method="post"  id="LabelPrintWithBatchForm" action="/track/LabelPrintServlet?">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
 <br>
 <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Label Print (Product ID & Description & Batch)</font></TH>
    </TR>
  </TABLE>
  <br>
   <Center>
  
  <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
    <div id="labelSettingPopUpContent" class='mainred'></div>
 </table>
  </Center>
  <TABLE border="0" width="80%" height = "20%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
   
     <TR>
          <TH ALIGN="left">&nbsp;Product ID : </TH>
          <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=50>
           <a href="#" onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
           <TH ALIGN="left">&nbsp;Product Description : </TH>
          <TD><INPUT name="DESC" type = "TEXT" value="<%=_strUtils.forHTMLTag(ITEMDESC)%>" size="20"  MAXLENGTH=100></TD>
          
		
	 </TR>
     
    <TR>
          <TH ALIGN="left" >&nbsp;Batch : </TH>
          <TD><INPUT name="BATCH" type = "TEXT" value="<%=BATCH%>" size="20"  MAXLENGTH=40>
          <a href="#" onClick="javascript:popUpWin('batch_list_inventory.jsp?BATCH='+form1.BATCH.value);">
		   
		   <img src="images/populate.gif" border="0" />
		    </a>
           </TD>
          
           <TH ALIGN="left" width="15%">&nbsp;Product Class ID:  </TH>
          <TD width="13%"><INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
                 
    </TR>
     
          <TR>
               <TH ALIGN="left" width="15%">&nbsp;Product Type ID:  </TH>
          <TD width="13%"><INPUT name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><img src="images/populate.gif" border="0"/>
          </TD>	
          
          <TH ALIGN="left" width="15%">&nbsp;Product Brand ID:  </TH>
          <TD width="13%"><INPUT name="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');">           
           <img src="images/populate.gif" border="0"/>
          </a>
<input type="button" value="View"  onClick="javascript:return onGo();">
          </TD>
        
        </TR>
        
         <TR>
 <!--
         <TH ALIGN="left">&nbsp;Location :</TH>
		<TD><INPUT name="LOC" type="TEXT" value="<%=LOC%>"
			size="20" MAXLENGTH=50>
		<a href="#" onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
	   </TD>
	   
          <TH ALIGN="left">&nbsp;Location Type: </TH>
		<TD><INPUT name="LOC_TYPE_ID"  type="TEXT" value="<%=LOC_TYPE_ID%>"
			size="20" MAXLENGTH=20>
			<a href="#"	onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"><img
			src="images/populate.gif" border="0"></a></TD>
		</TR>
	
       
        <TR>
       
         <TH ALIGN="left" width="15%">&nbsp;Print Status:  </TH>
	
			<TD >
				<INPUT name="PRINTSTATUS" type = "radio"  value="N"  id="NotPrinted" <%if(PRINTSTATUS.equalsIgnoreCase("N")) {%>checked <%}%>>N
		 		<INPUT  name="PRINTSTATUS" type = "radio" value="C"  id = "Printed" <%if(PRINTSTATUS.equalsIgnoreCase("C")) {%>checked <%}%>>Y
		 		<INPUT  name="PRINTSTATUS" type = "radio" value="ALL"  id = "ALL" <%if(PRINTSTATUS.equalsIgnoreCase("ALL")) {%>checked <%}%>>All
		 	</td> 
		 	 <TD></TD>
		 	 <TD ALIGN="left"><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
		 	</TR>
	
 -->
 <input type="hidden" name="LOC_DESC" value="">
	 
  </TABLE>
	<br>

  <TABLE WIDTH="30%"  border="0" cellspacing="1" cellpadding = 2 align = "right">
 <TR> <TD colspan = 7></td><td colspan=3> Records display Per Page : </td><TD><INPUT name="RECS_PER_PAGE" type = "TEXT" value="<%=RECS_PER_PAGE%>" size="10"  MAXLENGTH=4></td></tr>
   </TABLE> 
     
<table BORDER = "1" WIDTH = "92%" align="center" bgcolor="#dddddd" >
		<tr>
		<td width = "15%">  
			<INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
        	&nbsp; Select/Unselect All 
         </td>
		<tr>
</table>
  <TABLE WIDTH="95%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
 
    <TR BGCOLOR="#000066">
        <!--  <TH><font color="#ffffff" align="center">S/N</TH> -->
         <TH><font color="#ffffff" align="left"><b>Chk</TH>
             
         <TH><font color="#ffffff" align="left"><b>Product ID</TH>
         <TH><font color="#ffffff" align="left"><b>Description</TH>
         <TH><font color="#ffffff" align="left"><b><%=IDBConstants.UOM_LABEL%></TH>
           <TH><font color="#ffffff" align="left"><b>Batch</TH>
         <TH><font color="#ffffff" align="left"><b>Quantity</TH>
        <!--  <TH><font color="#ffffff" align="left"><b>Loc</TH>
              
	     <TH><font color="#ffffff" align="left"><b>Print Status</TH>
         -->
         </tr>
       <%
	      if(movQryList.size()<=0 ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
		 
			 
		   strTransactionDate="";
		    for (int iCnt =0; iCnt< movQryList.size(); iCnt++){
			    Map lineArr = (Map) movQryList.get(iCnt);
         		int iIndex = iCnt + 1;
         		
        	    bgcolor = ((iColor == 0) || (iColor % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        		//String uom = itemdao.getItemUOM(plant,(String)lineArr.get("item"));
        		loc = (String)lineArr.get("loc");
        		String locarry[] = loc.split("_");
        		loc = locarry[locarry.length-1];
        		item = (String)lineArr.get("item");
          		 Item= (String)lineArr.get("item");
				 ItemDesc= (String)lineArr.get("itemdesc");
				 Loc = (String)lineArr.get("loc");
		         Batch= (String)lineArr.get("batch");
		         Qty= (String)lineArr.get("qty");
		       //  Uom=  itemdao.getItemUOM(plant,item);
		       Uom=(String)lineArr.get("uom");
		        
		         PrintStatus= (String)lineArr.get("printstatus");
		         chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+Loc+",,,"+Batch+",,,"+StrUtils.replaceCharacters2Send(Uom);
		 
		         String lineno=String.valueOf(iIndex)+"_"+OrderNo+"_"+Loc+"_"+Batch;
		         if(Status.equals("Empty"))
		         {
		        	 Status="";
		         }
		       
        	%>

            <TR bgcolor = "<%=bgcolor%>">
            
            <TD width="5%" align="CENTER"><font color="black"><INPUT Type=checkbox style="border: 0;" name="chkdLnNo" 	value="<%=chkString%>" ></font></TD>
            
           			
			<TD align="center" width="13%"><%=Item%></TD>
			<input type="hidden" name = "Item_<%=lineno%>" id = "Item_<%=lineno%>"   value = <%=Item%> ></input>
			
	
			<TD align="center" width="15%"><%=ItemDesc%></TD>
            <input type="hidden" name = "ItemDesc_<%=lineno%>" id = "ItemDesc_<%=lineno%>"  value = <%=ItemDesc%> ></input>
            
            <TD align="center" width="10%"><%=Uom%></TD>
			<input type="hidden" name = "Uom_<%=lineno%>" id = "Uom_<%=lineno%>"  value = <%=Uom%> ></input>
			
			  <TD align="center" width="8%"><%=Batch%></TD>
            <input type="hidden" name = "Batch_<%=lineno%>" id = "Batch_<%=lineno%>"  value = <%=Batch%> ></input>
			<!--
			<TD align="center" width="9%"><%=Loc%></TD>
			<input type="hidden" name = "Loc_<%=lineno%>" id = "Loc_<%=lineno%>" value = <%=Loc%> ></input>
			
          		
			<TD align="center" width="9%"><%=PrintStatus%></TD>
			<input type="hidden" name = "PrintStatus_<%=lineno%>" id = "PrintStatus_<%=lineno%>"  value = <%=PrintStatus%> ></input>
			             
-->
 <TD align="center" width="9%"><%=Qty%></TD>
          </TR>
                    
   
	 
	  <%
       iColor=iColor+1;
     }
		   
     	if(k==0)
			k=1;
     	 if(  movQryList.size()<=0){ cntRec ="true";
	  }%>
		  
	
    </TABLE>
      <br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='LabelPrintMenu.jsp'">&nbsp; </td>
   <td>   <input type="button" value="Generate Label" onClick="onPrint();" > </td>
      <td><input type = "submit" value= "Prev" onclick="return onPrev();" <%=isDisabled%> >&nbsp;

   <input type = "submit" value= "Next" onclick="return onNext();" <%=isDisabled%> >&nbsp;
           Page :<%=curPage%>
             <input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >&nbsp;&nbsp;
	    /&nbsp;&nbsp;
            <%=totalPages%>&nbsp;&nbsp;
   </td>
   <INPUT type="Hidden" name="DIRTYPE" value="LABEL PRINT PRODDUCT WITH BATCH">
   <INPUT type="Hidden" name="REFNO" value=<%=REFNO%>>
    <INPUT type="Hidden" name="PRD_CLS_DESC" value=<%=PRD_CLS_DESC%>>
   <INPUT type="Hidden" name="PRD_CLS_ID1" value=<%=PRD_CLS_ID1%>>
   <INPUT type="Hidden" name="PRD_TYPE_DESC" value=<%=PRD_TYPE_DESC%>>
   <INPUT type="Hidden" name="PRD_TYPE_ID1" value=<%=PRD_TYPE_ID1%>>
   <INPUT type="Hidden" name="PRD_BRAND_DESC" value=<%=PRD_BRAND_DESC%>>
    <INPUT type="Hidden" name="ACTIVE" value=<%=ACTIVE%>>
    <input type="hidden" name="visited" value="" />
      <INPUT type="hidden" name="listRecSize" value="<%=listRecSize%>">
  
  </TR>
    </table>
       <INPUT name="JOBNO" type = "Hidden" value="<%=JOBNO%>" size="20"  MAXLENGTH=20>
  </FORM>
<SCRIPT>

    function validateProduct() {
        var productId = document.form1.ITEM.value;
	var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=PLANT%>",
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					
                                    if (data.status == "100") {
                                            var resultVal = data.result;
                                            document.form1.DESC.value = resultVal.sItemDesc;
                                     } 
				}
			});
		
	}


	function withBatch50x25(data,barcodeWidth,fontsize){
		var productID=[];	
		var Description=[];
		var batchBarCode=[];
		var productImgData=[];
		var batchImgData=[];
	
		var dataLength=data.items.length;
	
		$.each(data.items, function(i,prodItem){

			var item = prodItem['ITEM'];
			var itemDesc = prodItem['ITEMDESC'];
			var batch = prodItem['BATCH'];
		productID.push(item);
		Description.push(itemDesc);
		batchBarCode.push(batch);
		JsBarcode("#productBarCode",
				item,
				{displayValue: false ,
					orientation: 'landscape',
					format:'CODE128',
					lineColor: "black",
					textPosition: "bottom",
					width:barcodeWidth,
					height:150,
					text:item,
					textAlign:"center",
					textMargin:2,
					fontSize:10,
					background:"",
					lineColor:"",
					marginLeft:1,
					marginRight:0,				
					marginTop:10,
					marginBottom:10,
		});
		JsBarcode("#batchBarCode",
			batch,
			{displayValue: false ,
				orientation: 'landscape',
				format:'CODE128',
				lineColor: "black",
				textPosition: "bottom",
				width:barcodeWidth,
				height:150,
				text:batch,
				textAlign:"center",
				textMargin:2,
				fontSize:10,
				background:"",
				lineColor:"",
				marginLeft:1,
				marginRight:0,				
				marginTop:10,
				marginBottom:10,
		});
		productImgData.push($("#productBarCode").attr("src"));
		batchImgData.push($("#batchBarCode").attr("src"));
		});
		
			var data = [], doc;
			doc = new jsPDF('l', 'mm', [50, 25]);
			//var widths = doc.internal.pageSize.width;    
			//var heights = doc.internal.pageSize.height;
			
			doc.setFont("Arial Narrow","bold");
			doc.setFontSize(fontsize);
		//	doc.text(0, 60, "_________________________________________________________________________________________________________________________________________________________________________");
			doc.text(2, 3, "Product ID: "+productID[0]);
			doc.addImage(productImgData[0], 'JPEG', 2, 5,45,5);
			doc.text(2, 13,Description[0]);
			doc.addImage(batchImgData[0], 'JPEG', 2, 14,45,5);
			doc.text(2, 23, "Batch/SN: "+batchBarCode[0]);
			if(dataLength>=1){
			 for(var i=1;i<dataLength;i++){
			doc.addPage();
			doc.text(2, 3, "Product ID: "+productID[i]);
			 doc.addImage(productImgData[i], 'JPEG', 2, 5,45,5);
			 doc.text(2, 13,Description[i]);
			doc.addImage(batchImgData[i], 'JPEG', 2, 14,45,5);
			doc.text(2, 23, "Batch/SN: "+batchBarCode[i]);
			 }
			}
			data = [];
			doc.save("withBatch50x25.pdf");


		}
    
	function withBatch100x50(data,barcodeWidth,fontsize){
		var productID=[];	
		var Description=[];
		var batchBarCode=[];
		var productImgData=[];
		var batchImgData=[];
	
		var dataLength=data.items.length;
	
		$.each(data.items, function(i,prodItem){

			var item = prodItem['ITEM'];
			var itemDesc = prodItem['ITEMDESC'];
			var batch = prodItem['BATCH'];
		productID.push(item);
		Description.push(itemDesc);
		batchBarCode.push(batch);
		JsBarcode("#productBarCode",
				item,
				{displayValue: false ,
					orientation: 'landscape',
					format:'CODE128',
					lineColor: "black",
					textPosition: "bottom",
					width:barcodeWidth,
					height:150,
					text:item,
					textAlign:"center",
					textMargin:2,
					fontSize:10,
					background:"",
					lineColor:"",
					marginLeft:1,
					marginRight:0,				
					marginTop:10,
					marginBottom:10,
		});
		JsBarcode("#batchBarCode",
			batch,
			{displayValue: false ,
				orientation: 'landscape',
				format:'CODE128',
				lineColor: "black",
				textPosition: "bottom",
				width:barcodeWidth,
				height:150,
				text:batch,
				textAlign:"center",
				textMargin:2,
				fontSize:10,
				background:"",
				lineColor:"",
				marginLeft:1,
				marginRight:0,				
				marginTop:10,
				marginBottom:10,
		});
		productImgData.push($("#productBarCode").attr("src"));
		batchImgData.push($("#batchBarCode").attr("src"));
		});
		
			var doc;
			doc = new jsPDF('l', 'mm', [100, 50]);
			
			doc.setFont("Arial Narrow","bold");
			doc.setFontSize(fontsize);
		//	doc.text(0, 60, "_________________________________________________________________________________________________________________________________________________________________________");
			doc.text(11, 4, "Product ID: "+productID[0]);
			doc.addImage(productImgData[0], 'JPEG', 11, 4,85,19);
			doc.text(11, 27,Description[0]);
			doc.addImage(batchImgData[0], 'JPEG', 11, 28,85,19);
			doc.text(11, 49, "Batch/SN: "+batchBarCode[0]);
			if(dataLength>=1){
			 for(var i=1;i<dataLength;i++){
			doc.addPage();
			doc.text(11, 4, "Product ID: "+productID[i]);
			 doc.addImage(productImgData[i], 'JPEG', 11, 4,85,19);
			 doc.text(11, 27,Description[i]);
			doc.addImage(batchImgData[i], 'JPEG', 11, 28,85,19);
			doc.text(11, 49, "Batch/SN: "+batchBarCode[i]);
			 }
			}
			data = [];
			doc.save("withBatch100x50.pdf");


		}
                        
</SCRIPT>
<%@ include file="footer.jsp"%>
 
