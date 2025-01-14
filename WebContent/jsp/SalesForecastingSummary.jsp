<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Sales Forecasting";
%>
<%@include file="sessionCheck.jsp" %>
<%!@SuppressWarnings({"rawtypes", "unchecked"})%>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
    <jsp:param name="submenu" value="<%=IConstants.INVENTORY%>"/>
</jsp:include>
<style type="text/css">
.dt-button-collection.dropdown-menu{
left: 50px; !important;
}
</style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script>

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'SalesPerformance', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
{
    
//    document.form1.action = "/track/deleveryorderservlet?Submit=ExportOutboundSalesSummary";
//    document.form1.submit();

}


function onGo(){
document.form1.action="../inventory/salesforecasting";
document.form1.submit();
}
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
List saleList =null, stockin=null;
ShipHisDAO shipdao = new ShipHisDAO();
PlantMstDAO plantMstDAO = new PlantMstDAO();


String fieldDesc="";
String USERID ="",PLANT="",datawise ="",reportperiod = "",fdate="",tdate="";
String ITEM = "",ACTIVE="",PRD_CLS_ID="",MPERCENTAGE="";
boolean flag=false;

String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
ACTIVE    = StrUtils.fString(request.getParameter("ACTIVE"));
PRD_CLS_ID    = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
MPERCENTAGE    = StrUtils.fString(request.getParameter("MPERCENTAGE"));
datawise = StrUtils.fString(request.getParameter("datawise"));
reportperiod = StrUtils.fString(request.getParameter("reportperiod"));
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
Map map = (Map) al.get(0);
String collectionDate=DateUtils.getDate();
String CNAME = (String) map.get("PLNTDESC");
String ADD1 = (String) map.get("ADD1");
String ADD2 = (String) map.get("ADD2");
String ADD3 = (String) map.get("ADD3");
String ADD4 = (String) map.get("ADD4");
String STATE = (String) map.get("STATE");
String COUNTRY = (String) map.get("COUNTY");
String ZIP = (String) map.get("ZIP");
String TELNO = (String) map.get("TELNO");

String fromAddress_BlockAddress = ADD1 + " " + ADD2;
String fromAddress_RoadAddress = ADD3 + " " + ADD4;
String fromAddress_Country = STATE + " " + COUNTRY+" "+ZIP;

if(reportperiod.equals(""))
{
	reportperiod="YEARLY";
}
if(datawise.equals(""))
{
	datawise="PRODUCT";
}
if(ACTIVE.equals(""))
{
	ACTIVE="1";
}
if(MPERCENTAGE.equals(""))
{
	MPERCENTAGE="0";
}

String FDate1="",FDate2="",FDate3="",FDate4="",TDate1="",TDate2="",TDate3="",TDate4="";
	
String curDate =DateUtils.getDateMinusDays();
Calendar cal=Calendar.getInstance();
cal.add(Calendar.YEAR,3);
SimpleDateFormat simpleformat = new SimpleDateFormat("YYYY");
String year7= simpleformat.format(cal.getTime());
cal=Calendar.getInstance();
cal.add(Calendar.YEAR,2);
simpleformat = new SimpleDateFormat("YYYY");
String year6= simpleformat.format(cal.getTime());
cal=Calendar.getInstance();
cal.add(Calendar.YEAR,1);
simpleformat = new SimpleDateFormat("YYYY");
String year5= simpleformat.format(cal.getTime());
cal=Calendar.getInstance();
simpleformat = new SimpleDateFormat("YYYY");
String year4= simpleformat.format(cal.getTime());
cal=Calendar.getInstance();
cal.add(Calendar.YEAR,-1);
simpleformat = new SimpleDateFormat("YYYY");
String year3= simpleformat.format(cal.getTime());
cal=Calendar.getInstance();
cal.add(Calendar.YEAR,-2);
simpleformat = new SimpleDateFormat("YYYY");
String year2= simpleformat.format(cal.getTime());
cal=Calendar.getInstance();
cal.add(Calendar.YEAR,-3);
simpleformat = new SimpleDateFormat("YYYY");
String year1= simpleformat.format(cal.getTime());

