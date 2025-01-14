<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<!-- Not in Use - Menus status 0 -->
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function ExportReport()
{
   var flag    = "false";
 
   var ITEM    = document.form1.ITEM.value;
  
  

  
   if(ITEM != null    && ITEM != "") { flag = true;}
 
 

  
   document.form1.action="HQReportExcel.jsp?xlAction=GenerateXLSheetForHQReport";
   document.form1.submit();


}


function onGo(){
document.form1.action="HQReport.jsp";
document.form1.submit();
}
</script>
<title>Inventory Summary (Receipt vs Issue)</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
List saleList =null, stockin=null;
ArrayList invQryListSumTotal  = new ArrayList();
ShipHisDAO shipdao = new ShipHisDAO();
DateUtils _dateUtils = new DateUtils();
CustomerReturnDAO cusrtnDao = new CustomerReturnDAO();
String fieldDesc="";
String USERID ="",PLANT="",LOC ="",  ITEM = "",STOCK_SALE="",STOCK_TOTAL="",TOTAL_RTN="",BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="", FROM_DATE ="",  TO_DATE = "",fdate="",tdate="";
String html = "";
int Total=0,STOCK_EXPIRE_INT=0,STOCK_TOTAL_INT=0, STOCK_BALANCE_INT=0;
String SumColor="",PRD_CLS_ID="",PRD_CLS_ID1="";
boolean flag=false;

String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
LOC     = strUtils.fString(request.getParameter("LOC"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));

PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;


ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);



boolean cntRec=false;

