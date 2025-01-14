<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.math.BigDecimal"%>
<%@ include file="header.jsp"%>

<%
    String PLANT = (String)session.getAttribute("PLANT");
    String CRBY = (String)session.getAttribute("LOGIN_USER");

    if(PLANT == null) PLANT = "COM";
    StrUtils strUtils         = new StrUtils();
    ReportUtil reportUtil     = new ReportUtil();
    DateUtils dateUtil    = new DateUtils();
    CCUtil ccUtil             = new CCUtil();
    UserTransaction ut = null;
    String currentPage        = strUtils.fString(request.getParameter("cur_page"));
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
    List listRec = reportUtil.reportOnCycleCnt2Inv(ix);
    if (listRec != null)           totalRec = listRec.size();
    if (currentPage.length() > 0)
    {
    try                   {   curPage = (new Integer(currentPage)).intValue();  }
    catch (Exception e)   {   curPage = 1;                                      }
    }
    int totalPages = (totalRec + recPerPage -1)/recPerPage;
    if (curPage > totalRec) curPage = 1;                    // Out of range


   //############### INVENTORY UPDATE STARTS HERE  ###############

   String action="";
   String res = "";
   String strEnb = "";
   try{
   /*if(reportUtil.CanUpdateCycleCount(true) == true)
     { strEnb = "enabled"; }
  else { strEnb ="disabled"; }*/
  action = strUtils.fString(request.getParameter("updateStock"));
  if(action.equalsIgnoreCase("Update Stock")){
	/*  if(reportUtil.CanUpdateCycleCount(true) == false){
            res = "<font class = \"mainred\">Updated Already</font>";
      }
      else {*/
          try{ut= DbBean.getUserTranaction();}catch(Exception e){}
          boolean flg= false;
          try{
          ut.begin();
          flg = reportUtil.updateInventoryWithStockTake(true);
           }catch(Exception e){flg= false;}

          if(flg == true) {
          try{ut.commit();}catch(Exception e){ }
          res = "<font class = \"maingreen\">Success to update Cycle Count with Inventory</font>";
          } else if (flg == false){
          try{ut.rollback();}catch(Exception e){ }
          res = "<font class = \"mainred\">Failed to update Cycle Count with Inventory</font>";
         /* }
          if(reportUtil.CanUpdateCycleCount(true) == true)
               {  strEnb = "enabled"; }
          else {  strEnb ="disabled";  }*/
      }
 }

System.out.println(" res : " + res);
}catch(Exception e){
   e.printStackTrace();
}


%>
<html>
<title>Cycle Count Report</title>
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
	if ((nPage < 1) ||( nPage > total_pages))
	{
	  alert("The page number is out of range"); return false;
	}
	document.form1.cur_page.value = nPage;
	document.form1.submit();
    }
    function updateStock(){
    return confirm ("Are you sure want to Update the Stock ?");

	}

    function onExport(){
	document.form1.action = "report_export.jsp"
	document.form1.submit();
    }

</script>



<%@ include file="body.jsp"%>
<form method = "post" name = "form1">
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">REPORT - CYCLECOUNT Vs WMS</font></TH>
     </TR>
  </table>
  <br>
  <table  width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">
       <TABLE WIDTH="80%"  border="0" cellspacing="0" cellpadding = "3" align="center">
       <tr>
<td COLSPAN=4>
    <a href="cycleCount_report.jsp?filter=0">Show All</a>
   &nbsp;&nbsp;&nbsp;
   <a href="cycleCount_report.jsp?filter=1">Show Discrepancy Records</a>
</td>

	<td align="right" colspan = 6>
              <input type = "submit" value= "Prev" onclick="return onPrev();">&nbsp;
              <input type = "submit" value= "Next" onclick="return onNext();">&nbsp;
              Page :
             <input type="text" name="cur_page" size="4" maxlength="4" value="<%=curPage%>">&nbsp;&nbsp;
	     /&nbsp;&nbsp;
            <%=totalPages%>&nbsp;&nbsp;

            <input type = "submit" value= "Go" onclick="return onGo();">
        </td>
        </tr>

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
</form>
<br>
<form action="" method="post">
<center>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="submit" name ="updateStock" value="Update Stock"  <%=strEnb%>>

</center>
</form>
</body>
</html>

<%@ include file="footer.jsp"%>
