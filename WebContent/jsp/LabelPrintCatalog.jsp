<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.ItemSesBeanDAO"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%><html>
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script src="js/JsBarcode.all.js"></script>
<script src="js/jspdf.js"></script>
<script language="javascript">

var subWin = null;

function popUpWin(URL) {
	  subWin = window.open(URL, 'Label Setting', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
	
function popUpMobWin(URL) {
	subWin = window
			.open(
					URL,
					'Catalog',
					'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=350,height=500,left = 420,top = 140');
}
 function onGo(){
 
     document.form.submit();
}
 
 function checkAll(isChk)
 {
 	var len = document.form.chkdLnNo.length;
 	 var orderLNo; 
 	 if(len == undefined) len = 1;  
     if (document.form.chkdLnNo)
     {
         for (var i = 0; i < len ; i++)
         {      
               	if(len == 1){
               		document.form.chkdLnNo.checked = isChk;
                	}
               	else{
               		document.form.chkdLnNo[i].checked = isChk;
               	}
             	
         }
     }
 }
 
 function onPrint()
 {
 	 var checkFound = false;  
 	 var orderLNo;
 	 var len = document.form.chkdLnNo.length; 
 	 if(len == undefined) len = 1;
 	for (var i = 0; i < len ; i++)
     {
     	if(len ==1 && document.form.chkdLnNo.checked)  
     	{
     		chkstring = document.form.chkdLnNo.value;
      	}
     	else
     	{
     		chkstring = document.form.chkdLnNo[i].value;
      	}
     	chkdvalue = chkstring.split(',');
 		if(len == 1 && (!document.form.chkdLnNo.checked))
 		{
 			checkFound = false;
 		}
 		
 		else if(len ==1 && document.form.chkdLnNo.checked)
 	     {
 	    	 checkFound = true;
 	    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
 	    	 
 	    }
 	
 	     else {
 		     if(document.form.chkdLnNo[i].checked){
 		    	 checkFound = true;
 		    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
 		    	 
 		    }
 		   
 	     }
               	     
     }
 	
 	
 	
     if (checkFound!=true) {
     	alert("Please check at least one checkbox.");
 		return false;
 	}
     
    //document.form.action ="/track/LabelPrintServlet?action=PrintProduct";
    //document.form.submit();
    
    var formData = $('form#LabelPrintCatalog').serialize();
 	$.ajax({
 		
       type: 'post',
       url: '/track/LabelPrintServlet?action=PrintCatalog', 
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
</script>
<title>Label Print (Catalog)</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	ArrayList locQryList = new ArrayList();
	
	String fieldDesc = "";
	String DIRTYPE ="",ISPOPUP="",errorDesc="",REFNO="";
	String PLANT = "", item = "",descr="",extraCond ="",imageFile="",RECS_PER_PAGE="",PRD_CLS_ID="",PRD_TYPE_ID="",PRD_BRAND_ID= "";
	int Total = 0;
    int listRec=0;
	String SumColor = "",chkString = "",allChecked = "";
	boolean flag = false;
	session = request.getSession();
	String PGaction = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT = session.getAttribute("PLANT").toString();
	item = strUtils.fString(request.getParameter("PRODUCTID"));
	descr = strUtils.fString(request.getParameter("DESCR"));
	PRD_CLS_ID= strUtils.fString(request.getParameter("PRD_CLS_ID"));
	PRD_TYPE_ID= strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	PRD_BRAND_ID= strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	CatalogUtil _catalogUtil = new CatalogUtil();
	_catalogUtil.setmLogger(mLogger);
    RECS_PER_PAGE = strUtils.fString(request.getParameter("RECS_PER_PAGE"));
    DIRTYPE       = strUtils.fString(request.getParameter("DIRTYPE"));
    REFNO=(String)request.getSession().getAttribute("refNo");
    if(DIRTYPE.length()<=0){
		DIRTYPE = "LABEL PRINT CATALOG";
	}
	   
    int curPage      = 1;
	if(RECS_PER_PAGE.length()==0) RECS_PER_PAGE = "25";
	int recPerPage   = Integer.parseInt(RECS_PER_PAGE);
	long totalRec     = 0;
	String isDisabled ="disabled";
	String currentPage        = strUtils.fString(request.getParameter("cur_page"));
	String listRecSize =strUtils.fString(request.getParameter("listRecSize")); 
	if(listRecSize.length()==0){
	listRecSize ="0";
	}
	
	if (PGaction.equalsIgnoreCase("View")) {
		try {
			 currentPage="1";
			Hashtable ht = new Hashtable();
			if (strUtils.fString(PLANT).length() > 0)
				 ht.put("PLANT", PLANT);
			if(item.length()>0)
				 ht.put(IDBConstants.PRODUCTID, item);
			 if(descr.length()>0){
				 descr = new StrUtils().InsertQuotes(descr);
				 extraCond= "  DESCRIPTION1 like '%"+descr+"%'";
			 }
			 listRec = _catalogUtil.NoofRecords(ht, extraCond);
            
            int start =  Integer.parseInt(currentPage);
            int end = Integer.parseInt(currentPage)*recPerPage;
            
	
			locQryList = _catalogUtil.getCatalogList(PLANT,item,descr,"", start,end,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID);

			if (locQryList.size() == 0) {
				fieldDesc = "Data Not Found";
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

		} catch (Exception e) {
		}
	}
	
	
	if(PGaction.equalsIgnoreCase("PREVORNEXT")){
		 try{

		int start = ((new Integer(currentPage)-1)*recPerPage)+1;
		int end = (new Integer(currentPage)*recPerPage);//+1
		Hashtable ht = new Hashtable();
		if (strUtils.fString(PLANT).length() > 0)
			ht.put("PLANT", PLANT);
		if(item.length()>0)
			ht.put(IDBConstants.PRODUCTID, item);
		 if(descr.length()>0){
			 descr = new StrUtils().InsertQuotes(descr);
			 extraCond= " AND DESCRIPTION1 like '%"+descr+"%'";
	           }
		 locQryList = _catalogUtil.getCatalogList(PLANT,item,descr,"", start,end,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID);
		     if (locQryList != null)  {         totalRec = locQryList.size();
		     isDisabled="";
		     }
		     if (currentPage.length() > 0)
		     {
		     
		    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
		    catch (Exception e)   {   curPage = 1;                                      }
		    }
		   
		 }catch(Exception e) {System.out.println("Exception :getCatalogList"+e.toString()); }
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
				document.form.cur_page.value = cur_page;
				document.form.PGaction.value="PREVORNEXT";
				document.form.action = "LabelPrintCatalog.jsp";
				document.form.submit();
		    }
		    //////////////////////////////////////////////////////////////////////////////////////////////////
		    // Display next page of user list
		    function onNext()
		    {
			    if (cur_page >= total_pages)  {
			      return false;
			    }else{
			      cur_page = parseInt(cur_page) + 1;
			      document.form.cur_page.value = cur_page;
			      document.form.PGaction.value="PREVORNEXT";
			      document.form.action = "LabelPrintCatalog.jsp";
			      document.form.submit();
			      }
		    }
</SCRIPT>
<div hidden id="qrcode" style="width:100px; height:100px; margin-top:15px;"></div>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" id="LabelPrintCatalog" >
<input 	type="hidden" name="xlAction" value="">
<input type="hidden"	name="PGaction" value="View"> 
 <INPUT type="hidden" name="listRecSize" value="<%=listRecSize%>">
<input type="hidden" name="PRICE"></input>	
<input type="hidden" name="DESCRIPTION1"></input>	
	<br>

<TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Label Print (Catalog)</font></TH>
    </TR>
  </TABLE>
  <br>
   <Center>
  <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
   <div id="labelSettingPopUpContent" class='mainred'></div>
 </table>
  </Center>
  
<TABLE border="0" width="80%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">

	<TR align="Center">

		<TH ALIGN="right" width="15%">Product ID : &nbsp;&nbsp;</TH>
		<TD ALIGN="left"><INPUT name="PRODUCTID" type="TEXT"
			value="<%=item%>" size="20" MAXLENGTH=20> <a href="#"
			onClick="javascript:popUpWin('list/labellistcatalog.jsp?ITEM='+form.PRODUCTID.value);"><img
			src="images/populate.gif" border="0"></a></TD>
        <TH ALIGN="right" width="15%">Product Description : &nbsp;&nbsp; </TH>
		<TD  align="left"><INPUT name="DESCR" type="TEXT"
			value="<%=descr%>" size="20" MAXLENGTH=80> <a href="#"
			onClick="javascript:popUpWin('list/labellistcatalog.jsp?DESCR='+form.DESCR.value);"><img
			src="images/populate.gif" border="0"></a></TD>
		
			
			<TH ALIGN="right" width="15%"> &nbsp;&nbsp;Product Class ID : &nbsp;&nbsp;  </TH>
          <TD  ALIGN="left"><INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('PrdClsIdList.jsp?PRD_CLS_ID='+form.PRD_CLS_ID.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
	</TR>
	 <TR>
          
          <TH ALIGN="right" width="15%"> &nbsp;&nbsp;Product Type ID : &nbsp;&nbsp;  </TH>
          <TD ALIGN="left"><INPUT name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('productTypeIdList.jsp?ITEM_ID='+form.PRD_TYPE_ID.value);"><img src="images/populate.gif" border="0"/>     
          </TD>
           <TH ALIGN="right" width="15%"> &nbsp;&nbsp;Product Brand ID : &nbsp;&nbsp;  </TH>
          <TD  ALIGN="left"><INPUT name="PRD_BRAND_ID"  type = "TEXT" value="<%=PRD_BRAND_ID%>" size="20"  MAXLENGTH=20>
          
           <a href="#" onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form');">           
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
          <TD ALIGN="center" width="10%"><input type="button" value="View"
			align="left" onClick="javascript:return onGo();"></TD>
        </TR>
        <input type="hidden" name="PRD_CLS_DESC" value="">
            <input type="hidden" name="PRD_CLS_ID1" value="">
            <input type="hidden" name="PRD_TYPE_DESC" value="">
            <input type="hidden" name="PRD_TYPE_ID1" value="">
           <input type="hidden" name="PRD_BRAND_DESC" value="">
            <input	type="hidden" name="PRICE">
			</input> <input type="hidden"name="DESCRIPTION1"></input>
           <INPUT name="ACTIVE" type = "hidden" value="">
            <INPUT type="Hidden" name="DIRTYPE" value="CATALOG">
             <INPUT type="Hidden" name="REFNO" value=<%=REFNO%>>
</TABLE>
<br>
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
		<TH><font color="#ffffff" align="left"><b>Chk</TH>
         <TH><font color="#ffffff" align="left"><b>Catalog Image</TH>
          <TH><font color="#ffffff" align="left"><b>ProductID</TH>
             <TH><font color="#ffffff" align="left"><b>Product Description</TH>
	      <TH><font color="#ffffff" align="left"><b>UOM</TH>
	</TR>
	<%
	 ItemMstDAO itemmstdao=new ItemMstDAO();
		for (int iCnt = 0; iCnt < locQryList.size(); iCnt++) {
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#dddddd";

			Map lineArr = (Map) locQryList.get(iCnt);
			imageFile = StrUtils.fString((String)lineArr.get(IDBConstants.CATLOGPATH));
			
		    String	prodid= (String) lineArr.get(IDBConstants.PRODUCTID);
		    String	ItemDesc= (String) lineArr.get(IConstants.DESCRIPTION1);
		    String Uom=itemmstdao.getItemUOM(PLANT,prodid);
		    String lineno=String.valueOf(iIndex)+"_"+prodid;
			chkString  =imageFile+",,,"+prodid+",,,"+ItemDesc+",,,"+Uom;
			
	%>

	<TR bgcolor="<%=bgcolor%>">
		<TD width="5%" align="CENTER"><font color="black"><INPUT Type=checkbox style="border: 0;" name="chkdLnNo" 	value="<%=chkString%>" ></font></TD>

		<TD align="center" >
         <a href="#" onClick="javascript:popUpMobWin('catalogBrowseImage.jsp?PRODUCTID=<%=prodid%>&PLANT=<%=PLANT%>');"><img src="/track/ReadFileServlet/?fileLocation=<%=imageFile%>" width="62" height="50"/></a>
		</TD>
		<input type="hidden" name = "imageFile_<%=lineno%>" id = "imageFile_<%=lineno%>"   value = <%=imageFile%> ></input>
		
		<TD align="center" >&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;<a href="sumryCatalogDetails.jsp?PLANT=<%=PLANT%>&PRODUCTID=<%=prodid%>&PAGE=view"> 
		<%=strUtils.fString((String)lineArr.get(IDBConstants.PRODUCTID))%></a></TD>
		<input type="hidden" name = "prodid_<%=lineno%>" id = "prodid_<%=lineno%>"   value = <%=prodid%> ></input>
			 
		<TD align="center" >&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=ItemDesc%></TD>
		<input type="hidden" name = "ItemDesc_<%=lineno%>" id = "ItemDesc_<%=lineno%>"   value = <%=ItemDesc%> ></input>
		
		<TD align="center" >&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=Uom%></TD>
		<input type="hidden" name = "Uom_<%=lineno%>" id = "IUom_<%=lineno%>"   value = <%=Uom%> ></input>
		
	</TR>
	<%
		}
	%>
</TABLE>
<br></br>
  <table align="center" >
     <TR>
      <td>  <input type="button" value="Back" onClick="window.location.href='LabelPrintMenu.jsp'">&nbsp; </td>
   <td>   <input type="button" value="Generate Label" onClick="onPrint();" >&nbsp;  </td>
   <td><input type = "submit" value= "Prev" onclick="return onPrev();" <%=isDisabled%> >&nbsp;

   <input type = "submit" value= "Next" onclick="return onNext();" <%=isDisabled%> >&nbsp;
           Page :<%=curPage%>
             <input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >&nbsp;&nbsp;
	    /&nbsp;&nbsp;
            <%=totalPages%>&nbsp;&nbsp;
   </td>
   </TR>
  
    </table>
</FORM>
<script>
function popUpMobWin1(productID) {
	
	
	var companyID = "<%=PLANT%>";
	
	var catalogURL ="catalogBrowseImage.jsp?PRODUCTID="+productID+"&PLANT="+companyID; 
	
 	subWin = window.open(catalogURL, 'Catalog', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=350,height=500,left = 420,top = 140');
}


</SCRIPT>
<%@ include file="footer.jsp"%>