//MONTH
cal=Calendar.getInstance();
cal.add(Calendar.MONTH,3);
simpleformat = new SimpleDateFormat("MMM YYYY");
String month7= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("YYYY");
String my7= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("MM");
String mm7= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.MONTH,2);
cal.add(Calendar.MONTH,2);
simpleformat = new SimpleDateFormat("MMM YYYY");
String month6= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("YYYY");
String my6= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("MM");
String mm6= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.MONTH,1);
simpleformat = new SimpleDateFormat("MMM YYYY");
String month5= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("YYYY");
String my5= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("MM");
String mm5= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
simpleformat = new SimpleDateFormat("MMM YYYY");
String month4= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("YYYY");
String my4= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("MM");
String mm4= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.MONTH,-1);
simpleformat = new SimpleDateFormat("MMM YYYY");
String month3= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("YYYY");
String my3= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("MM");
String mm3= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.MONTH,-2);
simpleformat = new SimpleDateFormat("MMM YYYY");
String month2= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("YYYY");
String my2= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("MM");
String mm2= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.MONTH,-3);
simpleformat = new SimpleDateFormat("MMM YYYY");
String month1= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("YYYY");
String my1= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("MM");
String mm1= simpleformat.format(cal.getTime());

//DAY
cal=Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, 3);
simpleformat = new SimpleDateFormat("dd/MM");
String day7= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, 2);
simpleformat = new SimpleDateFormat("dd/MM");
String day6= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, 1);
simpleformat = new SimpleDateFormat("dd/MM");
String day5= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, 0);
simpleformat = new SimpleDateFormat("dd/MM");
String day4= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate1= simpleformat.format(cal.getTime());
TDate1= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, -1);
simpleformat = new SimpleDateFormat("dd/MM");
String day3= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate2= simpleformat.format(cal.getTime());
TDate2= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, -2);
simpleformat = new SimpleDateFormat("dd/MM");
String day2= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate3= simpleformat.format(cal.getTime());
TDate3= simpleformat.format(cal.getTime());

cal=Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, -3);
simpleformat = new SimpleDateFormat("dd/MM");
String day1= simpleformat.format(cal.getTime());
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate4= simpleformat.format(cal.getTime());
TDate4= simpleformat.format(cal.getTime());


if(reportperiod.equals("YEARLY"))
{
cal = Calendar.getInstance();
cal.set(Calendar.YEAR, Integer.parseInt(year1));
cal.set(Calendar.DAY_OF_YEAR, 1);    
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate4= simpleformat.format(cal.getTime());

cal = Calendar.getInstance();
cal.set(Calendar.YEAR, Integer.parseInt(year1));
cal.set(Calendar.MONTH, 11); // 11 = december
cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve    
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
TDate4= simpleformat.format(cal.getTime());

cal = Calendar.getInstance();
cal.set(Calendar.YEAR, Integer.parseInt(year2));
cal.set(Calendar.DAY_OF_YEAR, 1);    
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate3= simpleformat.format(cal.getTime());

cal = Calendar.getInstance();
cal.set(Calendar.YEAR, Integer.parseInt(year2));
cal.set(Calendar.MONTH, 11); // 11 = december
cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve    
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
TDate3= simpleformat.format(cal.getTime());

cal = Calendar.getInstance();
cal.set(Calendar.YEAR, Integer.parseInt(year3));
cal.set(Calendar.DAY_OF_YEAR, 1);    
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate2= simpleformat.format(cal.getTime());

cal = Calendar.getInstance();
cal.set(Calendar.YEAR, Integer.parseInt(year3));
cal.set(Calendar.MONTH, 11); // 11 = december
cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve    
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
TDate2= simpleformat.format(cal.getTime());

cal = Calendar.getInstance();
cal.set(Calendar.YEAR, Integer.parseInt(year4));
cal.set(Calendar.DAY_OF_YEAR, 1);    
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate1= simpleformat.format(cal.getTime());

cal = Calendar.getInstance();
cal.set(Calendar.YEAR, Integer.parseInt(year4));
cal.set(Calendar.MONTH, 11); // 11 = december
cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve    
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
TDate1= simpleformat.format(cal.getTime());

} else if(reportperiod.equals("MONTHLY")) {
cal = Calendar.getInstance();
cal.set(Integer.parseInt(my1), Integer.parseInt(mm1)-1, 1);
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate4= simpleformat.format(cal.getTime());

cal.add(Calendar.MONTH, 1);  
cal.set(Calendar.DAY_OF_MONTH, 1); 
cal.add(Calendar.DATE, -1); 
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
TDate4= simpleformat.format(cal.getTime());

cal = Calendar.getInstance();
cal.set(Integer.parseInt(my2), Integer.parseInt(mm2)-1, 1);    
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate3= simpleformat.format(cal.getTime());

cal.add(Calendar.MONTH, 1);  
cal.set(Calendar.DAY_OF_MONTH, 1);  
cal.add(Calendar.DATE, -1); 
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
TDate3= simpleformat.format(cal.getTime());

cal = Calendar.getInstance();
cal.set(Integer.parseInt(my3), Integer.parseInt(mm3)-1, 1); 
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate2= simpleformat.format(cal.getTime());

cal.add(Calendar.MONTH, 1);  
cal.set(Calendar.DAY_OF_MONTH, 1);  
cal.add(Calendar.DATE, -1); 
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
TDate2= simpleformat.format(cal.getTime());

cal = Calendar.getInstance();
cal.set(Integer.parseInt(my4), Integer.parseInt(mm4)-1, 1);    
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
FDate1= simpleformat.format(cal.getTime());

cal.add(Calendar.MONTH, 1);  
cal.set(Calendar.DAY_OF_MONTH, 1);  
cal.add(Calendar.DATE, -1); 
simpleformat = new SimpleDateFormat("yyyyMMdd");//20200101
TDate1= simpleformat.format(cal.getTime());
	
}

