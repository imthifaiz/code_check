<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.math.BigDecimal"%>
<%@ include file="header.jsp"%>

<%
    String PLANT = (String)session.getAttribute("PLANT");
    String CRBY = (String)session.getAttribute("LOGIN_USER");

    if(PLANT == null) PLANT = "SIS";
    StrUtils strUtils         = new StrUtils();
    ReportUtil reportUtil     = new ReportUtil();
    DateUtils dateUtil    = new DateUtils();
    CCUtil ccUtil             = new CCUtil();
    UserTransaction ut = null;
    String currentPage        = strUtils.fString(request.getParameter("cur_page"));
    String fromLoc            = strUtils.fString(request.getParameter("from_loc"));
    String toLoc              = strUtils.fString(request.getParameter("to_loc"));


//############### INVENTORY UPDATE STARTS HERE  ###############

String action = "";
String res    = "";
String btnEnb = "";

try{
    action = strUtils.fString(request.getParameter("action"));
    if(action.equalsIgnoreCase("Update Stock")){
                try{
                 ut = DbBean.getUserTranaction();
                 }catch(Exception e){}
                  boolean flg  = false;
                  try{
                  ut.begin();

                  flg  = reportUtil.updateInventoryWithStockTake4Loc(fromLoc,toLoc);
                  }catch(Exception e){flg= false;}
                  if(flg == true){
                   try{ut.commit();}catch(Exception e){ }

                   res = "<font class=\"maingreen\">Success to update Stocktake with Inventory</font>";
                  }
                  else if (flg== false){
                  try{ut.rollback();}catch(Exception e){ }
                  res = "<font class=\"mainred\">Failed to update Stocktake with Inventory</font>";
                  }
      }

//System.out.println(" res : " + res);
}catch(Exception e){
   e.printStackTrace();
}
//############### INVENTORY UPDATE END HERE  ###############

    int ix=0;
    String filter="";

    try{
     filter = request.getParameter("filter");
     if(filter==null) {
       ix=0;
     }else{
       if(filter.equalsIgnoreCase("0"))ix=0;
       if(filter.equalsIgnoreCase("1"))ix=1;
     }
    }catch(Exception e){}


    int curPage      = 1;
    int recPerPage   = 20;
    int totalRec     = 0;
    List listRec = reportUtil.reportOnPhysicalStockTake2Inv(ix,fromLoc,toLoc);
    if (listRec != null)           totalRec = listRec.size();
    if (currentPage.length() > 0)
    {
	try                   {   curPage = (new Integer(currentPage)).intValue();  }
	catch (Exception e)   {   curPage = 1;                                      }
    }
    int totalPages = (totalRec + recPerPage -1)/recPerPage;
    if (curPage > totalRec) curPage = 1;                    // Out of range


