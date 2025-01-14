
<!-- PAGE CREATED BY : IMTHI -->
<!-- DATE 22-02-2023 -->
<!-- DESC : SALES PERFORMANCE SUMMARY-->
<!-- URL : salesorder/salesperformance-->

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
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Sales Performance Summary";
%>
<%@include file="sessionCheck.jsp" %>
<%!@SuppressWarnings({"rawtypes", "unchecked"})%>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALES_REPORTS%>"/>
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
document.form1.action="../salesorder/salesperformance";
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
String USERID ="",PLANT="",FROM_DATE ="",TO_DATE = "",fdate="",tdate="";
String ITEM = "",CUSTOMER="",PRD_DEPT_ID="",PRD_CLS_ID="",PRD_TYPE_ID="",PRD_BRAND_ID="",Loc="",SORT="",POSSEARCH="1";
boolean flag=false;

String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
PRD_DEPT_ID    = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
PRD_CLS_ID    = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID    = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID    = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
String[] chkdLoc = request.getParameterValues("chkdLoc");
CUSTOMER    = StrUtils.fString(request.getParameter("CUSTOMER"));
SORT = StrUtils.fString(request.getParameter("SORT"));
FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
String ENABLE_POS = plantMstDAO.getispos(PLANT);
POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
if(POSSEARCH.equalsIgnoreCase("") || POSSEARCH.equalsIgnoreCase("null")){
	if(ENABLE_POS.equals("1"))
		POSSEARCH="3";
	else
		POSSEARCH="1";
}
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
	
