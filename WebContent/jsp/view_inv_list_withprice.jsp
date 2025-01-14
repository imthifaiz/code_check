<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
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
   var LOC     = document.form1.LOC.value;
   var ITEM    = document.form1.ITEM.value;
   var BATCH   = document.form1.BATCH.value;
  

   if(LOC != null     && LOC != "") { flag = true;}
   if(ITEM != null    && ITEM != "") { flag = true;}
   if(BATCH != null    && BATCH  != "") { flag = true;}
 

 
  document.form1.action="InventoryExcel.jsp?xlAction=GenerateXLSheet_WithPrice";
  
  document.form1.submit();


}



function onGo(){
document.form1.action="view_inv_list_withprice.jsp";
document.form1.submit();
}
</script>
<title>Inventory Summary with price</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
ArrayList invQryListSumTotal  = new ArrayList();


String fieldDesc="";
String USERID="",PLANT="",LOC ="",  ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="", FROM_DATE ="",  TO_DATE = "",fdate="",tdate="";
String html = "";
double Total=0;
double sumTotalPrice=0;
double TotalPrice=0;
String SumColor="",PRD_CLS_ID="",PRD_CLS_ID1="";
boolean flag=false;

String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
LOC     = strUtils.fString(request.getParameter("LOC"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
BATCH   = strUtils.fString(request.getParameter("BATCH"));
PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_CLS_ID1 = strUtils.fString(request.getParameter("PRD_CLS_ID1"));
FROM_DATE    = strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE    = strUtils.fString(request.getParameter("TO_DATE"));
PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));

ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);


if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);


if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);


