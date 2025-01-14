<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>
<html>
  <head>
    <script language="javascript">

function onExportExcel(){
    //  document.form1.excelexport.disabled = true;
      document.form1.action="exportCntSheet.jsp";
      document.form1.submit();
 }   

function onConfirm()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/WorkOrderImportServlet?action=confirmProdBOMCountSheet" ;
  frmRoot.submit();
  return true;
}

function onGo()
{
 
 var frmRoot=document.form1;
  
 	if(frmRoot.ImportFile.value=="" || frmRoot.ImportFile.value.length==0)
	{
		alert("Please Select Import file!");
		frmRoot.ImportFile.focus();
		return false;
	}
  else if(frmRoot.SheetName.value=="" || frmRoot.SheetName.value.length==0)
	{
		alert("Please Enter Sheet Name!");
		frmRoot.SheetName.focus();
		return false;
	}
  else{
      
    /*   if(frmRoot.isDataPresent.value=="true"){
         var isDataPresentcon = confirm ("Data Already Exists,Use Reset StkTake option to Clear the Data and then Import CountSheet");
          if(isDataPresentcon) {
          return false;
          }else{
           return false;
          }
          
       }*/
      var con = confirm ("Process will take few minutes  to download");
      if(con) {
         
          var file=frmRoot.ImportFile.value;
          var sheet=frmRoot.SheetName.value;
         
          frmRoot.action = "/track/WorkOrderImportServlet?action=importProdBOMCountSheet" + "&ImportFile=" + file +"&SheetName=" + sheet;
          frmRoot.submit();
          return true;
   }else
   {
         return false;
   }
  
}
  
 
}


  
</script>
    <title>Import Data Sheet</title>
  </head>
  <link rel="stylesheet" href="css/style.css"></link>
  <%
 MLogger logger = new MLogger();
String fieldDesc="";
String msg="";

String PLANT              = (String)session.getAttribute("PLANT");
String _login_user        = (String)session.getAttribute("LOGIN_USER");
String ServerName =  StrUtils.fString(request.getRemoteHost()).trim();
String action         = StrUtils.fString(request.getParameter("action")).trim();
String ImportFile         = StrUtils.fString(request.getParameter("ImportFile")).trim();
String SheetName         = StrUtils.fString(request.getParameter("SheetName")).trim();

System.out.println("ImportFileNAme *** :" + ImportFile); 
System.out.println("SheetName *** :" + SheetName);
String buttonStat ="";
boolean isExistsCountSheetData = false;

Hashtable ht = new Hashtable();
System.out.println("Setting plant value ");
//System.out.println ("PLANT : " + PLANT);

if(PLANT==null){
String result = "New user diverting to login page";
%>
    <jsp:forward page="login.jsp" >
    <jsp:param name="warn" value="<%=result%>" />
    </jsp:forward>
<%
}

ht.put("PLANT",PLANT);


if(action.equalsIgnoreCase("new")){
  
   session.removeAttribute("IMP_PRODBOMRESULT");
   session.removeAttribute("PRODBOMRESULT");
   ImportFile="";
   SheetName="";
   action="";
   
}

 ArrayList alRes = null;
 System.out.println("Action *** :" + action);

 fieldDesc=StrUtils.fString((String)session.getAttribute("PRODBOMRESULT"));

 if(null == session.getAttribute("IMP_PRODBOMRESULT"))
    {
      alRes = new ArrayList();
      buttonStat= "disabled";
   } else{
    
      alRes = (ArrayList)session.getAttribute("IMP_PRODBOMRESULT");
      if(alRes.size()==0)
      buttonStat= "disabled";
  }
  
 MLogger.log(1, "_______________________ " + this.getClass() + "" +msg);

     
   
