<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="com.track.constants.TransactionConstants"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.dao.MovHisDAO"%>

<%
String title = "Authorise Company";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'PlantSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
function onGo(){
  
   var objFromDate = document.form1.STARTDATE.value;
   var objToDate =document.form1.EXPIRYDATE.value;
   document.form1.submit();
}
function OnAuthData(form){
 var ischeck = false;
 var flag = false;
 var len =document.form1.chkCmp.length;
	for(var j=0;j<len;j++){
	 if( eval("document.form1.chkCmp[" + j + "].checked") ==true)
		{
			flag=true;
			break;
	 	}
    }
	if(len>0)
	{
		if(flag)
			{
	 			document.form1.action ="authCompany.jsp?action=Authorise";
	  			document.form1.submit(); 
			}
			else
			{  
				alert("Please select company to Authorise");
				return false;
			}
	}
	else
	{
		document.form1.action ="authCompany.jsp?action=Authorise";
		  document.form1.submit(); 
	}
}
function onClear(){

     document.form1.PLANT.value="";
     document.form1.STARTDATE.value="";
     document.form1.EXPIRYDATE.value="";
 }
 function OnClickData()
 {
 	document.form1.action="authCompany.jsp?action=ClearData";
 	document.form1.submit();
 } 

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>


<%



String plant = (String) session.getAttribute("PLANT");
String userName = StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim();