if(PGaction.equalsIgnoreCase("View")){
 try{
	 ITEM = itemMstUtil.isValidInvAlternateItemInItemmst( PLANT, ITEM);
     
      
      
      Hashtable ht = new Hashtable();
      ht.put("a.PLANT",PLANT);
      if(strUtils.fString(PLANT).length() > 0)        
      if(strUtils.fString(ITEM).length() > 0)      
      {
        ht.put("a.ITEM",ITEM);
      } 
      if(strUtils.fString(PRD_DESCRIP).length() > 0)      
      {
        ht.put("b.ITEMDESC",PRD_DESCRIP);
      }
         invQryList = invUtil.getHQList(ht,PLANT,FROM_DATE,TO_DATE);
      if(invQryList.size() <=0)
      {
    	  cntRec =true;

        fieldDesc="Data's Not Found";
       
      }
      
 }catch(Exception e) { 
	 invQryList.clear();
	 cntRec=true;
	 
 }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="HQReport.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Inventory Summary (Receipt vs Issue)</font></TH>
    </TR>
  </TABLE>
  
  <center>
  <font face="Times New Roman" size="4">
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
       <th> <font color="red"><%=fieldDesc%></font></th>
    </table>
    </font>
  </center>
  <br>
  <TABLE border="0" width="70%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  
   <TR>
          <TH ALIGN="right" width="15%"> &nbsp;&nbsp;Product ID:  </TH>
          <TD  align="left">&nbsp;&nbsp;&nbsp;&nbsp;<INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="15"  MAXLENGTH=50>
           <a href="#" onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
     
          <TH ALIGN="right" width="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Product Description: </TH>
          <TD align="left">&nbsp;&nbsp;&nbsp;&nbsp;
          <INPUT name="PRD_DESCRIP" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" size="15"  MAXLENGTH=100>
          
           </TD>
           
        </TR>
        <TR>
          <TH ALIGN="right" >&nbsp;From_Date : </TH>
          <TD ALIGN="left">&nbsp;&nbsp;&nbsp;&nbsp;<INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = "inactiveGry" READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="right">&nbsp;To_Date : </TH>
          <TD align="left">&nbsp;&nbsp;&nbsp;&nbsp;<INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = "inactiveGry"  READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
	
    </TR>
    <TR>
          <TH ALIGN="left" width="15%"> &nbsp;&nbsp;</TH>
          <TD width="15%">&nbsp;&nbsp;
          </TD>
          <TH ALIGN="left" width="15%">&nbsp;&nbsp;</TH>
          <TD width="13%">&nbsp;&nbsp;</TD>
          <TH ALIGN="left" width="10%"></TH>
           <TD width="5"><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
          <TD width="0%"></TD>
          <TD ALIGN="left" width="29%"></TD>
        </TR>
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
       	  <TH><font color="#ffffff" align="left"><b>Product ID</TH>
          <TH><font color="#ffffff" align="left"><b>Description</TH>
          <TH><font color="#ffffff" align="left"><b>Total Goods Receipt</TH>
           <TH><font color="#ffffff" align="left"><b>Total Goods Issue</TH>
             <TH><font color="#ffffff" align="left"><b>Total Goods Return</TH>
            <TH><font color="#ffffff" align="left"><b>Balance stock</TH>     
       </tr>
        
       <%
          int j=0;
          String rowColor="";
         
          Hashtable htship = new Hashtable();
          htship.put("PLANT",PLANT);
          Hashtable htexp = new Hashtable();
          shipdao.setmLogger(mLogger);
             
              for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
            	 
                j=j+1;
                Map lineArr = (Map) invQryList.get(iCnt);
                String trDate="";
                int iIndex = iCnt + 1;
               // String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                rowColor = ((j == 0) || (j % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                htship.put("ITEM",(String)lineArr.get("ITEM")); 
                //Get Total Stock in 
                
                RecvDetDAO recvdao = new RecvDetDAO();
                stockin = recvdao.getTotalStockIn(htship,"","","");
System.out.println("stockin"+stockin.size());                
               
              for(int p=0;p<stockin.size();p++)
              {
            	Map stockarr = (Map)stockin.get(p);
            	STOCK_TOTAL = (String)stockarr.get("RECQTY");
            	if(STOCK_TOTAL==null)
	            	{
	            	  STOCK_TOTAL="0";
	              	}
                }
               //Get stock sale by item
               saleList = shipdao.getStockSale("isnull(sum(pickqty),0) as pickqty",htship,"");
            
               if(saleList.size()==0)
            	   STOCK_SALE="0";
               else{
               for(int k=0;k<saleList.size();k++)
               {
            	   Map salArr = (Map) saleList.get(k);
            	   STOCK_SALE = (String) salArr.get("pickqty"); 
               }
               }
               
                ArrayList rtnal=cusrtnDao.selectCustomerReturn("isnull(sum(qty),0) as returnqty",htship,"");
               if(rtnal.size()==0)
            	   TOTAL_RTN="0";
               else{
               for(int k=0;k<rtnal.size();k++)
               {
            	   Map salArr = (Map) rtnal.get(k);
            	   TOTAL_RTN = (String) salArr.get("returnqty"); 
               }
               }
                  int STOCK_TOTAL_RTN = Integer.parseInt(TOTAL_RTN);
                 STOCK_TOTAL_INT = Integer.parseInt(STOCK_TOTAL);
                 //STOCK_EXPIRE_INT = invUtil.getCountofExpdt(htexp,PLANT,(String)lineArr.get("ITEM"),"","");
                  STOCK_BALANCE_INT = ((STOCK_TOTAL_INT+STOCK_TOTAL_RTN)-Integer.parseInt(STOCK_SALE));//-STOCK_EXPIRE_INT;
          %>
         
          <TR bgcolor = "<%=rowColor%>">
 
              <TD align= "center"><%=(String)lineArr.get("ITEM") %></TD>
              <TD align= "center"><%=(String)lineArr.get("ITEMDESC")%></TD>
              <TD align= "center"><%=StrUtils.formatNum(STOCK_TOTAL)%></TD>
              <TD align= "center"><%=StrUtils.formatNum(STOCK_SALE)%></TD>
              <TD align= "center"><%=StrUtils.formatNum(TOTAL_RTN)%></TD>
              <TD align= "center"><%=StrUtils.formatNum( String.valueOf(STOCK_BALANCE_INT))%></TD>            
       <%} %>
         </TR>
          
     
    </TABLE>
   
    </table>
    
    <br>
<table align="center">
	<TR>
	<td><input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp;</td>
	<%
		//	if (invQryList.size() > 0) {
		%>
		<td><input type="button" value="ExportReport"
			onClick="javascript:ExportReport();"></td>
		<%
		//	}
		%>
	</TR>
</table>
  </FORM>
<%@ include file="footer.jsp"%>