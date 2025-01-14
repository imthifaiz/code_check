<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>



<%@ include file="header.jsp"%>
    <script language="javascript">

function onCloseLine(){
     var mes=confirm("Are you sure you want to Close the Order Line !");
      if(mes==false)
      {
      return  false;
      }
   
     var ischeck = false;
     var Lot ;
     var concatLotDet="";
 
   var i = 0;
   var noofcheckbox=1;
   
    var noofcheckbox = document.form1.chkdLot.length;
   
    document.form1.cmd.value="CLOSE_ORDER_LINES";
    
    if(form1.chkdLot.length == undefined)
    {
             if(form1.chkdLot.checked)
            {
            document.form1.LINES.value=form1.chkdLot.value+",";
            document.form1.action = "orderLineDetails.jsp";
            document.form1.submit();
            }else if(!form1.chkdLot.checked)
            {
             alert("Please Select Line Products To Close");
               return false;
            }
    
    }else
    {
             for (i = 0; i < noofcheckbox; i++ )
              {
               ischeck = document.form1.chkdLot[i].checked;
                   if(ischeck)
                    {
                    Lot=document.form1.chkdLot[i].value;
                    concatLotDet=concatLotDet+Lot+",";
                    }
                }
              document.form1.LINES.value=concatLotDet;
             
              document.form1.action = "orderLineDetails.jsp";
              document.form1.submit();  
    }
}





</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
 <% 
 
        OrderTypeUtil orderType =  new OrderTypeUtil();
        orderType.setmLogger(mLogger);
        StrUtils strUtils     = new StrUtils();
        ArrayList alList  = new ArrayList();
        String PLANT        = (String)session.getAttribute("PLANT");
        String USER         = (String)session.getAttribute("LOGIN_USER");
        String action       = strUtils.fString(request.getParameter("action")).trim();
        String cmd       = strUtils.fString(request.getParameter("cmd")).trim();
      
        String  chkString= "",result="",recStr="";
        String  MODULETYPE="",ORDERNO="",LINES="",REMARKS="";
       
        MODULETYPE            = strUtils.fString(request.getParameter("MODULENAME"));
        ORDERNO               = strUtils.fString(request.getParameter("ORDERNO"));
        LINES                = strUtils.fString(request.getParameter("LINES"));
        result                = strUtils.fString(request.getParameter("result"));
         REMARKS               = strUtils.fString(request.getParameter("REMARKS"));
        String CurrentDate    = DateUtils.getDate();
        String closeBtnEnabled="disabled";
        
         alList=  orderType.getOrderLineDetails(PLANT,MODULETYPE,ORDERNO);
           if(alList.size()==0){
           recStr="No Orders Found";
              closeBtnEnabled="disabled";
           }else{
             closeBtnEnabled="";
           }
        
        
      if(cmd.equalsIgnoreCase("CLOSE_ORDER_LINES")){
      try{

          Map map = new HashMap();
          map.put("LOGIN_USER",USER);
          map.put("PLANT",PLANT);
          map.put("MODULE_NAME",MODULETYPE); 
          map.put("ORDERNO",ORDERNO);
          map.put("ORDERLINES",LINES);
          if(REMARKS.length()>100)REMARKS=REMARKS.substring(0,100)  ;
          map.put("REMARKS",REMARKS);
          
         boolean isOrdersClosed=  orderType.process_OrderCloseItems(map);
         if (isOrdersClosed){
               result =  "<tr><td><font class=maingreen><centre>Line products Closed successfully</centre></font></td></tr>";
               response.sendRedirect("closeByOrder.jsp?cmd=View&result="+result+"&MODULENAME="+MODULETYPE);
                
         }else{
              result =  "<tr><td><font class=mainred><centre>Failed to Close Line Items !</centre></font></td></tr>";
              response.sendRedirect("orderLineDetails.jsp?result="+result+"&MODULENAME="+MODULETYPE);
         } 
         
    
        }catch(Exception e){System.out.println("Exception :: "+e.toString());
              result =  "<tr><td><font class=mainred><centre>" + e.getMessage() + "!</centre></font></td></tr>";
              response.sendRedirect("orderLineDetails.jsp?result="+result+"&MODULENAME="+MODULETYPE+"&ORDERNO="+ORDERNO);
        }
    
     }
 
       
       
   

