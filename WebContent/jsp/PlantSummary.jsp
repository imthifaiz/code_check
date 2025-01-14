<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Company Summary";
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
function onClear(){

     document.form1.PLANT.value="";
     document.form1.STARTDATE.value="";
     document.form1.EXPIRYDATE.value="";
 }
 function OnClickData()
 {
 document.form1.action="PlantSummary.jsp?action=ClearData";
 document.form1.submit();
 } 
</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<%
DateUtils dateutil = new DateUtils();
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
PlantMstUtil  _PlantMstUtil       = new PlantMstUtil();
ArrayList invQryList  = new ArrayList();

String fieldDesc="";String action="";
String PLANT="", FROM_DATE ="",  TO_DATE = "",STARTDATE="",EXPIRYDATE="",fdate="",tdate="";
String cntRec ="false",addr1="",addr2="",addr3="",addr4="";

action      = strUtils.fString(request.getParameter("action"));
String PGaction      = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= strUtils.fString(request.getParameter("PLANT"));
STARTDATE    = strUtils.fString(request.getParameter("STARTDATE"));
EXPIRYDATE   = strUtils.fString(request.getParameter("EXPIRYDATE"));
if(action.equalsIgnoreCase("clear"))
{
PLANT="";STARTDATE="";EXPIRYDATE="";
}
String curDate =dateutil.getDate();

if(STARTDATE ==null) STARTDATE =""; else STARTDATE  = STARTDATE.trim();
if (STARTDATE.length()>5)
fdate    = STARTDATE.substring(6)+"-"+ STARTDATE.substring(3,5)+"-"+STARTDATE.substring(0,2);

if(EXPIRYDATE==null) EXPIRYDATE=""; else EXPIRYDATE =  EXPIRYDATE.trim();
if (EXPIRYDATE.length()>5)
tdate    = EXPIRYDATE.substring(6)+"-"+ EXPIRYDATE.substring(3,5)+"-"+EXPIRYDATE.substring(0,2);


if(action.equalsIgnoreCase("ClearData"))
{
	Hashtable ht1 = new Hashtable();
	invQryList.clear();
	PGaction="";
}

if(PGaction.equalsIgnoreCase("View")){
try{
     Hashtable ht = new Hashtable();
     invQryList = _PlantMstUtil.getauthPlantList(ht,PLANT,fdate,tdate);
     fieldDesc="";
       
     if(invQryList.size()<=0)
     {
       cntRec ="true";
       fieldDesc="Data Not Found";
     }
 }catch(Exception e) { }
}

%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
 <div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div> 

<FORM class="form-horizontal" name="form1" method="post" action="PlantSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">

 <div class="form-group">
  <label class="control-label col-sm-1" for="Company">Company:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control"  name="PLANT" type = "TEXT" value="<%=PLANT%>" size="15"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('PlantMstListSummary.jsp?PLANT='+form1.PLANT.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Company Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
 <div class="form-inline">
        <label class="control-label col-sm-1"  for="Start Date">Start Date:</label>
         <div class="col-sm-2">
      	  <div class="input-group">    
    		<input class="form-control datepicker" name="STARTDATE"  type = "TEXT" value="<%=STARTDATE%>" size="20"  MAXLENGTH=20  READONLY>
   		 	</div>
  		      </div>
  		        </div>
  		        
<div class="form-inline">
            <label class="control-label col-sm-1"  for="Expiry Date">Expiry Date:</label>
              <div class="col-sm-2">
      	      <div class="input-group">    
    		<input class="form-control datepicker" name="EXPIRYDATE"  type = "TEXT" value="<%=EXPIRYDATE%>" size="20"  MAXLENGTH=20 READONLY>
   		 	</div>
  		      </div>
  		      <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;&nbsp;
      	      <button type="button" class="Submit btn btn-default"onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
  		        </div>
       </div>
       
       
 <br>
 <div style="overflow-x:auto;">
