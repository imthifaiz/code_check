<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

var subWin = null;
function popUpWin(URL) {
  document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function ExportReport()
{
   var flag    = "false";
   var LOC     = document.form1.LOC.value;
   var ITEM    = document.form1.ITEM.value;
  

   if(LOC != null     && LOC != "") { flag = true;}
   if(ITEM != null    && ITEM != "") { flag = true;}
   
 
  document.form1.action="InventoryExcel.jsp?xlAction=GenerateXLSheet";
  
  document.form1.submit();


}



function onGo(){
document.form1.action="view_inv_list.jsp";
document.form1.submit();
}
</script>
<title>Inventory Summary with min qty</title>
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
String USERID="",PLANT="",LOC ="",  ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_BRAND_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",PRD_CLS_ID="",PRD_CLS_ID1="";
double Total=0;
boolean isWithZero= false;
boolean flag=false;

String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
LOC     = strUtils.fString(request.getParameter("LOC"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
// Start code added by Deen for product brand on 11/9/12 
PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
// End code added by Deen for product brand on 11/9/12 
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
String minQty = strUtils.fString(request.getParameter("MINQTY"));
String withZero = strUtils.fString(request.getParameter("WITHZERO"));

if(minQty.length()==0){
    minQty="N";
}else{
    minQty="Y";
}

if(withZero.length()>0){
isWithZero=true;
}
boolean cntRec=false;


%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="view_inv_list.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Inventory Summary with min qty</font></TH>
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
           <a href="#" onClick="javascript:popUpWin('product_list.jsp?ITEM='+form1.ITEM.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
     
          <TH ALIGN="left" width="10%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Location:  </TH>
          <TD width="23%"><INPUT name="LOC" type = "TEXT" value="<%=LOC%>" size="15"  MAXLENGTH=20>
          <a href="#" onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);">
              
           <img src="images/populate.gif" border="0"/>
          </a>
           </TD>
           <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product Class ID:  </TH>
          <TD width="15%"><INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="15"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
        </TR>
        <TR>
         
               <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product Type ID:  </TH>
          <TD width="13%"><INPUT name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="15"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><img src="images/populate.gif" border="0"/>
                
          </TD>
         <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product Description:  </TH>
          <TD width="13%"><INPUT name="PRD_DESCRIP" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" size="15"  MAXLENGTH=100>
                
          </TD>
           <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product Brand ID:  </TH>
          <TD width="13%"><INPUT name="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" size="15"  MAXLENGTH=20>
          <input type="hidden" name="PRD_BRAND_DESC" value="">
 		   <INPUT name="ACTIVE" type = "hidden" value="">
           <a href="#" onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');">           
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
          <TD width="0%"></TD>
          <TD ALIGN="left" width="29%"></TD>
        </TR>
         <!-- Start code added by Deen for product brand on 11/9/12  -->
		<TR>
           <!-- End code added by Deen for product brand on 11/9/12  -->
           <TH ALIGN="left" width="15%">&nbsp;&nbsp;</TH>
          <TD width="13%">&nbsp;&nbsp;</TD>
          <TH ALIGN="left" width="15%">&nbsp;&nbsp;</TH>
          <TD width="13%">&nbsp;&nbsp;</TD>
          <TH ALIGN="left" width="10%"></TH>
             <TD width="5"><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
          <TD width="0%">&nbsp;&nbsp;</TD>
          <TD ALIGN="left" width="29%">&nbsp;&nbsp;</TD>
        </TR>
	<!--	Do not remove comment this is not in use
        <TR>
                            
           <TH ALIGN="left" width="5%">&nbsp;&nbsp;</TH>
            <TD width="23%" align=" left">
            <input type="checkbox" name="MINQTY" value="MINQTY" <%if(minQty.equalsIgnoreCase("Y")) {%> checked <%}%>> Show Minimum Qty Stock<BR>
            <input type="checkbox" name="WITHZERO" value="WITHZERO" <%if(isWithZero) {%> checked <%}%>> With Zero<BR>
           </TD>  
		
         
		     
          <TH ALIGN="left" width="10%"></TH>
           <TD width="5"><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
          <TD width="0%"></TD>
          <TD ALIGN="left" width="29%"></TD>
        </TR>-->
 </TABLE>
  <br>
 <!-- <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
        <TH><font color="#ffffff" align="left"><b>Product ID</TH>
        <TH><font color="#ffffff" align="left"><b>Loc</TH>
        <TH><font color="#ffffff" align="left"><b>Product Class ID</TH>
        <TH><font color="#ffffff" align="left"><b>Product Type ID</TH>
        <TH><font color="#ffffff" align="left"><b>Description</TH>
        <TH><font color="#ffffff" align="left"><b>UOM</TH>
         <TH><font color="#ffffff" align="left"><b>MinQty</TH>
        <TH><font color="#ffffff" align="left"><b>Quantity</TH>
    
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
          for (int iSum =0; iSum<invQryListSumTotal.size(); iSum++){
             
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
             // if(strUtils.fString(BATCH).length() > 0)       ht.put("a.USERFLD4",BATCH);
              if(strUtils.fString(PRD_CLS_ID).length() > 0)        ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
			 
              else
            	  if(strUtils.fString(PRD_CLS_ID1).length() > 0)        ht.put("b.PRD_CLS_ID",PRD_CLS_ID1);
              if(strUtils.fString(PRD_TYPE_ID).length() > 0)        ht.put("b.itemtype",PRD_TYPE_ID);
              else
            	  if(strUtils.fString(request.getParameter("PRD_TYPE_ID1")).length() > 0)        ht.put("b.itemtype",request.getParameter("PRD_TYPE_ID1"));   
              if(PRD_CLS_ID.equalsIgnoreCase("")||PRD_CLS_ID==null)
            	  PRD_CLS_ID=PRD_CLS_ID1;
              if(minQty.equalsIgnoreCase("Y")){
              Hashtable htQuery = new Hashtable();
              
              if(strUtils.fString(PLANT).length() > 0)        htQuery.put("C.PLANT",PLANT);
              if(strUtils.fString(ITEM).length() > 0)        
              {
                htQuery.put("C.ITEM",ITEM);
              } 
              else
              {
               htQuery.put("C.ITEM",(String)lineArrSum.get("ITEM"));
              }
              if(strUtils.fString(LOC).length() > 0)        htQuery.put("D.LOC",LOC);
             // if(strUtils.fString(BATCH).length() > 0)       htQuery.put("D.BATCH",BATCH);
              if(strUtils.fString(PRD_CLS_ID).length() > 0)       htQuery.put("C.PRD_CLS_ID",PRD_CLS_ID);
              else
              if(strUtils.fString(PRD_CLS_ID1).length() > 0)           htQuery.put("C.PRD_CLS_ID",PRD_CLS_ID1);
              if(strUtils.fString(PRD_TYPE_ID).length() > 0)      htQuery.put("C.ITEMTYPE",PRD_TYPE_ID);
              else 
              if(strUtils.fString(PRD_TYPE_ID).length() > 0)  htQuery.put("C.ITEMTYPE",request.getParameter("PRD_TYPE_ID1"));
               invQryList = invUtil.getInvListSummaryForItemMinStockQty(htQuery,PLANT,PRD_DESCRIP);
              }else{
             // invQryList = invUtil.getInvListSummary(ht,PLANT,(String)lineArrSum.get("ITEM"),LOC,BATCH,PRD_CLS_ID1,PRD_DESCRIP,isWithZero);
              }
              if(invQryList.size() <=0)
              {
                fieldDesc="Data's Not Found";
              }
              for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                j=j+1;
                Map lineArr = (Map) invQryList.get(iCnt);
                String trDate="";
                int iIndex = iCnt + 1;
                rowColor = ((j == 0) || (j % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
               
                 if (trDate.length()>8)
                 trDate    = trDate.substring(8,10)+"-"+ trDate.substring(5,7)+"-"+trDate.substring(0,4);
                 Total=Total+Double.parseDouble(((String)lineArr.get("QTY").toString()));
                 if(((String)lineArr.get("ITEM")).equalsIgnoreCase((String)lineArrSum.get("ITEM")))
                 {
                   flag=true;
                 }
                 qty=Double.parseDouble(((String)lineArr.get("QTY").toString()));
                 stkqty=Double.parseDouble(((String)lineArr.get("STKQTY").toString()));
          %>
         
          <TR bgcolor = "<%=rowColor%>">
              <TD align= "center"><%=(String)lineArr.get("ITEM") %></TD>
               <TD align= "center"><%=(String)lineArr.get("LOC") %></TD>
               <TD align= "center"><%=(String)lineArr.get("PRDCLSID") %></TD>
                <TD align= "center"><%=(String)lineArr.get("ITEMTYPE") %></TD>
              <TD align= "center"><%=(String)lineArr.get("ITEMDESC") %></TD>
              <TD align= "center"><%=(String)lineArr.get("STKUOM") %></TD>
              <TD align= "right"><%=(String)lineArr.get("STKQTY") %></TD>
              <TD align= "right" ><%=StrUtils.formatNum((String)lineArr.get("QTY")) %></TD>
            
       <%} if(flag==true) {%>
         </TR>
           <%
            j=j+1;
            rowColor = ((j == 0) || (j % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
          %>
           <TR bgcolor = "<%=rowColor%>">
              <TD align= "center"></TD>
              <TD align= "center"></TD><TD align= "center"></TD>
              <TD align= "center"></TD>
              <TD align= "center"></TD>
              <TD align= "center"></TD> <TD align= "center"></TD>
             <TD align= "right" ><b>Total:</b></TD>
             <%if(Total < stkqty) { if(strUtils.fString(LOC).length() > 0) { %>
                  <TD align= "right" ><b><%=Total%></b></TD>
                  <%}else{%>
                   <TD align= "right"><font color="red"><b><%=StrUtils.formatNum(String.valueOf(Total))%></b></font></TD>
              <% }}else{%>
                   <TD align= "right" ><b><%=StrUtils.formatNum(String.valueOf(Total))%></b></TD>
              <%}%> 
          </TR>
       <%}}%>
    </TABLE>-->
     <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  <br>
  <br>
     <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; </td>
   <td>   
   <input type="button" value="ExportReport" onClick="javascript:ExportReport();" >  
 
   </td>
   </TR>
    </table>
    
  </FORM>
  </html>
  <SCRIPT LANGUAGE="JavaScript">



function onGo(){
    var productId = document.form1.ITEM.value;
    var desc = document.form1.PRD_DESCRIP.value;
    var loc = document.form1.LOC.value;
    var prdClass = document.form1.PRD_CLS_ID.value;
    var prdType = document.form1.PRD_TYPE_ID.value;
    // Start code modified by Deen for product brand on 11/9/12 
    var prdBrand = document.form1.PRD_BRAND_ID.value;


    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/InvMstServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: { ITEM: productId,PRD_DESCRIP:desc,LOC:loc,PRD_BRAND_ID:prdBrand,PRD_CLS_ID:prdClass,PRD_TYPE_ID:prdType,ACTION: "VIEW_INV_SUMMARY_MIN_QTY",PLANT:"<%=PLANT%>",LOGIN_USER:"<%=USERID%>"},dataType: "json", success: callback });
    // End code modified by Deen for product brand on 11/9/12 
}


function callback(data){
		
		var outPutdata = getTable();
		var ii = 0;
		var errorBoo = false;
		$.each(data.errors, function(i,error){
			if(error.ERROR_CODE=="99"){
				errorBoo = true;
				
			}
		});
		
		if(!errorBoo){
			
	        $.each(data.items, function(i,item){
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#dddddd";
                       
	        	outPutdata = outPutdata+item.INVDETAILS
                        	ii++;
	            
	          });
		}else{
		//	outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="6"><BR><CENTER><B><FONT COLOR="RED">No details found!</FONT></B></CENTER></TD></TR>';
		}
        outPutdata = outPutdata +'</TABLE>';
        document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
         document.getElementById('spinnerImg').innerHTML ='';

     
   }

function getTable(){
            return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
            '<TR BGCOLOR="#000066">'+
            '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Loc</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Product Class ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Product Type ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Product Brand ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
            '<TH><font color="#ffffff" align="left"><b>UOM</TH>'+
            '  <TH><font color="#ffffff" align="left"><b>MinQty</TH>'+
       ' <TH><font color="#ffffff" align="left"><b>Quantity</TH>'+
            '</TR>';
                
}
   
document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
              
</SCRIPT>
<%@ include file="footer.jsp"%>