%>
<html>
<title>Stock Take Report Generation</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">
<!-- Begin
    var cur_page      = <%=curPage%>;                            // Current display page
    var total_pages   = <%=totalPages%>;                         // The total number of records

    // Display previous page of user list
    function onPrev()
    {
	if (cur_page <= 1)  return;
	cur_page = parseInt(cur_page) -1;
	document.form1.cur_page.value = cur_page;
	document.form1.submit();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Display next page of user list
    function onNext()
    {
	if (cur_page >= total_pages)  return;
	cur_page = parseInt(cur_page) + 1;
	document.form1.cur_page.value = cur_page;
	document.form1.submit();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Display specified page of user list
    function onGo()
    {
	var nPage = document.form1.cur_page.value;
	nPage = parseInt(nPage);
	if (nPage < 1 || nPage > total_pages)
	{
	  alert("The page number is out of range"); return false;
	}
	document.form1.cur_page.value = nPage;
	document.form1.submit();
    }
    function updateStock(){
     var con = confirm("Are you sure want to Update the Stock ?");
     if(con){
     document.form1.from_loc.value = <%=fromLoc%>
     document.form1.to_loc.value   = <%=toLoc%>
	 document.form1.action = "stockTake_report.jsp?action =Update Stock";
	 document.form1.submit();
      }else{
         return false;
      }
    }
    function onView()
    {
    var fLoc = document.form1.from_loc.value;
    var tLoc = document.form1.to_loc.value;
    if(parseInt(tLoc) < parseInt(fLoc)) { alert("Please,Enter Start Loc Greater than To Loc");
	document.form1.from_loc.focus()
	return false;}
    if (isNaN( parseInt(fLoc))) {alert("Please enter Loc as a number."); document.form1.from_loc.focus();
        return false;}
    if (isNaN( parseInt(tLoc))) {alert("Please enter Loc as a number."); document.form1.to_loc.focus();
        return false;}
    document.form1.action = "stockTake_report.jsp";
    document.form1.submit();
    }




-->
</script>



<%@ include file="body.jsp"%>
<form method = "post" name = "form1">
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">REPORT - STOCKTAKE Vs WMS</font></TH>
     </TR>
  </table>
  <br>
  <table  width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr> <td width="100%"> <TABLE WIDTH="80%"  border="0" cellspacing="0" cellpadding = "3" align="center">
	<td>
	<a href="stockTake_report.jsp?filter=0">Show All</a>
   &nbsp;&nbsp;&nbsp;
   <a href="stockTake_report.jsp?filter=1">Show Discrepancy Records</a>
   &nbsp;&nbsp;&nbsp;
      From Location :
      <input type="text" name="from_loc" size="4"  value="<%=fromLoc%>">
      To :
      <input type="text" name="to_loc" size="4"  value="<%=toLoc%>">&nbsp;
      <input type="submit"  value = "View" onclick="return onView();">&nbsp;&nbsp;
	  </td>
<td>
	   <input type = "submit" value= "Prev" onclick="return onPrev();">&nbsp;
              <input type = "submit" value= "Next" onclick="return onNext();">&nbsp;
              Page :
             <input type="text" name="cur_page" size="4" maxlength="4" value="<%=curPage%>">&nbsp;&nbsp;
	     /&nbsp;&nbsp;
            <%=totalPages%>&nbsp;&nbsp;

            <input type = "submit" value= "Go" onclick="return onGo();">
</td></table>
	</td></tr>
	<tr>
      <td width="100%">
       <TABLE WIDTH="80%"  border="0" cellspacing="0" cellpadding = "3" align="center">
         <tr bgcolor="navy">
            <th><font color="#ffffff">S NO</th>
            <th><font color="#ffffff">ITEM</th>
            <th><font color="#ffffff">DESCRIPTION</th>
            <th><font color="#ffffff">LOC</th>
            <th><font color="#ffffff">WMS QTY</th>
            <th><font color="#ffffff">STOCK TAKE QTY</th>
            <th><font color="#ffffff">QTY DIFFERENCE</th>
       </tr>
      <%
	int start = (curPage - 1) * recPerPage;
	int end = start + recPerPage;
	if (listRec == null) end = 0;
	else if (end >= listRec.size()) end = listRec.size();
	if (end == 0){
	 res = "<font class=\"mainred\">No Records Found for this Criteria</font>";
	}
        String bgcolor="";
	for (int index = start; index < end; index ++)
	{
               bgcolor = ((index == 0) || (index % 2 == 0)) ? "#FFFFFF" : "#dddddd";
               Vector vec_report = (Vector)listRec.get(index);
               String item     = (String) vec_report.get(0);
               String desc     = (String) vec_report.get(1);
                String bloc    = (String) vec_report.get(2);
               String qty_inv  = (String) vec_report.get(3);
               String qty_stk  = (String) vec_report.get(4);
               String qty_diff = (String) vec_report.get(5);
    %>
         <tr bgcolor ="<%=bgcolor%>">
             <td align="center"><%=index+1%></td>
             <td><%=item%></td>
             <td><%=desc%></td>
             <td align="center"><%=bloc%></td>
             <td align=right><%=qty_inv%></td>
             <td align = right><%=qty_stk%></td>
             <td align = right><%=qty_diff%></td>
    <%}   %>
       <tr><td colspan= 10>&nbsp;</td></tr>
       <tr><td colspan= 10><center><b><%=res%></b></td></tr>
       </TABLE>
       </center>
        </td>
    </tr>
  </table>
<br>
<center>
<input type="submit" name ="action" value="Update Stock" <%=btnEnb%> onclick="return updateStock();">
</form>
</center>
</body>
</html>

<%@ include file="footer.jsp"%>