String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;
if(SORT.equals(""))
{
	SORT="SELLING";
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
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                                 
                <li><label>Sales Performance Summary</label></li>                                   
            </ul>             
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
				<div class="box-title pull-right">
                  <div class="btn-group" role="group">
<!--                   <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data</button>  -->
				</div>
				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="../salesorder/salesperformance">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="POSSEARCH" value="<%=StrUtils.forHTMLTag(POSSEARCH)%>">
  
  <span style="text-align: center;"><small><font color="red"> <%=fieldDesc%> </font></small></span>
  
		<div id="target" style="padding: 18px; display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" id="FROM_DATE" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="CUSTOMER NAME" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>">				
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control" placeholder="PRODUCT" value="<%=StrUtils.forHTMLTag(ITEM)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeproduct(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>				
  		</div>
		<div class="col-sm-4 ac-box">
  		<INPUT class="ac-selected  form-control typeahead" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT"  placeholder="PRODUCT DEPARTMENT" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>" size="30"  MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprddeptid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
		<div class=""> 
		<INPUT class="ac-selected  form-control typeahead" name="PRD_CLS_ID" ID="PRD_CLS_ID" type = "TEXT" placeholder="PRODUCT CATEGORY" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" size="30"  MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdclsid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>				
  		</div>
		<div class="col-sm-4 ac-box">
  		<INPUT class="ac-selected  form-control typeahead" name="PRD_TYPE_ID" ID="PRD_TYPE_ID" type = "TEXT"  placeholder="PRODUCT SUB CATEGORY" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" size="30"  MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdtypeid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
		<div class=""> 
		<INPUT class="ac-selected  form-control typeahead" name="PRD_BRAND_ID" ID="PRD_BRAND_ID" type = "TEXT"  placeholder="PRODUCT BRAND" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" size="30"  MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdbrdid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>				
  		</div>
		<div class="col-sm-4 ac-box">
  		<label class="radio-inline">
      	<INPUT  name="SORT" id="SORT" type = "radio" value="SELLING"    <%if(SORT.equalsIgnoreCase("SELLING")) {%>checked <%}%>>Best Selling Product</label>
   		<label class="radio-inline">
   		<INPUT name="SORT" id="SORT" type = "radio"  value="MOVING"   <%if(SORT.equalsIgnoreCase("MOVING")) {%>checked <%}%>>Slow Moving Product</label>
  		</div>
  		</div>
  		
  		<div class="row" style="padding: 0px;">
  		<div class="col-sm-2">
  		</div>
  		<label>
  			<input type="checkbox" class="form-check-input" style="border:0;" name="select" value="select" onclick="return checkAll(this.checked);">
			&nbsp; Select/Unselect All &nbsp;
			</label>
		
		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-8 ac-box">
		<div class=""> 
		<table id="table" class="table table-bordred table-striped" > 
   
   <thead style="text-align: center"> 
          <tr>  
            <th style="font-size: smaller;">Chk</th>  
            <th style="font-size: smaller;">Location</th>
          </tr>  
        </thead> 
						<tbody>
						<%
						Hashtable ht = new Hashtable();
						ArrayList locQryList =    new LocUtil().getAllLocDetails(PLANT,"","");
						if (locQryList.size() > 0) {
						for(int i =0; i<locQryList.size(); i++) {
							Map arrCustLine = (Map) locQryList.get(i);
							Loc = (String)arrCustLine.get("LOC");
							if (chkdLoc!=null){
								String ischecked="";
								for (int j = 0; j < chkdLoc.length; j++) {
									String location = chkdLoc[j].toString().replaceAll("\\[|\\]", "");
									if(Loc.equalsIgnoreCase(location)) {
										ischecked="checked";
										break;
									}
								}
								%>
							    <tr>
							      <td><INPUT Type=checkbox style=border: 0; name=chkdLoc id=chkdLoc value="<%=Loc%>" <%=ischecked%> ></td>
							      <td><%=Loc%></td>
								<%
							} else {
								%>
							    <tr>
							      <td><INPUT Type=checkbox style=border: 0; name=chkdLoc id=chkdLoc value="<%=Loc%>" ></td>
							      <td><%=Loc%></td>
													<%
							}
						
						}
						}
						%>
    </tr>
  </tbody>
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
</table>
  		</div>				
  		</div>
  		</div>
	
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-8 txn-buttons" style="text-align: center;">
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
			</div>
		</div>
  		</div>
		</div>
        
        <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
  	</div>
        </div>
       	  </div>
       	  
       	  <div class="form-group">
       	  <% if(ENABLE_POS.equals("1")){ %>
					<label class="control-label col-sm-1" for="view">View By :</label>
				  	<label class="radio-inline">
  					<input name="POSSTATUS" type="radio" value="1" id="all" <%if(POSSEARCH.equalsIgnoreCase("1")) {%>checked <%}%> onclick="changepostype(this.value)"> <b>Both Sales</b></label>
  					<label class="radio-inline">
  					<input name="POSSTATUS" type="radio" value="2" id="done" <%if(POSSEARCH.equalsIgnoreCase("2")) {%>checked <%}%> onclick="changepostype(this.value)"> <b>ERP Sales</b></label>
  					<label class="radio-inline">
  					<input name="POSSTATUS" type="radio" value="3" id="notdone" <%if(POSSEARCH.equalsIgnoreCase("3")) {%>checked <%}%> onclick="changepostype(this.value)"> <b>POS Sales</b></label>
  		 <% } else {%>
  					<input name="POSSTATUS" type="radio" hidden value="1" id="all" <%if(POSSEARCH.equalsIgnoreCase("1")) {%>checked <%}%> onclick="changepostype(this.value)">
  		 <% } %>
		</div>
			
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tablePerformanceSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tablePerformanceSummary"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
       	  <th style="font-size: smaller;">PRODUCT</TH>
          <th style="font-size: smaller;">DESCRIPTION</TH>
          <th style="font-size: smaller;">DEPARTMENT</TH>
          <th style="font-size: smaller;">CATEGORY</TH>
          <th style="font-size: smaller;">BRAND</TH>
          <th style="font-size: smaller;">QTY</TH>
          <th style="font-size: smaller;">VALUE</TH>
          <th style="font-size: smaller;">GP %</TH>
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
	   invQryList = shipdao.getSalesPerformanceSummary(PLANT,ITEM,PRD_DEPT_ID,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,CUSTOMER,SORT,FROM_DATE,TO_DATE,chkdLoc,POSSEARCH);
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
                double TOTPRICE = Double.parseDouble((String)lineArr.get("TOTPRICE"));
                TOTPRICE = StrUtils.RoundDB(TOTPRICE,5);
                
                Totalqty = Totalqty+Double.parseDouble((String)lineArr.get("TOTQTY"));
                Totalqty = StrUtils.RoundDB(Totalqty,2);
                
                GP = Totalqty+Double.parseDouble((String)lineArr.get("TOTCOST"));
                GP = (GP-TOTPRICE)/(GP*100);
//                 double Gp = Math.abs(GP);
                
                Totalprice = Totalprice+Double.parseDouble((String)lineArr.get("TOTPRICE"));
                Totalprice = StrUtils.RoundDB(Totalprice,5);
              
                String totQtyValue = (String)lineArr.get("TOTQTY");
                String totalPriceValue = String.valueOf(TOTPRICE);
                String gp = String.valueOf(GP);
                
                float totQtyVal="".equals(totQtyValue) ? 0.0f :  Float.parseFloat(totQtyValue);
                double totalPriceVal ="".equals(totalPriceValue) ? 0.0d :  Double.parseDouble(totalPriceValue);
                
                if (totQtyVal == 0f) {
                	totQtyValue = "0.000";
				} else {
					totQtyValue = totQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
				}
                String qtys = String.valueOf(totQtyVal);
                qtys = StrUtils.addZeroes(totQtyVal, "3");
                totalPriceValue = StrUtils.addZeroes(totalPriceVal, numberOfDecimal);
                gp = StrUtils.addZeroes(GP, "3");
                if(gp.contains("-")){
	                gp = gp.replace("-", "");
                }else{
                	gp = gp;
                }
          %>
          
          var rowData = [];
          rowData[rowData.length] = '<%=(String)lineArr.get("ITEM") %>';
          rowData[rowData.length] = '<%=(String)lineArr.get("NAME")%>';
          rowData[rowData.length] = '<%=(String)lineArr.get("DEPT")%>';
          rowData[rowData.length] = '<%=(String)lineArr.get("CLASS")%>';
          rowData[rowData.length] = '<%=(String)lineArr.get("BRAND")%>';
          rowData[rowData.length] = '<%=qtys%>';
          rowData[rowData.length] = '<%=totalPriceValue%>';
          rowData[rowData.length] = '<%=gp%>';
          tableData[tableData.length] = rowData;
          <%}%>    
         
    $(document).ready(function(){
   	 $('#tablePerformanceSummary').DataTable({
   		"lengthMenu": [[50, 100, 500], [50, 100, 500]],
   		  	data: tableData,
   		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
   			"<'row'<'col-md-6'><'col-md-6'>>" +
   			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
   			"columnDefs": [{"className": "t-right", "targets": [5,6,7]}],
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
                      extend: 'colvis'
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

 function changecustomer(obj){
	 $("#CUSTOMER").typeahead('val', '"');
	 $("#CUSTOMER").typeahead('val', '');
	 $("#CUSTOMER").focus();
	}
 
 function changeproduct(obj){
	 $("#ITEM").typeahead('val', '"');
	 $("#ITEM").typeahead('val', '');
	 $("#ITEM").focus();
	}

 function changeprddeptid(obj){
	 $("#PRD_DEPT_ID").typeahead('val', '"');
	 $("#PRD_DEPT_ID").typeahead('val', '');
	 $("#PRD_DEPT_ID").focus();
	}

 function changeprdclsid(obj){
	 $("#PRD_CLS_ID").typeahead('val', '"');
	 $("#PRD_CLS_ID").typeahead('val', '');
	 $("#PRD_CLS_ID").focus();
	}

 function changeprdtypeid(obj){
	 $("#PRD_TYPE_ID").typeahead('val', '"');
	 $("#PRD_TYPE_ID").typeahead('val', '');
	 $("#PRD_TYPE_ID").focus();
	}

 function changeprdbrdid(obj){
	 $("#PRD_BRAND_ID").typeahead('val', '"');
	 $("#PRD_BRAND_ID").typeahead('val', '');
	 $("#PRD_BRAND_ID").focus();
	}

 
 $(document).ready(function(){

    $('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('.Hide').click(function() {
	    $('#target').hide(500);
	    $('.ShowSingle').show(0);
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
    if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('.Show').click();
    }else{
    	$('.Hide').click();
    }
    var plant= '<%=PLANT%>';
	/* Customer Auto Suggestion */
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_CUSTOMER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTMST);
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
		    return '<div><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		});
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
				//ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION_REPORT",
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

	/* Product Dept Auto Suggestion */
	$('#PRD_DEPT_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'prd_dep_id',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTDEPARTMENTID_FOR_SUMMARY",
				PRODUCTDEPARTMENTID : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTOMERTYPELIST);
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
				return '<div onclick="document.form1.PRD_DEPT_ID.value = \''+data.prd_dep_id+'\'"><p class="item-suggestion">' + data.prd_dep_id + '</p><br/><p class="item-suggestion">DESC: '+data.prd_dep_desc+'</p></div>';
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
	
	/* Product Type Number Auto Suggestion */
	$('#PRD_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_TYPE_MST);
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
				return '<div onclick="document.form1.PRD_TYPE_ID.value = \''+data.PRD_TYPE_ID+'\'"><p class="item-suggestion">' + data.PRD_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_TYPE_DESC+'</p></div>';
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
	
	/* Product Type Number Auto Suggestion */
	$('#PRD_BRAND_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_BRAND_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTBRAND_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_BRAND_MST);
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
				return '<div onclick="document.form1.PRD_BRAND_ID.value = \''+data.PRD_BRAND_ID+'\'"><p class="item-suggestion">' + data.PRD_BRAND_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
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

	function checkAll(isChk)
 	{
 		var len = document.form1.chkdLoc.length;
 		 var orderLNo; 
 		 if(len == undefined) len = 1;  
 	    if (document.form1.chkdLoc)
 	    {
 	        for (var i = 0; i < len ; i++)
 	        {      
 	              	if(len == 1){
 	              		document.form1.chkdLoc.checked = isChk;
 	               	}
 	              	else{
 	              		document.form1.chkdLoc[i].checked = isChk;
 	              	}
 	            	
 	        }
 	    }
 	}
	 function changepostype(count){
		  $("input[name ='POSSEARCH']").val(count);
		  onGo();
		}
 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>