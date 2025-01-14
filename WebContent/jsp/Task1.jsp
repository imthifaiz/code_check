<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	_userBean.setmLogger(mLogger);
	List locQryList  = new ArrayList();
	String fieldDesc="";
	String PLANT="",ITEM ="",ITEM_DESC="",PRD_CLS_ID="",PRD_TYPE_ID="";
	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	session=request.getSession();
	String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	ITEM     = strUtils.fString(request.getParameter("ITEM"));
	ITEM_DESC = strUtils.fString(request.getParameter("ITEM_DESC"));
        PRD_CLS_ID =  strUtils.fString(request.getParameter("PRD_CLS_ID"));
        PRD_TYPE_ID =  strUtils.fString(request.getParameter("PRD_TYPE_ID"));
        String currentPage        = strUtils.fString(request.getParameter("cur_page"));
         int curPage      = 1;
    int recPerPage   = 100;
    int totalRec     = 0;
    String isDisabled ="disabled";
   System.out.println("currentPage :: "+currentPage);
	ItemUtil itemUtil = new ItemUtil();
	itemUtil.setmLogger(mLogger);
	if(PGaction.equalsIgnoreCase("View")){
	 try{
             if(ITEM.length()>0){
             ITEM_DESC ="";
             }
	      Hashtable ht = new Hashtable();
	      if(strUtils.fString(PLANT).length() > 0)
	      ht.put("PLANT",PLANT);
	      locQryList= itemUtil.queryItemMstForSearchCriteria(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PLANT,"");
              
	      if(locQryList.size()== 0)
	      {
	        fieldDesc="Data Not Found";
	      }
    if (locQryList != null)  {         totalRec = locQryList.size();
     isDisabled="";
     }
     if (currentPage.length() > 0)
     {
     
    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
    catch (Exception e)   {   curPage = 1;                                      }
    }
     
	 }catch(Exception e) { }
         }else if(PGaction.equalsIgnoreCase("ViewCommonChem")){
	 try{
	      Hashtable ht = new Hashtable();
	      if(strUtils.fString(PLANT).length() > 0)
	      ht.put("PLANT",PLANT);
	      locQryList= itemUtil.queryItemMstForSearchCriteria("","","","",PLANT," AND ISNULL(USERFLG5,'') ='Y'");
	      if(locQryList.size()== 0)
	      {
	        fieldDesc="Data Not Found";
	      }
  if (locQryList != null)  {         totalRec = locQryList.size();
     isDisabled="";
     }
     if (currentPage.length() > 0)
     {
     
    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
    catch (Exception e)   {   curPage = 1;                                      }
    }
	 }catch(Exception e) { }
}


    int totalPages = (totalRec + recPerPage -1)/recPerPage;
    if (curPage > totalRec) curPage = 1;                    // Out of range

    System.out.println("totalPages :: "+totalPages);
    System.out.println("curPage :: "+curPage);