boolean cntRec=false;
if(PGaction.equalsIgnoreCase("View")){
 try{
	 ITEM = itemMstUtil.isValidInvAlternateItemInItemmst( PLANT, ITEM);
     
       Hashtable htSumTotal = new Hashtable();
       if(strUtils.fString(PLANT).length() > 0)       htSumTotal .put("a.PLANT",PLANT);
       if(strUtils.fString(ITEM).length() > 0)        htSumTotal.put("a.ITEM",ITEM);
       if(strUtils.fString(LOC).length() > 0)        htSumTotal.put("a.LOC",LOC);
       if(strUtils.fString(BATCH).length() > 0)        htSumTotal.put("a.USERFLD4",BATCH);

               if(strUtils.fString(PRD_CLS_ID).length() > 0)        htSumTotal.put("b.PRD_CLS_ID",PRD_CLS_ID);
         if(strUtils.fString(PRD_TYPE_ID).length() > 0)        htSumTotal.put("b.ITEMTYPE",PRD_TYPE_ID);
       invQryListSumTotal = invUtil.getInvListSumTotalByItemForCond(htSumTotal,PLANT,PRD_DESCRIP); 

      if(invQryListSumTotal.size()<=0)
			cntRec =true;
      
 }catch(Exception e) {
	 invQryListSumTotal.clear();
	 cntRec=true;	 
 }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="view_inv_list_withprice.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Inventory Summary with price</font></TH>
    </TR>
  </TABLE>
  
  <center>
  <font face="Times New Roman" size="4">
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
       <%=fieldDesc%>
    </table>
    </font>
  </center>
  <br>
  <TABLE border="0" width="70%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  
   <TR>
          <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product ID:  </TH>
          <TD width="18%"><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="15"  MAXLENGTH=50>
           <a href="#" onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
     
          <TH ALIGN="left" width="10%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Location:  </TH>
          <TD width="23%"><INPUT name="LOC" type = "TEXT" value="<%=LOC%>" size="15"  MAXLENGTH=20>
          <a href="#" onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);">
              
           <img src="images/populate.gif" border="0"/>
          </a>
           </TD>
           <TH ALIGN="Center" width="10%"> &nbsp;&nbsp;  Batch :  </TH>
           <TD width="29%"><INPUT name="BATCH" type = "TEXT" value="<%=BATCH%>" size="15"  MAXLENGTH=40></TD>
          <TD width="0%"></TD>
          <TD ALIGN="center" width="5%"></TD>
        </TR>
        <TR>
          <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product Class ID:  </TH>
          <TD width="15%"><INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="15"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
               <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product Type ID:  </TH>
          <TD width="13%"><INPUT name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="15"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><img src="images/populate.gif" border="0"/>
                
          </TD>
         <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product Description:  </TH>
          <TD width="13%"><INPUT name="PRD_DESCRIP" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" size="15"  MAXLENGTH=100>
                
          </TD>
          <TD width="0%"></TD>
          <TD ALIGN="left" width="29%"></TD>
        </TR>
		<TR>
          <TH ALIGN="left" width="15%"> &nbsp;&nbsp;</TH>
          <TD width="15%">&nbsp;&nbsp;
          </TD>
          <TH ALIGN="left" width="15%">&nbsp;&nbsp;</TH>
          <TD width="13%">&nbsp;&nbsp;</TD>
          <TH ALIGN="left" width="10%"></TH>
           <TD width="5">&nbsp;&nbsp;</TD>
          <TD width="0%">&nbsp;&nbsp;</TD>
          <TD ALIGN="left" width="29%">&nbsp;&nbsp;</TD>
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
         <!--<TH><font color="#ffffff" align="center">SNO</TH>-->
         <TH><font color="#ffffff" align="left"><b>Product ID</TH>
          <TH><font color="#ffffff" align="left"><b>Loc</TH>
          <TH><font color="#ffffff" align="left"><b>Product Class ID</TH>
           <TH><font color="#ffffff" align="left"><b>Product Type ID</TH>
         <TH><font color="#ffffff" align="left"><b>Description</TH>
         <TH><font color="#ffffff" align="left"><b>UOM</TH>
         
           <TH><font color="#ffffff" align="left"><b>Batch</TH>
           <TH><font color="#ffffff" align="left"><b>Unit Price</TH>
          <TH><font color="#ffffff" align="left"><b>Qty</TH>
          <TH><font color="#ffffff" align="left"><b>Total Price</TH>
          
       <!--  <TH><font color="#ffffff" align="left"><b>UOM</TH> -->
       </tr>
        <%
	      if(invQryListSumTotal.size()<=0 && cntRec==true ){ %>
		  <TR><TD colspan=8 align=center>No Data For This criteria</TD></TR>
		  <%}%>
       <%
          int j=0;
          String rowColor="";
         double qty=0;
         double stkqty=0;
          double unitprice=0;
        double totqty=0;
         // System.out.println("Inventory total size:"+invQryListSumTotal.size());
          for (int iSum =0; iSum<invQryListSumTotal.size(); iSum++){
             
             // SumColor = ((iSum == 0) || (iSum % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
              
              Total=0;
              flag=false;
              Map lineArrSum = (Map) invQryListSumTotal.get(iSum);
              Hashtable ht = new Hashtable();
              if(strUtils.fString(PLANT).length() > 0)        ht.put("a.PLANT",PLANT);
              if(strUtils.fString(ITEM).length() > 0)      
              {
                ht.put("a.ITEM",ITEM);
              } 
              else
              {
               ht.put("a.ITEM",(String)lineArrSum.get("ITEM"));
              }
              
              if(strUtils.fString(LOC).length() > 0)        ht.put("LOC",LOC);
              if(strUtils.fString(BATCH).length() > 0)        ht.put("a.USERFLD4",BATCH);
              if(strUtils.fString(PRD_CLS_ID).length() > 0)        ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
			 
              else
            	  if(strUtils.fString(PRD_CLS_ID1).length() > 0)        ht.put("b.PRD_CLS_ID",PRD_CLS_ID1);
              if(strUtils.fString(PRD_TYPE_ID).length() > 0)        ht.put("b.itemtype",PRD_TYPE_ID);
              else
            	  if(strUtils.fString(request.getParameter("PRD_TYPE_ID1")).length() > 0)        ht.put("b.itemtype",request.getParameter("PRD_TYPE_ID1"));   
              if(PRD_CLS_ID.equalsIgnoreCase("")||PRD_CLS_ID==null)
            	  PRD_CLS_ID=PRD_CLS_ID1;
              //invQryList = invUtil.getInvListSummary(ht,PLANT,(String)lineArrSum.get("ITEM"),LOC,BATCH,PRD_CLS_ID1);
              //invQryList = invUtil.getInvListSummaryWithPrice(ht,PLANT,(String)lineArrSum.get("ITEM"),LOC,BATCH,PRD_CLS_ID1,PRD_DESCRIP);
              if(invQryList.size() <=0)
              {
                fieldDesc="Data's Not Found";
              }
             
              for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
            	 
                j=j+1;
                Map lineArr = (Map) invQryList.get(iCnt);
                String trDate="";
                TotalPrice=0;
                int iIndex = iCnt + 1;
               // String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                rowColor = ((j == 0) || (j % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
               
                 if (trDate.length()>8)
                 trDate    = trDate.substring(8,10)+"-"+ trDate.substring(5,7)+"-"+trDate.substring(0,4);
                 unitprice=Double.parseDouble(((String)lineArr.get("UNITPRICE").toString()));
                 totqty=Double.parseDouble(((String)lineArr.get("QTY").toString()));
                 Total=Total+totqty;
                 TotalPrice=(unitprice*totqty);
                 //totqty = StrUtils.Round(totqty,2);
		//System.out.println("Unitp"+unitprice+"totqty"+totqty+"total"+TotalPrice);                 
                // sumTotalPrice=sumTotalPrice+(unitprice*totqty);
                // sumTotalPrice=unitprice*Total;
                sumTotalPrice=sumTotalPrice+TotalPrice;
                 if(((String)lineArr.get("ITEM")).equalsIgnoreCase((String)lineArrSum.get("ITEM")))
                 {
                	
                   flag=true;
                 }
                
                 qty=Double.parseDouble(((String)lineArr.get("QTY").toString()));
                
                
                
                 
                
          %>
         
          <TR bgcolor = "<%=rowColor%>">
           <!-- <TD align="center"><%=j%></TD>-->
              <TD align= "center"><%=(String)lineArr.get("ITEM") %></TD>
               <TD align= "center"><%=(String)lineArr.get("LOC") %></TD>
               <TD align= "center"><%=(String)lineArr.get("PRDCLSID") %></TD>
                <TD align= "center"><%=(String)lineArr.get("ITEMTYPE") %></TD>
              <TD align= "center"><%=(String)lineArr.get("ITEMDESC") %></TD>
              <TD align= "center"><%=(String)lineArr.get("STKUOM") %></TD>
             
              <TD align= "center"><%=(String)lineArr.get("BATCH") %></TD>
              <TD align= "right"><%=StrUtils.currencyWtoutSymbol((String)lineArr.get("UNITPRICE")) %></TD>
              <TD align= "right" ><%=StrUtils.formatNum((String)lineArr.get("QTY")) %></TD>
              <TD align= "right" ><%=StrUtils.currencyWtoutSymbol(String.valueOf(TotalPrice)) %></TD>
            
       <%} if(flag==true) {%>
         </TR>
           <%
            j=j+1;
            rowColor = ((j == 0) || (j % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
          %>
           <TR bgcolor = "<%=rowColor%>">
            <!-- <TD align="center"><%=j%></TD>-->
              <TD align= "center"></TD>
              <TD align= "center"></TD><TD align= "center"></TD>
              <TD align= "center"></TD>
              <TD align= "center"></TD>
              <TD align= "center"></TD> 
              <TD align= "center"></TD> 
              <TD align= "right"><b>Total:</TD>
            
             <%if(Total < stkqty) { if(strUtils.fString(LOC).length() > 0) { %>
                  <TD align= "right" ><b><%=StrUtils.formatNum(String.valueOf(Total))%></b></TD>
                  <%}else{%>
                   <TD align= "right"><font color="red"><b><%=StrUtils.formatNum(String.valueOf(Total))%></b></font></TD>
              <% }}else{%>
                   <TD align= "right" ><b><%=StrUtils.formatNum(String.valueOf(Total))%></b></TD>
              <%}%> 
            <%if(Total < stkqty) { if(strUtils.fString(LOC).length() > 0) { %>
                  <TD align= "right" ><b><%=StrUtils.currencyWtoutSymbol(String.valueOf(sumTotalPrice))%></b></TD>
                  <%}else{%>
                   <TD align= "right"><font color="red"><b><%=StrUtils.currencyWtoutSymbol(String.valueOf(sumTotalPrice))%></b></font></TD>
              <% }}else{%>
                   <TD align= "right" ><b><%=StrUtils.currencyWtoutSymbol(String.valueOf(sumTotalPrice))%></b></TD>
              <%}%> 
          </TR>
       <%}
       sumTotalPrice=0;
          }%>
    </TABLE>
     <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; </td>
   <td>   
   <input type="button" value="ExportReport" onClick="javascript:ExportReport();" >  
   <!--  <input type="button" value="Generate Excel" onClick="javascript:return onGenerate();">-->
   </td>
   </TR>
    </table>
    
  </FORM>
<%@ include file="footer.jsp"%>