ItemMstUtil itemMstUtil = new ItemMstUtil();

itemMstUtil.setmLogger(mLogger);
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);


boolean cntRec=false;

if(PGaction.equalsIgnoreCase("View")){
 try{
	 ITEM = itemMstUtil.isValidInvAlternateItemInItemmst( PLANT, ITEM);
 }catch(Exception e) { 
	 invQryList.clear();
	 cntRec=true;
	 
 }
}
%>
<div class="container-fluid m-t-20">
	<div class="box">
	<ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../inventory/reports"><span class="underline-on-hover">Inventory Reports</span></a></li>	
                <li><label>Sales Forecasting</label></li>                                   
            </ul>         
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
				<div class="box-title pull-right">
                  <div class="btn-group" role="group">
<!--                   <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data</button>  -->
				</div>
				&nbsp;
               <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../inventory/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="../inventory/salesforecasting">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
  
  <span style="text-align: center;"><small><font color="red"> <%=fieldDesc%> </font></small></span>
  
		<div class="form-group">
		<div>Search</div>
		<div class="row">
		<div class="col-sm-3">
			<select class="form-control" id="datawise" name="datawise"
				onchange="datewisechanged(this);">
				<option value="PRODUCT" <%if (datawise.equalsIgnoreCase("PRODUCT")) {%> selected <%}%>>ProductWise</option>
				<option value="CATEGORY" <%if (datawise.equalsIgnoreCase("CATEGORY")) {%> selected <%}%>>CategoryWise</option>
			</select>
		</div>		
		<div class="col-sm-3">
			<select class="form-control" id="reportperiod" name="reportperiod"
				onchange="datechanged(this);">
				<option value="YEARLY" <%if (reportperiod.equalsIgnoreCase("YEARLY")) {%> selected <%}%>>Yearly</option>
				<option value="MONTHLY" <%if (reportperiod.equalsIgnoreCase("MONTHLY")) {%> selected <%}%>>Monthly</option>
				<option value="DAILY" <%if (reportperiod.equalsIgnoreCase("DAILY")) {%> selected <%}%>>Daily</option>
			</select>
		</div>		
		<div class="col-sm-2">
  		<label class="radio-inline"> <input type="radio" name="ACTIVE" value="1" <%if (ACTIVE.equalsIgnoreCase("1")) {%> checked <%}%>>Auto
										</label> 
		<label class="radio-inline"> <input type="radio"name="ACTIVE" value="0" <%if (ACTIVE.equalsIgnoreCase("0")) {%> checked <%}%>>Manual
										</label>
     	</div>
		<div class="col-sm-3">
  		<INPUT class="form-control" name="MPERCENTAGE" id="MPERCENTAGE" type="TEXT" value="<%=MPERCENTAGE%>" placeholder="Manual Percentage" style="width:100%" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)">
  		</div>
  		</div>
  		<div class="row">
  		<div class="col-sm-12 txn-buttons" style="text-align: center;">
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
		</div>
		</div>
  		</div>
        
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tablePerformanceSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tablePerformanceSummary"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
       	  <th style="font-size: smaller;">S.NO</TH>
       	  <%if (datawise.equalsIgnoreCase("PRODUCT")) {%>
       	  <th style="font-size: smaller;">PRODUCT</TH>
          <% } else { %>
       	  <th style="font-size: smaller;">CATEGORY</TH>
       	  <%} if (reportperiod.equalsIgnoreCase("YEARLY")) {%>
          <th style="font-size: smaller;"><%=year1%></TH>
          <th style="font-size: smaller;"><%=year2%></TH>
          <th style="font-size: smaller;"><%=year3%></TH>
          <th style="font-size: smaller;"><%=year4%></TH>
          <th style="font-size: smaller;"><%=year5%></TH>
          <th style="font-size: smaller;"><%=year6%></TH>
          <th style="font-size: smaller;"><%=year7%></TH>
          <% } else if (reportperiod.equalsIgnoreCase("YEARLY")) { %>
          <th style="font-size: smaller;"><%=month1%></TH>
          <th style="font-size: smaller;"><%=month2%></TH>
          <th style="font-size: smaller;"><%=month3%></TH>
          <th style="font-size: smaller;"><%=month4%></TH>
          <th style="font-size: smaller;"><%=month5%></TH>
          <th style="font-size: smaller;"><%=month6%></TH>
          <th style="font-size: smaller;"><%=month7%></TH>
          <% } else { %>
          <th style="font-size: smaller;"><%=day1%></TH>
          <th style="font-size: smaller;"><%=day2%></TH>
          <th style="font-size: smaller;"><%=day3%></TH>
          <th style="font-size: smaller;"><%=day4%></TH>
          <th style="font-size: smaller;"><%=day5%></TH>
          <th style="font-size: smaller;"><%=day6%></TH>
          <th style="font-size: smaller;"><%=day7%></TH>
          <% } %>
                     </tr>
		            </thead>
				</table>
            		</div>
						</div>
					</div>
  </div>