%>
  <%@ include file="body.jsp"%>
  <form name="form1" method="post" enctype="multipart/form-data" action="/WorkOrderImportServlet">
    <input type="hidden"  name="isDataPresent" value="true" ></input>
    <input type="hidden" value="" name="download"></input>
    <br></br>
    <table border="0" width="100%" cellspacing="0" cellpadding="0"
           align="center" bgcolor="#dddddd">
      <tr>
        <th bgcolor="#000066" colspan="11">
          <font color="#ffffff">Import Production BOM Data </font>
        </th>
      </tr>
    </table>
    
    <br></br>
    <table border="0" width="80%" cellspacing="0" cellpadding="0" align="center"
           bgcolor="#dddddd" height="83">
      <tr>
        <th align="RIGHT">&nbsp;</th>
        <td>&nbsp;</td>
        <th align="RIGHT">&nbsp;Select Excel Sheet :</th>
        <td>
          <input name="ImportFile" type="file" size="20" value="<%=ImportFile%>" maxlength="20"  ></input>
        </td>
        <th align="RIGHT">&nbsp;Sheet Name:</th>
        <td>
         
            <input name="SheetName" type="text" value="Sheet1" size="20" maxlength="20"></input>
        </td>
        <td>
          <input type="button" name="Submit" value="Import"    onclick="javascript:return onGo();"/>
        </td>
      </tr>
     
    </table>
    

      <table border="0" width="80%" cellspacing="0" cellpadding="0" align="center"      bgcolor="#dddddd">
      <tr ></tr>
      <tr>
      
        
         <td align="center">
          <input type="button" name="Submit" value="Confirm" 
                 onclick="javascript:return onConfirm();"  <%=buttonStat%> />
        </td>
       </tr>
      </table>
    
    <br>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
	 <tr bgcolor="navy">
          <th width="3%"><font color="#ffffff" align="center">SNO</font></th>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>Parent Product</STRONG></center></font></td> 
	 <td width="10%"><font color="#ffffff" align="left"><center><STRONG>Child Product</STRONG></center></font></td>
          <td width="10%"><font color="#ffffff" align="left"><center><STRONG>Qty Per</STRONG></center></font></td>
      <td width="5%"><font color="#ffffff" align="left"><center><STRONG>Operation SeqNum</STRONG></center></font></td>
	 <td width="5%"><font color="#ffffff" align="left"><center><STRONG>Remark1</STRONG></center></font></td>
	 <td width="9%"><font color="#ffffff" align="left"><center><STRONG>Remark2</STRONG></center></font></td> 
	  </tr>
       <%
          for (int iCnt =0; iCnt<alRes.size(); iCnt++){
          Map lineArr = (Map) alRes.get(iCnt);
         
        int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
             <TR bgcolor = "<%=bgcolor%>">
               <TD align="left" width="3%"><%=iIndex%></TD>
             <TD align="left" width="10%"><%=(String)lineArr.get(IDBConstants.PARENTITEM)%></TD>
             <TD align="left" width="12%"><%=(String)lineArr.get(IDBConstants.CHILDITEM)%></TD>
             <TD align="left" width="12%"><%=(String)lineArr.get(IDBConstants.QTY)%></TD>
             <TD align="left" width="12%"><%=(String)lineArr.get(IConstants.OPRSEQNUM)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IDBConstants.ITEMMST_REMARK1)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IDBConstants.ITEMMST_REMARK2)%></TD>
             </TR> 
       <%}%>
        
    </TABLE>
    <font face="Times New Roman">
      <table border="0" cellspacing="1" cellpadding="2" align="center"
             bgcolor="">
             <%=fieldDesc%>
      </table>
    </font>
    <table align="center">
    <tr>
    </br>
    <td>
      <input type="button" value="Back"   onclick="window.location.href='importMasterExcelPage.jsp'" ></input>
    </td> 
  </form>
  </tr>
  </html>
  <%@ include file="footer.jsp"%>
  <% MLogger.log(-1, "_______________________ " + this.getClass() + ""); %>
</html>