<table id="table" class="table table-bordered table-hover dataTable no-footer "
   role="grid" aria-describedby="tableInventorySummary_info" > 
   
   <thead style="background: #eaeafa;text-align: center">  
          <tr>  
            <th >S/N</th>  
            <th>Company Code</th> 
            <th>Description</th>
            <th>Start Date</th>
            <th>End Date</th>   
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
            addr1 = strUtils.replaceCharacters2Send((String)lineArr.get("ADD1"));
            addr2 = strUtils.replaceCharacters2Send((String)lineArr.get("ADD2"));
            addr3 = strUtils.replaceCharacters2Send((String)lineArr.get("ADD3"));
            addr4 = strUtils.replaceCharacters2Send((String)lineArr.get("ADD4"));
       %>      
         <TR>
              <TD align="center" class="textbold">&nbsp;<%=iIndex%></TD>
             <!-- <TD align= "center"><%=(String)lineArr.get("PLANT")%></TD>-->
              <!--<TD align="center"> <a href = "PlantMst.jsp?action=view&ITEM=<%=(String)lineArr.get("mtid")%>")%><%=(String)lineArr.get("mtid")%></a></TD>
              <TD align="center"> <a href = "PlantMst.jsp?action=view&LOT=<%=(String)lineArr.get("LOT")%>&TRAVELER=<%=(String)lineArr.get("TRAVELER")%>&SKU=<%=(String)lineArr.get("SKU")%>&PALLET=<%=(String)lineArr.get("PALLET")%>&LOC=<%=(String)lineArr.get("LOC1")%>")%><%=(String)lineArr.get("LOT")%></a></TD>-->
               <TD align="center" class="textbold"> &nbsp;<a href ="plantDetail.jsp?action=View&PLANT=<%=(String)lineArr.get("PLANT")%>
               &PLNTDESC=<%=(String)lineArr.get("PLNTDESC")%>&STARTDATE=<%=(String)lineArr.get("STARTDATE")%>
               &EXPIRYDATE=<%=(String)lineArr.get("EXPIRYDATE")%>&ACTUALEXPIRYDATE=<%=(String)lineArr.get("ACTEXPIRYDATE")%>&NAME=<%=(String)lineArr.get("NAME")%>
               &EMAIL=<%=(String)lineArr.get("EMAIL")%>&TELNO=<%=(String)lineArr.get("TELNO")%>&REMARKS=<%=(String)lineArr.get("REMARKS")%>
               &FAX=<%=(String)lineArr.get("FAX")%>&HPNO=<%=(String)lineArr.get("HPNO")%>
               &DESGINATION=<%=(String)lineArr.get("DESGINATION")%>&ADD1=<%=addr1%>&ADD2=<%=addr2%>&ADD3=<%=addr3%>&ADD4=<%=addr4%>&COUNTY=<%=(String)lineArr.get("COUNTY")%>
              &ZIP=<%=(String)lineArr.get("ZIP")%>&SALES=<%=(String)lineArr.get("SALES_CHARGE_BY")%>
              &SALESPERCENT=<%=(String)lineArr.get("SALES_PERCENT")%>&SDOLLARFRATE=<%=(String)lineArr.get("SALES_FR_DOLLARS")%>
              &SCENTSFRATE=<%=(String)lineArr.get("SALES_FR_CENTS")%>&EDOLLARFRATE=<%=(String)lineArr.get("ENQUIRY_FR_DOLLARS")%>&ECENTSFRATE=<%=(String)lineArr.get("ENQUIRY_FR_CENTS")%>
              &NOOFCATALOGS=<%=(String)lineArr.get("NUMBER_OF_CATALOGS")%>&NOOFSIGNATURES=<%=(String)lineArr.get("NUMBER_OF_SIGNATURES")%>
              &BASECURRENCY=<%=(String)lineArr.get("BASE_CURRENCY")%>&TAXBY=<%=(String)lineArr.get("TAXBY")%>&TAXBYLABEL=<%=(String)lineArr.get("TAXBYLABEL")%>
               &STATE=<%=(String)lineArr.get("STATE")%>")%><%=(String)lineArr.get("PLANT")%></a></TD>
             <TD align= "center" class="textbold">&nbsp;<%=(String)lineArr.get("PLNTDESC") %></TD>
            <TD align="center" class="textbold">&nbsp;<%=(String)lineArr.get("STARTDATE") %></TD>
             <TD align= "center" class="textbold">&nbsp;<%=(String)lineArr.get("EXPIRYDATE") %></TD>
           </TR>
            
       <%}%>

    </tbody>  
</table>
</div>

<br>
    <div class="form-group">        
     <div class="col-sm-12" align="center">
    <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	      <button type="button" class="Submit btn btn-default" onClick="OnClickData();"><b>Clear</b></button>&nbsp;&nbsp;
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
