<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>
<html>
  <head>
  <script language="javascript">
 function onLabelPrintProduct()
    {
      var frmRoot=document.form;
      frmRoot.action = "LabelPrintProduct.jsp?action=new" ;
      frmRoot.submit();
      return true;
  }
    
function onLabelPrintProductWithBatch()
{
  var frmRoot=document.form;
  frmRoot.action = "LabelPrintProductWithBatch.jsp?action=new" ;
  frmRoot.submit();
  return true;
}
function onLabelPrintManual()
{
  var frmRoot=document.form;
  frmRoot.action = "LabelPrintManual.jsp?action=new" ;
  frmRoot.submit();
  return true;
}
function onLabelPrintGoodsIssue()
{
  var frmRoot=document.form;
  frmRoot.action = "LabelPrintGoodsIssue.jsp?action=new" ;
  frmRoot.submit();
  return true;
}
function onLabelPrintLocation()
{
  var frmRoot=document.form;
  frmRoot.action = "LabelPrintLoc.jsp?action=new" ;
  frmRoot.submit();
  return true;
}
function onLabelPrintEmployee()
{
  var frmRoot=document.form;
  frmRoot.action = "LabelPrintEmployee.jsp?action=new" ;
  frmRoot.submit();
  return true;
}
function onLabelPrintCatalog()
{
  var frmRoot=document.form;
  frmRoot.action = "LabelPrintCatalog.jsp?action=new" ;
  frmRoot.submit();
  return true;
}
  
  
</script>
    <title>Barcode Label Print Menu</title>
  </head>
  <link rel="stylesheet" href="css/style.css"></link>
  <%
  


MLogger logger = new MLogger();
String fieldDesc="";
String msg="";

String PLANT              = (String)session.getAttribute("PLANT");
String _login_user        = (String)session.getAttribute("LOGIN_USER");
String action         = StrUtils.fString(request.getParameter("action")).trim();



 ArrayList alRes = null;
 System.out.println("Action *** :" + action);


   
%>
  <%@ include file="body.jsp"%>
  <form name="form" method="post" enctype="multipart/form-data" action="LabelPrintMenu.jsp">
 
    <input type="hidden" value="" name="download"></input>
    <br></br><br></br>
    <table border="0" width="100%" cellspacing="0" cellpadding="0"
           align="center" bgcolor="#dddddd">
      <tr>
        <th bgcolor="#000066" colspan="11">
          <font color="#ffffff">Barcode Label Print Menu </font>
        </th>
      </tr>
    </table>
    
    <br></br>
   
      <table border="0" width="60%" cellspacing="0" cellpadding="0" align="center"      bgcolor="#dddddd">
      <tr rowspan =2></tr>
      <tr>
        <td align =center>
          <input type="button" name="Submit"  value="Label Product ID & Description"    onclick="javascript:return onLabelPrintProduct();"/>
        </td>

		<td align =center>
          <input type="button" name="Submit"  value="Label Product ID & Description + Batch No"    onclick="javascript:return onLabelPrintProductWithBatch();"/>
        </td>

         <td align =Center>
          <input type="button" name="Submit"  value="Label Product ID & Description + Batch No (manual)"     onclick="javascript:return onLabelPrintManual();"/>
        </td>
        
        
       </tr>
       
       </table>
       <br></br>
       <!-- <table border="0" width="60%" cellspacing="0" cellpadding="0" align="center"      bgcolor="#dddddd">
      <tr rowspan =2></tr>
      <tr>
        <td align =center>
          <input type="button"  style="width:180px" name="Submit" value="Label Location"    onclick="javascript:return onLabelPrintLocation();"/>
        </td>

		<td align =center>
          <input type="button"  style="width:250px" name="Submit" value="Label Employee"    onclick="javascript:return onLabelPrintEmployee();"/>
        </td>

         <td align =center>
          <input type="button"  style="width:300px" name="Submit" value="Label Catalog"     onclick="javascript:return onLabelPrintCatalog();"/>
        </td>
        
        
       </tr>
      
       
       </table>-->
      <br></br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value="Back"   onclick="window.location.href='../home'" ></input><td>  
   </tr>
   </table>

  </form>
  </tr>
  </html>
  <%@ include file="footer.jsp"%>
  <% MLogger.log(-1, "_______________________ " + this.getClass() + ""); %>
</html>