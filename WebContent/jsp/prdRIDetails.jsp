<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.IDBConstants" %>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Product Receive & Issue Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
</jsp:include>
<script language="javascript">
function ExportReport()
{
   document.form1.action = "/track/ReportServlet?action=ExportRIDetails";
   document.form1.submit();
  
}
</script>

<script language="JavaScript" type="text/javascript"
	src="js/calendar.js"></script>

<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post">

<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	>
	
		
	
	
<%
StrUtils _strUtils = new StrUtils();
HTReportUtil rptUtil = new HTReportUtil();
rptUtil.setmLogger(mLogger);
ArrayList QryList =  null;

session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
    String userID = (String) session.getAttribute("LOGIN_USER");
	String ITEM="",PGaction="",fieldDesc="",FRDATE="",TODATE="";
	ITEM =   _strUtils.fString(request.getParameter("ITEM"));
	FRDATE = _strUtils.fString(request.getParameter("FROM_DATE"));
	TODATE = _strUtils.fString(request.getParameter("TO_DATE"));
	String LOC = _strUtils.fString(request.getParameter("LOC"));
	String TYPE = _strUtils.fString(request.getParameter("TYPE"));
	
	
		
	%>
	<INPUT name="ITEM" type = "HIDDEN" value="<%=ITEM%>" size="15"  MAXLENGTH=50>
	<INPUT name="LOC" type = "HIDDEN" value="<%=LOC%>" size="15"  MAXLENGTH=50>
	<INPUT name="TYPE" type = "HIDDEN" value="<%=TYPE%>" size="15"  MAXLENGTH=50>
	<INPUT name="FROM_DATE" type = "HIDDEN" value="<%=FRDATE%>" size="15"  MAXLENGTH=50>
	<INPUT name="TO_DATE" type = "HIDDEN" value="<%=TODATE%>" size="15"  MAXLENGTH=50>
		<TR>
		<TD width="10%"><b><%=ITEM%></b></TD>
		<TD colspan=2><b><%=new ItemMstDAO().getItemDesc(plant,ITEM)%></b></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>		
	</TR>
	
	<TR></TR>
	</TABLE>
	<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center" class="table">
	<TR style="background-color: #eaeafa;">

		<TH><b> Type</b></TH>
		<TH><b>Tran Type</b></TH>
		<TH><b>Order No</b></TH>
		<TH><b>Sup/Cust Name</b></TH>
		<TH><b>Loc</b></TH>
		<TH><b>Batch</b></TH>
		<TH><b>Qty</b></TH>
		<TH><b>TranDate</b></TH>
		<TH><b>Remarks</b></TH>
		<TH><b>User</b></TH>
	</TR>
	<%
	if(TYPE.equalsIgnoreCase("OCLOC")){
		
			QryList =  rptUtil.getPrdRecvIssueDetails(plant,ITEM,FRDATE,TODATE,LOC);
		
	}else{
		QryList =  rptUtil.getPrdRecvIssueDetails(plant,ITEM,FRDATE,TODATE);
	}
	
		
		
	    
	       if(QryList.size()<=0  ){ %>
		  <TR><TD colspan="8" align=center>No Data For This criteria</TD></TR>
		  <%
		  } else { %>
		<%  int iIndex = 0;
                 int irow = 0;
		  double sumprdQty = 0;String lastProduct="";
     	          for (int iCnt =0; iCnt < QryList .size(); iCnt++) {
		  Map lineArr = (Map) QryList.get(iCnt);
		
                  sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
                
                 String item =(String)lineArr.get("TRANTYPE");
                  String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
      
        if(lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(item)){
        
     %>
     
      <TR bgcolor = "<%=bgcolor%>">
      	<TD width="10%"><%=(String) lineArr.get("TRANTYPE")%></TD>
      	<TD align= "center" width="5%"><%=(String)lineArr.get("DIRTYPE")%></TD>
		<TD align= "center" width="5%"><%=(String)lineArr.get("ORDNO")%></TD>
		<TD align= "center" width="12%"><%=(String)lineArr.get("CNAME")%></TD>
		<TD align="center" width="15%"><%=(String) lineArr.get("LOC")%></TD>
		<TD align="center" width="10%"><%=(String) lineArr.get("BATCH")%></TD>
		<TD align="center" width="10%"><%=(String) lineArr.get("QTY")%></TD>
		<TD align="center" width="10%"><%=(String) lineArr.get("TRANDATE")%></TD>
		<TD align="center" width="10%"><%=(String) lineArr.get("REMARK")%></TD>	
		<TD align="center" width="10%"><%=(String) lineArr.get("CRBY")%></TD>	
      </TR> 
       
      <%    
      if(iIndex+1 == QryList.size()){
      irow=irow+1;
      bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";  
      %>
     
      <TR bgcolor ="<%=bgcolor%>" >
      <TD colspan=5></td><td align= "right"><b>Total:</b></td><TD align= "right"><b><%=StrUtils.formatNum((new Double(sumprdQty).toString()))%></b></TD><TD align= "right"></TD><td></td><TD></TD>
          </TR>
     <% }}else{
           sumprdQty = sumprdQty - Double.parseDouble((String)lineArr.get("QTY"));
         
     %>
     <TR bgcolor ="<%=bgcolor%>" >
    
           <TD colspan=5></td><td align= "right"><b>Total:</b></td><TD align= "right"><b><%=StrUtils.formatNum((new Double(sumprdQty).toString()))%></b></TD><TD align= "right"></TD><td></td><TD></TD>
          </TR>
          <%
              irow=irow+1;
              bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF"; 
          %>
              <TR bgcolor = "<%=bgcolor%>">
              	<TD width="10%"><%=(String) lineArr.get("TRANTYPE")%></TD>
              		<TD align= "center" width="5%"><%=(String)lineArr.get("DIRTYPE")%></TD>
					<TD align= "center" width="5%"><%=(String)lineArr.get("ORDNO")%></TD>
					<TD align= "center" width="12%"><%=(String)lineArr.get("CNAME")%></TD>
					<TD align="center" width="15%"><%=(String) lineArr.get("LOC")%></TD>
					<TD align="center" width="10%"><%=(String) lineArr.get("BATCH")%></TD>
					<TD align="center" width="10%"><%=(String) lineArr.get("QTY")%></TD>
					<TD align="center" width="10%"><%=(String) lineArr.get("TRANDATE")%></TD>
					<TD align="center" width="10%"><%=(String) lineArr.get("REMARK")%></TD>	
					<TD align="center" width="10%"><%=(String) lineArr.get("CRBY")%></TD>	
              </TR>
       
         <% 
           
             sumprdQty =  Double.parseDouble((String)lineArr.get("QTY"));
         
              if(iIndex+1 == QryList.size()){ 
              irow=irow+1;
              bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF"; 
      
          %>
          <TR bgcolor ="<%=bgcolor%>" >
              <TD colspan=5></td><td align= "right"><b>Total:</b></td><TD align= "right"><b><%=StrUtils.formatNum((new Double(sumprdQty).toString()))%></b></TD><TD align= "right"></TD><td></td><TD></TD>
          </TR>
          <%
      }
     }
   
           irow=irow+1;
           iIndex=iIndex+1;
           lastProduct = item;
     	          } 
     	               
     	          } 
		%>

</TABLE>
		<div class="form-group">
  		<div class="col-sm-offset-5 col-sm-4">   
  		
  		<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  		<button type="button" class="Submit btn btn-default"  onClick="window.location.href='javascript:history.back()'"><b>Back</b></button>
  		</div>
  		</div>


</FORM>
</div></div></div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="nobackblock" value="1" />
</jsp:include>