<script>
  var tableData = [];
    
       <%
	   invQryList = shipdao.getsalesforecasting(PLANT,datawise,FDate1,TDate1,FDate2,TDate2,FDate3,TDate3,FDate4,TDate4);
       if(invQryList.size() <=0)
       {
     	  cntRec =true;
         fieldDesc="Data's Not Found";
       }
          int j=0;
          double Totalqty=0;
          double GP=0;
          double Totalprice=0;
          String rowColor="";
          DecimalFormat decformat = new DecimalFormat("#,##0.00");
          Hashtable htship = new Hashtable();
          htship.put("PLANT",PLANT);
          Hashtable htexp = new Hashtable();
          shipdao.setmLogger(mLogger);
          int total_stock_claim = 0;
              for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
            	 rowColor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#FFFFFF"; 
                j=j+1;
                Map lineArr = (Map) invQryList.get(iCnt);
                String trDate="";
                double PYEAR1 = Double.parseDouble((String)lineArr.get("PYEAR1"));
                double PYEAR2 = Double.parseDouble((String)lineArr.get("PYEAR2"));
                double PYEAR3 = Double.parseDouble((String)lineArr.get("PYEAR3"));
                //double CYEAR = Double.parseDouble((String)lineArr.get("CYEAR"));
                double Diff1 =PYEAR2-PYEAR1;
                double Diff2 =PYEAR3-PYEAR2;
                //double Diff3 =PYEAR3-CYEAR;
                double DiffPer1 =PYEAR1/100;
                double DiffPer2 =PYEAR2/100;
                //double DiffPer3 =PYEAR3/100;
                //System.out.println(Diff1+","+Diff2+","+DiffPer1+","+DiffPer2);
                double Diff =0.00;
                if (ACTIVE.equalsIgnoreCase("1")) {
                double DVal1 =0.00;
                double DVal2 =0.00;
                if(DiffPer1>0)
                	DVal1 =(Diff1/DiffPer1);
                if(DiffPer2>0)
                	DVal2 =(Diff2/DiffPer2);
                Diff =(DVal1+DVal2);
                //System.out.println(Diff);
                Diff =Diff/3;
                } else {
                Diff = Double.parseDouble(MPERCENTAGE);
                }
                double CYEAR =PYEAR3 + (PYEAR3/100)*Diff;
                double NYEAR1 =CYEAR + (CYEAR/100)*Diff;
                double NYEAR2 =NYEAR1 + (NYEAR1/100)*Diff;
                double NYEAR3 =NYEAR2 + (NYEAR2/100)*Diff;
                //System.out.println(NYEAR1+","+NYEAR2+","+NYEAR3+","+Diff);
          %>
          
          var rowData = [];
          rowData[rowData.length] = '<%=j%>';
          rowData[rowData.length] = '<%=(String)lineArr.get("ITEM") %>';
          rowData[rowData.length] = '<%=StrUtils.addZeroes(PYEAR1, "3")%>';
          rowData[rowData.length] = '<%=StrUtils.addZeroes(PYEAR2, "3")%>';
          rowData[rowData.length] = '<%=StrUtils.addZeroes(PYEAR3, "3")%>';
          rowData[rowData.length] = '<%=StrUtils.addZeroes(CYEAR, "3")%>';
          rowData[rowData.length] = '<%=StrUtils.addZeroes(NYEAR1, "3")%>';
          rowData[rowData.length] = '<%=StrUtils.addZeroes(NYEAR2, "3")%>';
          rowData[rowData.length] = '<%=StrUtils.addZeroes(NYEAR3, "3")%>';
          tableData[tableData.length] = rowData;
          <%}%>    
         
    $(document).ready(function(){
   	 $('#tablePerformanceSummary').DataTable({
   		"lengthMenu": [[50, 100, 500], [50, 100, 500]],
   		  	data: tableData,
   		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
   			"<'row'<'col-md-6'><'col-md-6'>>" +
   			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
   			"columnDefs": [{"className": "t-right", "targets": [2, 3, 4,5,6,7,8]}],
   			buttons: [
	        	{
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend : 'excel',
	                    	exportOptions: {
	    	                	columns: [':visible']
	    	                },
	    	                title: '<%=title%>',
	    	                footer: true
	                    },
	                    {
	                    	extend : 'pdf',
                            footer: true,
	                    	text: 'PDF Portrait',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
	                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
	                    	<%} else {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
	                    	<%}%>
                    		orientation: 'portrait',
	                    	customize: function(doc) {
	                    		doc.defaultStyle.fontSize = 7;
                     	        doc.styles.tableHeader.fontSize = 7;
                     	        doc.styles.title.fontSize = 10;
                     	       doc.styles.tableFooter.fontSize = 7;
	                    	},
                            pageSize: 'A4'
	                    },
	                    {
	                    	extend : 'pdf',
	                    	footer: true,
	                    	text: 'PDF Landscape',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
	                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
	                    	<%} else {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
	                    	<%}%>
                    		orientation: 'landscape',
                    		customize: function(doc) {
	                    		doc.defaultStyle.fontSize = 6;
                     	        doc.styles.tableHeader.fontSize = 6;
                     	        doc.styles.title.fontSize = 8;                     	       
                     	        doc.content[1].table.widths = "*";
                     	       doc.styles.tableFooter.fontSize = 7;
	                    	     },
                            pageSize: 'A4'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                    columns: ':not(:eq(1))'
                }
	        ],
				"order":[],
	   	        "drawCallback": function ( settings ) {
   	        } 
   		  });	 
   });

   </script>
   
  </FORM>
  </div></div></div>
 <script>
 $(document).ready(function(){
    var plant= '<%=PLANT%>';
	
	/* Product Number Auto Suggestion */
	$('#ITEM').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.ITEM+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});

	/* Product Number Auto Suggestion */
	$('#PRD_CLS_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_CLS_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTCLASS_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_CLASS_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
				return '<div onclick="document.form1.PRD_CLS_ID.value = \''+data.PRD_CLS_ID+'\'"><p class="item-suggestion">' + data.PRD_CLS_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_CLS_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});

	
 });
 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>