<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>

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
    if (document.form1 .chkdLnNo)
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
		     if(document.form1 .chkdLnNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
		    	 
		    }
		   
	     }
              	     
    }
	
    if (checkFound!=true) {
    	alert("Please check at least one checkbox.");
		return false;
	}
    
   //document.form1.action ="/track/LabelPrintServlet?action=PrintProduct";
   //document.form1.submit();
   
   var formData = $('form#LabelPrintProductForm').serialize();
	$.ajax({
		
      type: 'post',
      url: '/track/LabelPrintServlet?action=PrintProduct', 
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
  document.form1.action ="/track/LabelPrintServlet?action=ViewReportProduct";
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
 
   var ITEM         = document.form1.ITEM.value;
  
   var PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
   var PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
   var PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
   var PRD_DEPT_ID      = document.form1.PRD_DEPT_ID.value;
 
  
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   if(flag == "false"){ alert("Please select the Dirtype"); return false;}
   
  
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

 
   if(ITEM != null     && ITEM != "") { flag = true;}
   
 

   if(PRD_BRAND_ID != null     && PRD_BRAND_ID != "") { flag = true;}
   if(PRD_TYPE_ID != null     && PRD_TYPE_ID != "") 
   { flag = true;}
   if(PRD_CLS_ID != null     && PRD_CLS_ID != "") { flag = true;}
   if(PRD_DEPT_ID != null     && PRD_DEPT_ID != "") { flag = true;}
   var recsPerPage = document.form1.RECS_PER_PAGE.value;
   if (isNumericInput(recsPerPage) == false) {
                      alert("Entered Records display Per Page as Number!");
                      document.form1.RECS_PER_PAGE.value = "";
                      document.form1.RECS_PER_PAGE.focus();
                      return false;
   }
 
  document.form1 .action="LabelPrintProduct.jsp";
  document.form1 .submit();
}




</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Label Print (Product ID & Description)</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils _strUtils     = new StrUtils();
	Generator generator   = new Generator();
	HTReportUtil movHisUtil       = new HTReportUtil();
	movHisUtil.setmLogger(mLogger);
	DateUtils _dateUtils = new DateUtils();
	List locQryList  = new ArrayList();
	ItemUtil itemUtil = new ItemUtil();
	String  fieldDesc="",errorDesc="",loc="",expiredate="",item="",batch="",REASONCODE="",chkString ="",MODULETYPE ="";
	session= request.getSession();
	String plant 	= (String)session.getAttribute("PLANT");
    String userID 	= (String)session.getAttribute("LOGIN_USER");
	String DIRTYPE ="",USER="",ITEM="",ISPOPUP="",
	ITEMDESC="",TRANSACTIONDATE,strTransactionDate,PGaction="";
	PGaction        = _strUtils.fString(request.getParameter("PGaction")).trim();
	String html 	= "",cntRec ="false",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",PRD_DEPT_ID="";
	String PLANT 	= "",Item= "",ItemDesc= "",Uom = "",PrintStatus="";
	String action   = su.fString(request.getParameter("action")).trim();
	String REFNO="",PRINTTYPE="",PRD_CLS_DESC="",PRD_CLS_ID1,PRD_TYPE_DESC="",PRD_TYPE_ID1="",PRD_BRAND_DESC="",ACTIVE="",PRINTSTATUS="",RECS_PER_PAGE="";

	String value=null,allChecked = "";
	String bgcolor="";
	int iColor=0;
	
	double iordertotal=0;
	double ipicktotal=0;
	double iissuetotal=0;
	double ireversetotal=0;
	int k=0;
	long listRec=0;
	
	
	DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
	ITEM = _strUtils.fString(request.getParameter("ITEM"));
	ITEMDESC      = _strUtils.fString(request.getParameter("DESC"));
	PRD_TYPE_ID = _strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	PRD_BRAND_ID = _strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	PRD_DEPT_ID = _strUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
	PRD_CLS_ID = _strUtils.fString(request.getParameter("PRD_CLS_ID"));
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
		DIRTYPE = "LABEL PRINT PRODUCT";
	}
	ItemMstDAO itemdao = new ItemMstDAO();
	itemdao.setmLogger(mLogger);
	if(PGaction.equalsIgnoreCase("View")){
  	try{
  		String extcond="";
  		/*if(PRINTSTATUS.equals("N")){
				extcond=extcond +" AND ISNULL(PRINTSTATUS,'N') = 'N'";
		}
		else if(PRINTSTATUS.equals("C")){
			extcond=extcond+" AND ISNULL(PRINTSTATUS,'N') = 'C'";
		}*/
			
		currentPage="1";
		ItemSesBeanDAO _ItemSesBeanDAO=new ItemSesBeanDAO();
        listRec=_ItemSesBeanDAO.getProductCount(ITEM,ITEMDESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,plant,"");
        int start =  Integer.parseInt(currentPage);
        int end = Integer.parseInt(currentPage)*recPerPage;
        locQryList= itemUtil.queryItemMstForSearchCriteriaNew(ITEM,ITEMDESC ,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,"",plant,extcond,start,end);
        REFNO=(String)request.getSession().getAttribute("refNo");
        ISPOPUP=(String)request.getSession().getAttribute("ISPOPUP");
       	errorDesc=(String)request.getSession().getAttribute("RESULTERROR");
       	
        if(locQryList.size()== 0)
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
    			
    			/* PrintWriter outWriter = response.getWriter();
    			outWriter.println("<script type=\"text/javascript\">");
    			outWriter.println("window.open('LabelSettings.jsp?PRINTTYPE=1','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')");
    		    outWriter.println("</script>"); */
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
			      if(_strUtils.fString(PLANT).length() > 0)
			      ht.put("PLANT",PLANT);
			      locQryList= itemUtil.queryItemMstForSearchCriteriaNew(ITEM,ITEMDESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,"",plant,"",start,end);
		     if (locQryList != null)  {         totalRec = locQryList.size();
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
      document.form1.action = "LabelPrintProduct.jsp";
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
      document.form1.action = "LabelPrintProduct.jsp";
      document.form1.submit();
      }
    }
 </SCRIPT>
<%@ include file="body.jsp"%>
<img id="productBarCode"/ hidden>
<img id="batchBarCode"/ hidden>
<FORM name="form1" method="post" id="LabelPrintProductForm" action="/track/LabelPrintServlet?">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
 <br>
 
 <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Label Print (Product ID & Description)</font></TH>
    </TR>
  </TABLE>
  <br>
   <Center>
  <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
   <div id="labelSettingPopUpContent" class='mainred'></div>
 </table>
  </Center>
  <TABLE border="0" width="80%" height = "15%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
   
     <TR>
          <TH ALIGN="left">&nbsp;Product ID : </TH>
          <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=50>
           <a href="#" onClick="javascript:popUpWin('view_label_product_list.jsp?ITEM='+form1.ITEM.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
          
		<TH ALIGN="left">&nbsp;Product Description : </TH>
          <TD><INPUT name="DESC" type = "TEXT" value="<%=_strUtils.forHTMLTag(ITEMDESC)%>" size="20"  MAXLENGTH=100></TD>
	 </TR>
    
          <TR>
               
            <TH ALIGN="left" width="15%"> &nbsp;Product Category ID:  </TH>
          <TD width="13%"><INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1 .PRD_CLS_ID.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
          <TH ALIGN="left" width="15%"> &nbsp;Product Sub Category ID:  </TH>
          <TD width="13%"><INPUT name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1 .PRD_TYPE_ID.value);"><img src="images/populate.gif" border="0"/>
          </TD>	
        
        </TR>
        <TR>
        <TH ALIGN="left" width="15%"> &nbsp;Product Brand ID:  </TH>
          <TD width="13%"><INPUT name="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1 .PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1 ');">           
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
          
          <!-- <TH ALIGN="left" width="15%">&nbsp;&nbsp;Print Status:  </TH>
	
			<TD >
				<INPUT name="PRINTSTATUS" type = "radio"  value="N"  id="NotPrinted" <%if(PRINTSTATUS.equalsIgnoreCase("N")) {%>checked <%}%>>N
		 		<INPUT  name="PRINTSTATUS" type = "radio" value="C"  id = "Printed" <%if(PRINTSTATUS.equalsIgnoreCase("C")) {%>checked <%}%>>Y
		 		<INPUT  name="PRINTSTATUS" type = "radio" value="ALL"  id = "ALL" <%if(PRINTSTATUS.equalsIgnoreCase("ALL")) {%>checked <%}%>>All
		 	</td> 
        
        </TR>
       
		<TR>
          <TD></TD>
          <TD></TD>
          <TD></TD>
          -->
          <TD ALIGN="left"><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
        </TR>

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
          <TH><font color="#ffffff" align="left"><b>Product Description</TH>
             <TH><font color="#ffffff" align="left"><b>UOM</TH>
	     <!--  <TH><font color="#ffffff" align="left"><b>Print Status</TH>-->
         </tr>
       <%
	      if(locQryList.size()<=0 ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

			<%	
			 
		   strTransactionDate="";
		    for (int iCnt2 =0; iCnt2< locQryList.size(); iCnt2++){
			   // Map lineArr = (Map) locQryList.get(iCnt2);
         		int iIndex = iCnt2 + 1;
         		
         		 Vector vecItem   = (Vector)locQryList.get(iCnt2);
        	    bgcolor = ((iColor == 0) || (iColor % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        		//String uom = itemdao.getItemUOM(plant,(String)lineArr.get("item"));
        		  
				 		 ItemMstUtil itemMstUtil = new ItemMstUtil();
						 itemMstUtil.setmLogger(mLogger);
						 System.out.println("(String)vecItem.get(1)(String)vecItem.get(1)"+(String)vecItem.get(0));
						 String itemItem = itemMstUtil.isValidAlternateItemInItemmst( plant, (String)vecItem.get(0));
						 if (itemItem != "") {
						 	ITEM = itemItem;
						 } else {
						 	throw new Exception("Product not found!");
						 }       		
				 Item= (String)vecItem.get(0);
				 ItemDesc= (String)vecItem.get(1);
				
		         Uom=  (String)vecItem.get(3);
		       
		         PrintStatus= (String)vecItem.get(23);
		         chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom); chkString  =Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+StrUtils.replaceCharacters2Send(Uom);
		         String lineno=String.valueOf(iIndex)+"_"+item;
		        
		       
        	%>

            <TR bgcolor = "<%=bgcolor%>">
            
            <TD width="5%" align="CENTER"><font color="black"><INPUT Type=checkbox style="border: 0;" name="chkdLnNo" 	value="<%=chkString%>" ></font></TD>
            				
		
			<TD align="center" width="13%"><%=Item%></TD>
			<input type="hidden" name = "Item_<%=lineno%>" id = "Item_<%=lineno%>"   value = <%=Item%> ></input>
			 
			 
			<TD align="center" width="15%"><%=ItemDesc%></TD>
            <input type="hidden" name = "ItemDesc_<%=lineno%>" id = "ItemDesc_<%=lineno%>"  value = <%=ItemDesc%> ></input>
			
			
			
			<TD align="center" width="10%"><%=Uom%></TD>
			<input type="hidden" name = "Uom_<%=lineno%>" id = "Uom_<%=lineno%>"  value = <%=Uom%> ></input>
			
			
			
			<!-- <TD align="center" width="9%"><%=PrintStatus%></TD>
			<input type="hidden" name = "PrintStatus_<%=lineno%>" id = "PrintStatus_<%=lineno%>"  value = <%=PrintStatus%> ></input>
			-->             
          </TR>
                    
       <%
       iColor=iColor+1;
     }
		   
     	if(k==0)
			k=1;
     	 if( locQryList.size()<=0){ cntRec ="true";
	  }%>
		  
	
    </TABLE>
      <br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value="Back" onClick="window.location.href='LabelPrintMenu.jsp'">&nbsp; </td>
   <td>   <input type="button" value="Generate Label" onClick="onPrint();" > </td>
   
     <td><input type = "submit" value= "Prev" onclick="return onPrev();" <%=isDisabled%> >&nbsp;

   <input type = "submit" value= "Next" onclick="return onNext();" <%=isDisabled%> >&nbsp;
           Page :<%=curPage%>
             <input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >&nbsp;&nbsp;
	    /&nbsp;&nbsp;
            <%=totalPages%>&nbsp;&nbsp;
   <INPUT type="Hidden" name="DIRTYPE" value="PRODUCT">
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

    function withoutBatch50x25(data,barcodeWidth,fontsize){
    	var imgData=[];
    	var productID=[];	
    	var Description=[];
		var dataLength=data.items.length;
			$.each(data.items, function(i,prodItem){
			// and the formula is:
			//random = random+(Math.floor(Math.random() * (max - min + 1)) + min);
				
				var item = prodItem['ITEM'];
				var itemDesc = prodItem['ITEMDESC'];
			productID.push(item);
			Description.push(itemDesc);
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
			imgData.push($("#productBarCode").attr("src"));
			});

				var doc;
				doc = new jsPDF('l', 'mm', [50, 25]);
				//var widths = doc.internal.pageSize.width;    
				//var heights = doc.internal.pageSize.height;
				
				doc.setFont("Arial Narrow","bold");
				doc.setFontSize(fontsize);
	
				
			//	doc.text(0, 60, "_________________________________________________________________________________________________________________________________________________________________________");
				doc.text(2, 4, "Product ID: "+productID[0]);
				doc.addImage(imgData[0], 'JPEG', 2,5,45,16);
				doc.text(2, 23,Description[0]);
					if(dataLength>=1){
				 for(var i=1;i<dataLength;i++){
				doc.addPage();
				doc.text(2, 4, "Product ID: "+productID[i]);
				 doc.addImage(imgData[i], 'JPEG', 2, 5,45,16);
				 doc.text(2, 23,Description[i]);
				 }
				 }
				data = [];
				doc.save("withoutBatch50x25.pdf");				
	

		}
    
    function withoutBatch100x50(data,barcodeWidth,fontsize){
    	var imgData=[];
    	var productID=[];	
    	var Description=[];
		var dataLength=data.items.length;
			$.each(data.items, function(i,prodItem){
			// and the formula is:
			//random = random+(Math.floor(Math.random() * (max - min + 1)) + min);
				
				var item = prodItem['ITEM'];
				var itemDesc = prodItem['ITEMDESC'];
			productID.push(item);
			Description.push(itemDesc);
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
			imgData.push($("#productBarCode").attr("src"));
			});

				var doc;
				doc = new jsPDF('l', 'mm', [100, 50]);
				//var widths = doc.internal.pageSize.width;    
				//var heights = doc.internal.pageSize.height;
				
				doc.setFont("Arial Narrow","bold");
				doc.setFontSize(fontsize);
	
				
			//	doc.text(0, 60, "_________________________________________________________________________________________________________________________________________________________________________");
				doc.text(11, 4, "Product ID: "+productID[0]);
				doc.addImage(imgData[0], 'JPEG', 11,3,85,45);
				doc.text(11, 49,Description[0]);
					if(dataLength>=1){
				 for(var i=1;i<dataLength;i++){
				doc.addPage();
				doc.text(11, 4, "Product ID: "+productID[i]);
				 doc.addImage(imgData[i], 'JPEG', 11, 3,85,45);
				 doc.text(11, 49,Description[i]);
				 }
				 }
				data = [];
				doc.save("withoutBatch100x50.pdf");				
	

		}
              
</SCRIPT>
<%@ include file="footer.jsp"%>
 