%>
<html>

  <head>

  <title>Order Line Details</title>
  </head>
   
   <link rel="stylesheet" href="css/style.css">
    
 
    <%@ include file="body.jsp"%>
    <FORM name="form1" method="post" action="orderLineDetails.jsp">
    <INPUT type="hidden" name="cmd" value=""/>
    <INPUT type="hidden" name="LINES" value="">



    <br/>
    <TABLE border="0" width="75%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11">
          <font color="white"> Order Line Details </font>
        </TH>
      </TR>
    </TABLE>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <center>
   <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" >
      <%= result%>
   </table>
   </center>
     
         <TABLE border="0" width="75%" height="10%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
           <tr >
          
            <TH ALIGN="RIGHT" >Order :&nbsp;&nbsp;</TH>
            
            <TD ><INPUT name="MODULENAME" type = "TEXT" value="<%=MODULETYPE%>" size="20" READONLY >   </TD>
            <TH ALIGN="RIGHT" >Order No :&nbsp;&nbsp;</TH>
            <TD ><INPUT name="ORDERNO" type = "TEXT" value="<%=ORDERNO%>" size="20"  READONLY  ><!--<a href="#" onClick="javascript:popWin('listView/view_po_items.jsp?ITEM='+form1.ITEM.value +'&ITEMDESC='+form1.ITEMDESC.value);"><img src="images/populate.gif" border="0"></a>&nbsp;&nbsp;-->
            </TD>
          
          </TR>
          
        
         </TABLE>
        

      <br>
       <br>
       
        <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
        <TR BGCOLOR="#000066">
    
           <th  align="left"><font color="#ffffff">Check</th>
           <th  align="left"><font color="#ffffff">Line No</th>
           <td  align="left"><font color="#ffffff"><b>Product</b></font></td>
           <td  align="left"><font color="#ffffff"><b>Description</b></font></td>
           <td  align="left"><font color="#ffffff"><b>UOM</b></font></td>
           <td  align="left"><font color="#ffffff"><b>Qty Order</b></font></td>
           <td  align="left"><font color="#ffffff"><b>Status</b></font></td>
          
           
       </TR>
      
  <%
    
          for (int iCnt =0; iCnt<alList.size(); iCnt++){
          Map lineArr = (Map) alList.get(iCnt);
          int iIndex = iCnt + 1;
          
           chkString  =(String)lineArr.get("LNO")+"||"+(String)lineArr.get("ITEM");  
           String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        
       %>
       <TR bgcolor = <%=bgcolor%>>
       <TD align="left"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdLot" value="<%=chkString%>"  >  </TD>
        <TD align="left" > &nbsp;<font class="textbold"><%=(String)lineArr.get("LNO")%></font></TD>
          <TD align="left"><%=(String)lineArr.get("ITEM")%></TD>
          <TD align="left"><%=(String)lineArr.get("ITEMDESC")%></TD>
           <TD align="left"><%=(String)lineArr.get("UNITMO")%></TD>
           <TD align="left"><%=StrUtils.formatNum((String)lineArr.get("QTYOR"))%></TD>
          <TD align="left"><%=(String)lineArr.get("LNSTAT")%></TD>
        </TR>
        
      <% 
     
      }%>  

        </TABLE>
     
    <center>
        <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" >
        <%= recStr%>
        </table>
        </center>

  <b>
   
     <table cellspacing="0" cellpadding="0" border="0" width="100%" >
      <TR>
     <TH WIDTH="38%" ALIGN="RIGHT">Remarks&nbsp;&nbsp;:</TH>
     
		<TD width="62%"><TEXTAREA NAME="REMARKS" COLS=40 ROWS=3 onkeypress="return imposeMaxLength(this, 100);" ></TEXTAREA></TD>
     </tr>
     
        <tr >
         <TH WIDTH="38%" ALIGN="RIGHT"></TH>
          <td width="62%">
    <table cellspacing="2" cellpadding="0" border="0" WIDTH="30"  align="left" bgcolor="#dddddd">
      <tr>
         <td align= "center">    <input type="button" value="Back" onClick="window.location.href='closeByOrder.jsp?MODULENAME=<%=MODULETYPE%>&cmd=View'"/>  </td>
        <td align= "center"><input type="button" value="Close Line Products" name="closeItem" onclick="javascript:return onCloseLine();"  <%=closeBtnEnabled%> />  </td>
       
      </tr>
    </table>
      </td>
        </tr>
      </table>
  </b>
  </FORM>
  <%@ include file="footer.jsp"%>
</html>