DateUtils dateUtils = new DateUtils();
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	PlantMstUtil  _PlantMstUtil       = new PlantMstUtil();
	_PlantMstUtil.setmLogger(mLogger);
	
	MovHisDAO movdao = new MovHisDAO();
	movdao.setmLogger(mLogger);
	ArrayList invQryList  = new ArrayList();
	String fieldDesc="";String action="";
	String PLANT="", FROM_DATE ="",  TO_DATE = "",STARTDATE="",EXPIRYDATE="",fdate="",tdate="",chkString="";
	String cntRec ="false";boolean result1=false;
	PlantMstDAO plntdao = new PlantMstDAO();
	plntdao.setmLogger(mLogger);
	action      = strUtils.fString(request.getParameter("action"));
	String PGaction      = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= strUtils.fString(request.getParameter("PLANT"));
	STARTDATE    = strUtils.fString(request.getParameter("STARTDATE"));
	EXPIRYDATE   = strUtils.fString(request.getParameter("EXPIRYDATE"));
	if(action.equalsIgnoreCase("clear"))
	{
		PLANT="";STARTDATE="";EXPIRYDATE="";
	}
	
	if(STARTDATE ==null) STARTDATE =""; else STARTDATE  = STARTDATE.trim();
	if (STARTDATE.length()>5)
		
	fdate    = STARTDATE.substring(6)+"-"+ STARTDATE.substring(3,5)+"-"+STARTDATE.substring(0,2);
		
	if(EXPIRYDATE==null) EXPIRYDATE=""; else EXPIRYDATE =  EXPIRYDATE.trim();
	if (EXPIRYDATE.length()>5)
	tdate    = EXPIRYDATE.substring(6)+"-"+ EXPIRYDATE.substring(3,5)+"-"+EXPIRYDATE.substring(0,2);
	try
	{
	    Hashtable ht = new Hashtable();
	    invQryList = _PlantMstUtil.getUnauthPlantList(ht,PLANT,fdate,tdate);
	    fieldDesc="";
	}catch(Exception e) { }
	if(action.equalsIgnoreCase("ClearData"))
	{
		Hashtable ht1 = new Hashtable();
		invQryList.clear();
		PGaction="";
	}
	else if (action.equalsIgnoreCase("Authorise"))
	{
	try{
	      Hashtable htCond = new Hashtable();
	     
	      Hashtable htm = new Hashtable();
          htm.put("PLANT",plant);
          htm.put("DIRTYPE",TransactionConstants.AUTHORISE_CMPY);
          htm.put("RECID","");
          htm.put("ITEM","");
          htm.put("CRBY",userName);
          htm.put("CRAT",dateUtils.getDateTime());
          htm.put(IDBConstants.TRAN_DATE,dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
	 	  String[] selcmp = request.getParameterValues("chkCmp");
	      StringBuffer updateQyery = new StringBuffer("SET");
	      updateQyery.append(" ");
	      updateQyery.append(" "+IDBConstants.AUTHSTAT +" = '"+ "1" + "'");
	      for(int i=0;i<selcmp.length;i++)
	      {
	     	htCond.put(IDBConstants.PLANT,selcmp[i]);
	     	htm.put("REMARKS",selcmp[i]);
	        boolean result = plntdao.update(updateQyery.toString(),htCond,"");  
	        if(result == true)
	        	 result1=movdao.insertIntodefaultMovHis(htm);
	      	if(i==selcmp.length-1)
	      	{
	        	response.sendRedirect("authCompany.jsp?action=result");
	      
	      	}
	      }    
	
	 }catch(Exception e) { }
	
	}
	else if (action.equalsIgnoreCase("result"))
	{
		fieldDesc ="<font class='maingreen'>"+"Company Authorised Sucessfully"+"</font>";
	}

%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
<FORM class="form-horizontal" name="form1" method="post" action="authCompany.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">

  
    <center><%=fieldDesc%></center>
  
  <br>
  
  <div style="overflow-x:auto;">
<table id="table" class="table table-bordered table-hover dataTable no-footer "
   role="grid" aria-describedby="tableInventorySummary_info" > 
   
   <thead style="background: #eaeafa;text-align: center">  
          <tr>  
            <th>Chk</th>  
            <th>Company Code</th> 
            <th>Description</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Authorised Status</th>
      <!--  <TH><font color="#ffffff" align="left"><b>UOM</TH> -->
          </tr>  
        </thead> 
        
        <tbody>
 

  
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
            Map lineArr = (Map) invQryList.get(iCnt);
            String trDate="";
            trDate = (String)lineArr.get("EXPIRYDATE");
            int iIndex = iCnt + 1;
            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
            if (trDate.length()>8)
            	trDate    = trDate.substring(8,10)+"-"+ trDate.substring(5,7)+"-"+trDate.substring(0,4);
          		chkString = (String)lineArr.get("PLANT");
          		String status = (String)lineArr.get("AUTHSTAT");
          		String msg="";
          		if(status.equalsIgnoreCase("0"))
          		{
        	  		msg="No";
          		}
          		else
          		{
        	  		msg="Yes";
          		}
       %>      
          
          <TR>
              <TD align="center" >&nbsp;<INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkCmp" value="<%=chkString%>"></TD>
               <TD align="center" class="textbold"> &nbsp;<a href ="plantDetail.jsp?action=View&PLANT=<%=(String)lineArr.get("PLANT")%>&PLNTDESC=<%=(String)lineArr.get("PLNTDESC")%>&STARTDATE=<%=(String)lineArr.get("STARTDATE")%>&EXPIRYDATE=<%=(String)lineArr.get("EXPIRYDATE")%>&ACTUALEXPIRYDATE=<%=(String)lineArr.get("ACTEXPIRYDATE")%>&NAME=<%=(String)lineArr.get("NAME")%>&EMAIL=<%=(String)lineArr.get("EMAIL")%>&TELNO=<%=(String)lineArr.get("TELNO")%>&REMARKS=<%=(String)lineArr.get("REMARKS")%>
               &FAX=<%=(String)lineArr.get("FAX")%>&HPNO=<%=(String)lineArr.get("HPNO")%>
              &DESGINATION=<%=(String)lineArr.get("DESGINATION")%>&ADD1=<%=(String)lineArr.get("ADD1")%>&ADD2=<%=(String)lineArr.get("ADD2")%>&ADD3=<%=(String)lineArr.get("ADD3")%>&ADD4=<%=(String)lineArr.get("ADD4")%>&COUNTY=<%=(String)lineArr.get("COUNTY")%>&ZIP=<%=(String)lineArr.get("ZIP")%>")%><%=(String)lineArr.get("PLANT")%></a></TD>
             <TD align= "center" class="textbold">&nbsp;<%=(String)lineArr.get("PLNTDESC") %></TD>
            <TD align="center" class="textbold">&nbsp;<%=(String)lineArr.get("STARTDATE") %></TD>
             <TD align= "center" class="textbold">&nbsp;<%=(String)lineArr.get("EXPIRYDATE") %></TD>
              <TD align= "center" class="textbold">&nbsp;<%=msg%></TD>
          </TR>
 
       <%}%>

    </tbody>  
</table>
</div>
    
     <br>
    <div class="form-group">        
     <div class="col-sm-12" align="center">
    <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	      <button type="button" class="Submit btn btn-default" value=" Authorise " onClick="return OnAuthData(document.form);"><b>Authorise</b></button>&nbsp;&nbsp;
      	      </div>
      	      </div>
    
    
  </FORM>
  </div>
  </div>
  </div>
  
<script>
$(document).ready(function(){
	$('#table').dataTable({
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
	});
    
    $('[data-toggle="tooltip"]').tooltip();
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
       
