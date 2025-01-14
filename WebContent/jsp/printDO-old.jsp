<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.DateUtils"%>
<%@ page import="net.sf.jasperreports.engine.*"%>
<%@ page import="net.sf.jasperreports.engine.JasperPrintManager"%>
<%@ page import="javax.print.PrintService"%>
<%@ page import="javax.print.PrintServiceLookup"%>
<%@ page import="net.sf.jasperreports.engine.export.JRPrintServiceExporter"%>
<%@ page import="net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter"%>
<%@ page import="java.awt.print.PrinterJob"%>
<%@ page import="com.track.constants.*"%>
<%@ page import=" net.sf.jasperreports.engine.*,
            net.sf.jasperreports.engine.design.JasperDesign,
            net.sf.jasperreports.engine.design.JRDesignQuery,
            net.sf.jasperreports.engine.xml.JRXmlLoader,
            net.sf.jasperreports.engine.export.*"
%>
<%@ include file="header.jsp"%>
<html>
  <head>
    <script language="javascript">
var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'List', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
function onView(){
     var flag    = "false";
     var DONO    = document.form1.DONO.value;
     if(DONO != null    && DONO != "") { flag = true;}
     if(flag == "false"){ alert("Please Enter Order No"); return false;}
     document.form1.cmd.value="View" ;
     document.form1.action="printDO-old.jsp";
     document.form1.submit();
}
function onRePrint(){
    var flag    = "false";
    var DONO    = document.form1.DONO.value;
    if(DONO != null    && DONO != "") { flag = true;}
    if(flag == "false"){ alert("Please Enter Order No"); return false;}
    document.form1.action="/track/DynamicFileServlet?action=printDOWITHBATCH&DONO="+DONO;
    document.form1.submit();
}
function onRePrintWithContainer(){
    var flag    = "false";
    var DONO    = document.form1.DONO.value;
    if(DONO != null    && DONO != "") { flag = true;}
    if(flag == "false"){ alert("Please Enter Order No"); return false;}
    document.form1.action="/track/DynamicFileServlet?action=printDOWITHBATCHANDCONTAINER&DONO="+DONO;
    document.form1.submit();
}
function onRePrintWithOutBatch(){
    var flag    = "false";
    var DONO    = document.form1.DONO.value;
    if(DONO != null    && DONO != "") { flag = true;}
    if(flag == "false"){ alert("Please Enter Order No"); return false;}
    document.form1.action="/track/DynamicFileServlet?action=printDOWITHOUTBATCH&DONO="+DONO;
    document.form1.submit();
}
</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
  <title>Outbound Order Details</title>
  </head>
  <link rel="stylesheet" href="css/style.css"/>
  <% 
        String STATUS="";
        StrUtils strUtils     = new StrUtils();
        ArrayList QryList  = new ArrayList();
        DOUtil doUtil = new DOUtil();
        ShipHisDAO _ShipHisDAO = new ShipHisDAO();
        Map defMap = new HashMap();
        String PLANT = (String)session.getAttribute("PLANT");
        String USER  = (String)session.getAttribute("LOGIN_USER");
        String cmd   = strUtils.fString(request.getParameter("cmd")).trim();
        STATUS=strUtils.fString(request.getParameter("STATUS")).trim();
        String TRANDT="",DONO="",result="",  chkString= "";
        
        DONO    = strUtils.fString(request.getParameter("DONO"));
        STATUS   = strUtils.fString(request.getParameter("STATUS"));
        double pickQty = 0;String btnDisabled="disabled",btnContainerDisabled="disabled";
        
       
        
    
     if(cmd.equalsIgnoreCase("View")){
        try{
            QryList=  doUtil.listDODetilstoPrintNew(PLANT,DONO,"","");
	      
        }catch(Exception e){
        System.out.println("Exception :: View : "+e.toString());
       result =  "<tr><td><font class=mainred><B><h4><centre>" + e.getMessage() + "!</centre></b></font><h4></td></tr>";
        }
        }      
  %>
    <%@ include file="body.jsp"%>
    <FORM name="form1" method="post" action="printDO-old.jsp">
    <INPUT type="hidden" name="cmd" value=""/>
     <br/>
    <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11">
          <font color="white">Outbound Order Details</font>
        </TH>
      </TR>
        <INPUT type="hidden" name="STATUS" id="STATUS" value="<%=STATUS%>">
    </TABLE>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    
        <table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#dddddd">
        <tr>
          <td>
             <table cellspacing="0" cellpadding="0" border="0" width="80%" align="center">
          <TR>
            <TH ALIGN="RIGHT" >OutBound Order No:&nbsp;&nbsp;</TH>
            <TD><INPUT name="DONO" type = "TEXT" value="<%=DONO%>" size="20"  >&nbsp;
           <a href="#" onClick="javascript:popUpWin('list/ob_order_list.jsp?DONO='+form1.DONO.value+'&STATUS='+form1.STATUS.value);"><img src="images/populate.gif" border="0"></a>
           
           

              &nbsp;&nbsp; <input type="button" value="View" onClick="javascript:return onView();"/></TD>
                  </tr>
            </table>
          </td>
        </tr>
      </table>
      <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
        <TR bgcolor="navy">
           <th align="left"><font color="#ffffff" >Line No</th>
           <td align="left"><font color="#ffffff" ><b>Product ID</b></font></td>
           <td align="left"><font color="#ffffff" ><b>Description</b></font></td>
           <td align="left"><font color="#ffffff"><b>Order Qty</b></font></td>
           <td align="left"><font color="#ffffff" ><b>Pick Qty</b></font></td>
           <td align="left"><font color="#ffffff" ><b>UOM</b></font></td>
        </TR>
   <%
          for (int iCnt =0; iCnt<QryList.size(); iCnt++){
          Map lineArr = (Map) QryList.get(iCnt);
          int iIndex = iCnt + 1;
          String pickedQty = (String)lineArr.get("PICKQTY");
          pickQty = pickQty + Double.parseDouble(pickedQty);
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
             %>
       <TR bgcolor = <%=bgcolor%>>
            <TD align="left"> <%=(String)lineArr.get("DOLNO")%></TD>
            <TD align="left"><%=(String)lineArr.get("ITEM")%></TD>
            <TD align="left"><%=(String)lineArr.get("ITEMDESC")%></TD>
            <TD align="right"><%=StrUtils.formatNum((String)lineArr.get("ORDQTY"))%></TD>
            <TD align="right"><%=StrUtils.formatNum((String)lineArr.get("PICKQTY"))%></TD>
            <TD align="left"><%=(String)lineArr.get("UOM")%></TD>
        </TR>
        
      <% }  if (pickQty>0)
               {
    	          //to check dono has container in shiphis
    	           boolean exitFlag = false;
    	           Hashtable ht = new Hashtable();
    	           ht.put(IDBConstants.PLANT, PLANT);
       	   		   ht.put(IDBConstants.DONO, DONO);
    	           exitFlag =  _ShipHisDAO.isExisit(ht, " CONTAINER NOT IN ('NOCONTAINER', 'NOCONAINER') AND CONTAINER IS NOT NULL AND BATCH <>'NOBATCH'");
    	           //to check dono has container in shiphis end
    	           btnDisabled="";
    	           DOUtil poUtil = new DOUtil();
   		           Map ma = poUtil.getDOReceiptHdrDetails(PLANT,"Outbound order");
   		           
   		           //-----Modified by Deen on Feb 26 2014, Description:To display NOCONTAINER data's in PrintWithContainer/Batch/Sno report
   		           if(ma.get("DisplayContainer").equals("1"))
   		             {
   		            	btnContainerDisabled="";
   		             }
    	       }
        %>  
       </TABLE>
   </FORM>
  <b>
    <%= result%>
     <table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#dddddd">
        <tr>
          <td>
    <table cellspacing="2" cellpadding="0" border="0" width="35%" align="center">
      <tr>
        <td align= "center">
        <input type="button" value="PrintWithBatch/Sno"  name="action" onclick="javascript:return onRePrint();" <%=btnDisabled%> />&nbsp;&nbsp;    </td>
        <td align= "center">
        <td align= "center">
        <input type="button" value="PrintWithOutBatch/Sno"  name="action" onclick="javascript:return onRePrintWithOutBatch();" <%=btnDisabled%> />&nbsp;&nbsp;    </td>
         <td align= "center">
        <input type="button" value="PrintWithContainer/Batch/Sno"  name="action" onclick="javascript:return onRePrintWithContainer();" <%=btnContainerDisabled%> />&nbsp;&nbsp;    </td>
        <td align= "center">      <input type="button" value="Back" onClick="window.location.href='../home'"/>    </td>
       
     </tr>
    </table>
      </td>
        </tr>
      </table>
  </b>
  <%@ include file="footer.jsp"%>
</html>