%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
  var cur_page      = <%=curPage%>;                            // Current display page
    var total_pages   = <%=totalPages%>;                         // The total number of records

    // Display previous page of user list
    function onPrev()
    {
	if (cur_page <= 1)  return;
	cur_page = parseInt(cur_page) -1;
	document.form.cur_page.value = cur_page;
       
          document.form.action = "Task1.jsp";
	document.form.submit();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Display next page of user list
    function onNext()
    {
  
    cur_page = parseInt(cur_page) + 1;
    document.form.cur_page.value = cur_page;
    document.form.action = "Task1.jsp";
    document.form.submit();
    }
var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
  
   function popUpPdf(URL) {
	 	//subWin = window.open(URL, 'SDS', 'toolbar=0,fullscreen=yes, scrollbars=auto');\
                subWin = window.open(URL, 'SDS', 'toolbar=yes,location=0,statusbar=0,menubar=yes,dependant=1,resizable=1,width='+screen.availWidth+',height='+screen.availHeight+',left=0,top=0');
	}
 
function onGo(){
document.form.PGaction.value ="View"
document.form.cur_page.value = 1
  document.form.submit();
}
function onViewCommonChemicals(){
document.form.PGaction.value ="ViewCommonChem"
document.form.cur_page.value = 1
document.form.submit();

}
</script>
<title>Product Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">


<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="Task1.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="<%=PGaction%>">

<INPUT name="DESC" type = "hidden" value="" >
  <INPUT name="ARTIST" type = "hidden" value="" >
  <INPUT name="UOM" type = "hidden" value="" >
  <INPUT name="TITLE" type = "hidden" value="" >
  <INPUT name="MEDIUM" type = "hidden" value="" >
  <INPUT name="ITEM_CONDITION" type = "hidden" value="" >
  <INPUT name="REMARKS" type = "hidden" value="" >
  <INPUT name="STKQTY" type = "hidden" value="" >
   <INPUT name="PRICE" type = "hidden" value="" >
   <INPUT name="COST" type = "hidden" value="" >
   <INPUT name="DISCOUNT" type = "hidden" value="" >
   

  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Product Summary</font></TH>
    </TR>
  </TABLE>
    <br>
  <center>
   <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" >
      <font class="mainred"> <%=fieldDesc%></font>
   </table>
   </center>
  <TABLE border="0" width="75%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  <TR align="Center">
         <TH ALIGN="right" width="15%">Hazard Code </TH>
		  <td ALIGN="right" width="5%"></td>
         <TD width="25%" ALIGN="left" ><INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="20"  MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( document.form.PRD_CLS_ID.value.length > 0)){onGo();}">
       <a href="#" onClick="javascript:popUpWin('list/ListProductClass.jsp?PRD_CLS_ID='+form.PRD_CLS_ID.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
          <TH ALIGN="right" width="15%">Product Type </TH>
	  <td ALIGN="right" width="5%"></td>
         <TD width="25%" ALIGN="left" ><INPUT name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="20"  MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( document.form.PRD_TYPE_ID.value.length > 0)){onGo();}">
             <a href="#" onClick="javascript:popUpWin('list/ListProductType.jsp?PRD_TYPE_ID='+form.PRD_TYPE_ID.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
         <TD ALIGN="center" width="5%"></TD>
        </TR>
   <TR align="Center">
         <TH ALIGN="right" width="15%">Product ID </TH>
		 <td ALIGN="right" width="5%"></td>
         <TD width="25%" ALIGN="left" ><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){onGo();}">
          <a href="#" onClick="javascript:popUpWin('view_item_list.jsp?ITEM='+form.ITEM.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
          <TH ALIGN="right" width="15%">Description </TH>
		  <td ALIGN="right" width="5%"></td>
         <TD width="25%" ALIGN="left" ><INPUT name="ITEM_DESC" type = "TEXT" value="<%=strUtils.forHTMLTag(ITEM_DESC)%>" size="20"  MAXLENGTH=100 onkeypress="if((event.keyCode=='13') && ( document.form.ITEM_DESC.value.length > 0)){onGo();}">
          <a href="#" onClick="javascript:popUpWin('view_item_list.jsp?ITEM_DESC='+form.ITEM_DESC.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
         
            <TD ALIGN="center" width="5%"> </td>
           </TR>
        
	<TR align="Center">
         <TH ALIGN="right" width="15%"> </TH>
		 <td ALIGN="right" width="5%"></td>
     
          <TH ALIGN="right" width="15%"> <input type="button" value="View Common Chemical & Gases"   align="left" onClick="javascript:return onViewCommonChemicals();"></TH>
		  <td ALIGN="right" width="5%"></td>
         <TD width="25%" ALIGN="left" ><input type="button" value="View"   align="left" onClick="javascript:return onGo();"> </TD>
         
            <TD ALIGN="center" width="5%"> 
            
           </TD>
        </TR>
        
	
  </TABLE>
  </TABLE>
  
  <br>
 <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
     <TR BGCOLOR="#000066">
          <TH><font  align="center" color="#ffffff">S/N</TH>
          <TH><font color="#ffffff" align="left"><b>Product ID</TH>
           <TH><font color="#ffffff" align="left"><b>Description</TH>
           <TH><font color="#ffffff" align="left"><b>Hazard Code</TH>
           <TH><font color="#ffffff" align="left"><b>Product Type</TH>
           <TH><font color="#ffffff" align="left"><b>Brand</TH>
           <TH><font color="#ffffff" align="left"><b>Catalog No</TH>
           <TH><font color="#ffffff" align="left"><b>UOM</TH>
            <TH><font color="#ffffff" align="left"><b>IsActive</TH>
      </TR>
   	  <%
       int start = (curPage - 1) * recPerPage;
	int end = start + recPerPage;
	if (locQryList == null) end = 0;
	else if (end >= locQryList.size()) end = locQryList.size();
	if (end == 0){
	 fieldDesc = "<font class=\"mainred\">No Records Found for this Criteria</font>";
	}
         int iIndex=0;
           for (int iCnt =start; iCnt<end; iCnt++){
            
                     iIndex = iIndex + 1;
                   
	            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	            Vector vecItem   = (Vector)locQryList.get(iCnt);
                  
	           
       %>
          <TR bgcolor = "<%=bgcolor%>">
	            <TD align="center"><%=iCnt+1%></TD>
                    <TD align="left" > &nbsp;<font class="textbold"><%=(String)vecItem.get(0)%></a></font></TD>
	    	    <TD align="left" class="textbold">&nbsp; <%=(String)vecItem.get(1)%></TD>
                     <TD align="center" class="textbold">&nbsp; <%=strUtils.fString((String)vecItem.get(2))%></TD>
                      <TD align="center" class="textbold">&nbsp; <%=strUtils.fString((String)vecItem.get(3))%></TD>
	    	    <TD align="center" class="textbold">&nbsp; <%=strUtils.fString((String)vecItem.get(4))%></TD>
                    
			<TD align="center" class="textbold">&nbsp; <%=strUtils.fString((String)vecItem.get(16))%></TD>                    
	
	            <TD align="left" class="textbold">&nbsp; <%=strUtils.fString((String)vecItem.get(8))%></TD>
	              <TD align="left" class="textbold">&nbsp; <%=strUtils.fString((String)vecItem.get(14))%></TD>
                     
          </TR>
          <% if (iIndex==10){iIndex=0; System.out.println("index :"+iIndex);%> </TABLE><TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center"><%}%>
       <%}  System.out.println("Jsp table List at time :"+ new DateUtils().getDateAtTime());%>
      
       
        <br>
       <table align="center" >
     <TR>
    <td>  <input type="button" value="        Back      " onClick="window.location.href='../home'">&nbsp; </td>
    <td><input type = "submit" value= "Prev" onclick="return onPrev();" <%=isDisabled%> >&nbsp;
    <input type = "submit" value= "Next" onclick="return onNext();" <%=isDisabled%> >&nbsp;
    <input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >&nbsp;&nbsp;
   </td>
   </TR>

    </table>
    
  </FORM>
<%@ include file="footer.jsp"%>