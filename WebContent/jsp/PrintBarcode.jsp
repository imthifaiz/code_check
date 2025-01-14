<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@page import="com.track.util.http.HttpUtils"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.MasterDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@page import="com.track.dao.ItemMstDAO"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />

<%
String title = "Print Barcode";
String rootURI = HttpUtils.getRootURI(request);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
  <jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>

<script language="javascript">

</script>

<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script src="<%=rootURI %>/jsp/js/jspdf.debug.js"></script>
<script src="<%=rootURI %>/jsp/js/jspdf.plugin.autotable.js"></script>
<script src="<%=rootURI %>/jsp/js/JsBarcode.all.js"></script>
<script src="<%=rootURI %>/jsp/js/PrintBarcode.js"></script>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
StrUtils _strUtils = new StrUtils();
	Generator generator = new Generator();
	HTReportUtil movHisUtil = new HTReportUtil();
	DateUtils _dateUtils = new DateUtils();
	ArrayList movQryList = new ArrayList();
	ArrayList movItemQryList = new ArrayList();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	movHisUtil.setmLogger(mLogger);
	
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
        String userID = (String) session.getAttribute("LOGIN_USER");
        String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
        String systatus = session.getAttribute("SYSTEMNOW").toString();
        String printdate=DateUtils.getDateFormatYYMMDD();
        ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
        Map map = (Map) al.get(0);
        String CNAME = (String) map.get("PLNTDESC");
        String CHPNO = (String) map.get("HPNO");
        String CEMAIL = (String) map.get("EMAIL");
        if(!CEMAIL.equalsIgnoreCase(""))
        	CEMAIL="E-Mail : "+CEMAIL;
        if(!CHPNO.equalsIgnoreCase(""))
        	CHPNO="Customer Care : "+CHPNO;
        int totrecqty =0,icount=0;
        String LabelType="Single",BarcodeWidth="4",BarcodeHeight="40",FontSize="25",TextAlign="Left",TextPosition="Bottom",DisplayText="Show";
        String printtype="";
        String sepratedtoken = "";
		String sepratedtoken1 = "";
		String MANUAL_DESC="",MANUAL_BARCODE="",MANUAL_QUANTITY="0";
		String ITEM="",ITEM_DESC="",UNITPRICE="",PRD_CLS_ID="",SELLING_PRICE="",ITEM_QUANTITY="0",MODEL="",printwithtmodel="",printWithPLANT="",printwithtdate="",PRINTDATE_FORMAT="",PRINTDATE_LABEL="",PRINT_WITH_SIZE="0",SIZE_LABEL="SIZE",SIZE="",printwithduplicate="0";
		Map mp = null;
		mp = new HashMap();
		String fieldDesc=StrUtils.fString(request.getParameter("result"));
        String PAGE_TYPE = (String)request.getAttribute("PAGE_TYPE");
       String printwithpobill = StrUtils.fString(request.getParameter("printwithpobill"));
       String printwithbatch = StrUtils.fString(request.getParameter("printwithbatch"));
       String printwithproduct = StrUtils.fString(request.getParameter("printwithproduct"));
       String printwithlot = StrUtils.fString(request.getParameter("printwithlot"));
       System.out.println(printwithpobill);
       String totalString = StrUtils.fString(request.getParameter("TRAVELER"));
		System.out.println(totalString);
		printtype = (String)request.getAttribute("printtype");
		ArrayList arrCust =new MasterDAO().getBarcodePrint(PAGE_TYPE, plant, "");
		if (arrCust.size() > 0) {
            for(int i =0; i<arrCust.size(); i++) {
            	Map arrCustLine = (Map)arrCust.get(i);
            	LabelType=(String)arrCustLine.get("LABEL_TYPE");
            	BarcodeWidth=(String)arrCustLine.get("BARCODE_WIDTH");
            	BarcodeHeight=(String)arrCustLine.get("BARCODE_HEIGHT");
            	FontSize=(String)arrCustLine.get("FONT_SIZE");
            	TextAlign=(String)arrCustLine.get("TEXT_ALIGN");
            	TextPosition=(String)arrCustLine.get("TEXT_POSITION");
            	DisplayText=(String)arrCustLine.get("DISPLAY_BARCODE_TEXT");
            }            
            }
		String returl ="../inhouse/genreceiptbarcode";
		
		if(PAGE_TYPE.equalsIgnoreCase("LOCATION"))
			returl ="../inhouse/genlocationbarcode";
		else if(PAGE_TYPE.equalsIgnoreCase("MULTIPLEPRODUCT")){
        	SIZE_LABEL="";
        	printdate="";
        	PRINTDATE_LABEL="";
        	SELLING_PRICE = request.getParameter("SELLING_PRICE");
        	
        	printWithPLANT = request.getParameter("printWithPLANT");
        	PRINTDATE_FORMAT = request.getParameter("PRINTDATE");
        	PRINTDATE_LABEL = request.getParameter("PRINTDATE_LABEL");
        	
        	printwithtmodel = (request.getParameter("printwithtmodel") != null) ? "1": "0";
        	PRINT_WITH_SIZE = (request.getParameter("PRINT_WITH_SIZE") != null) ? "1": "0";//PRINT WITH LOC
        	printwithtdate = (request.getParameter("printwithtdate") != null) ? "1": "0";//PRINT WITH BATCH
        	printwithduplicate = (request.getParameter("printwithduplicate") != null) ? "1": "0";
        	
			if(printwithtdate.equalsIgnoreCase("1")){
			if(!PRINTDATE_FORMAT.equalsIgnoreCase("")){
        	SimpleDateFormat formatter13 = new SimpleDateFormat(PRINTDATE_FORMAT);
    		Date dt = new Date();
    		printdate = formatter13.format(dt);
			}
			} else {
    		printdate = "";			
			}
        	
			returl ="../inhouse/genmultiproductbarcode";
		}else if(PAGE_TYPE.equalsIgnoreCase("MANUAL")) {
        	MANUAL_DESC = request.getParameter("ITEM_DESC");
        	MANUAL_BARCODE = request.getParameter("BARCODE");
        	MANUAL_QUANTITY = request.getParameter("QUANTITY");
			returl ="../inhouse/genmanualbarcode"; 
        }else if (PAGE_TYPE.equalsIgnoreCase("PRDBARCODE")) {
        	ITEM = request.getParameter("ITEM");
        	ITEM_DESC = request.getParameter("ITEM_DESC");
        	PRD_CLS_ID = request.getParameter("PRD_CLS_ID");
        	SELLING_PRICE = request.getParameter("SELLING_PRICE");
        	UNITPRICE = request.getParameter("UNITPRICE");
        	ITEM_QUANTITY = request.getParameter("QUANTITY");
        	MODEL = request.getParameter("MODEL");
        	printwithtmodel = (request.getParameter("printwithtmodel") != null) ? "1": "0";
        	printWithPLANT = request.getParameter("printWithPLANT");
        	printwithtdate = (request.getParameter("printwithtdate") != null) ? "1": "0";
        	PRINTDATE_FORMAT = request.getParameter("PRINTDATE");
        	PRINTDATE_LABEL = request.getParameter("PRINTDATE_LABEL");
			if(printwithtdate.equalsIgnoreCase("1")){
			if(!PRINTDATE_FORMAT.equalsIgnoreCase("")){
        	SimpleDateFormat formatter13 = new SimpleDateFormat(PRINTDATE_FORMAT);
    		Date dt = new Date();
    		printdate = formatter13.format(dt);
			}
			} else {
    		printdate = "";			
			}
			returl ="../inhouse/genproductbarcode";}
        %>
        <center>
	<h2>
		<small class="error-msg"><%=fieldDesc%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                  <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                   				
				</div>
				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='<%=returl%>'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
		<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="../inhouse/printbarcode">
<div class="form-group">
  		<div class="row"> 
	 	<div class="col-sm-3 mb-2"> 
	 	<label>Label Type</label>
	 	<input type="text" name="LabelType" id="LabelType" class="ac-selected form-control" placeholder="Label Type" value="<%=LabelType%>" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'LabelType\']').focus()" style="TOP: 30PX"><i class="glyphicon glyphicon-menu-down"></i></span>
	 	</div>
	 	<div class="col-sm-3 mb-2"> 
	 	<label>Barcode Width</label>
	 	<input type="text" name="BarcodeWidth" id="BarcodeWidth" class="form-control" placeholder="Barcode Width" value="<%=BarcodeWidth%>">
	 	</div>
	 	<div class="col-sm-3 mb-2"> 
	 	<label>Barcode Height</label>
	 	<input type="text" name="BarcodeHeight" id="BarcodeHeight" class="form-control" placeholder="Barcode Height" value="<%=BarcodeHeight%>" >
	 	</div>
	 	<div class="col-sm-3 mb-2"> 
	 	<label>Font Size</label>
	 	<input type="text" name="FontSize" id="FontSize" class="form-control" placeholder="Font Size" value="<%=FontSize%>" >
	 	</div>
	 	<div class="col-sm-3 mb-2"> 
	 	<label>Text Align</label>
	 	<input type="text" name="TextAlign" id="TextAlign" class="ac-selected form-control" placeholder="Text Align" value="<%=TextAlign%>" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'TextAlign\']').focus()" style="TOP: 30PX"><i class="glyphicon glyphicon-menu-down"></i></span>
	 	</div>
	 	<div class="col-sm-3 mb-2"> 
	 	<label>Text Position</label>
	 	<input type="text" name="TextPosition" id="TextPosition" class="ac-selected form-control" placeholder="Text Position" value="<%=TextPosition%>" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'TextAlign\']').focus()" style="TOP: 30PX"><i class="glyphicon glyphicon-menu-down"></i></span>
	 	</div>
	 	<div class="col-sm-3 mb-2"> 
	 	<label>Display Barcode Text</label>
	 	<input type="text" name="DisplayText" id="DisplayText" class="ac-selected form-control" placeholder="Display Barcode Text" value="<%=DisplayText%>" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'DisplayText\']').focus()" style="TOP: 30PX"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<input type="hidden" name="totrecqty" value="">
  		<input type="hidden" name="totcount" value="">
  		<input type="hidden" name="TRAVELER" value="<%=totalString%>">
  		<input type="hidden" name="printtype" value="<%=printtype%>">
  		<input type="hidden" name="PAGE_TYPE" value="<%=PAGE_TYPE%>">
  		<input type="hidden" name="printwithbatch" value="<%=printwithbatch%>">
  		<input type="hidden" name="printwithlot" value="<%=printwithlot%>">
  		<input type="hidden" name="printwithpobill" value="<%=printwithpobill%>">
  		<input type="hidden" name="printwithproduct" value="<%=printwithproduct%>">
  		<input type="hidden" name="CNAME" value="<%=CNAME%>">
  		<input type="hidden" name="CHPNO" value="<%=CHPNO%>">
  		<input type="hidden" name="CEMAIL" value="<%=CEMAIL%>">
  		<input type="hidden" name="printdate" value="<%=printdate%>">
  		<input type="hidden" name="MODEL" value="<%=MODEL%>">
  		<input type="hidden" name="PRINT_WITH_MODEL" value="<%=printwithtmodel%>">
  		<input type="hidden" name="PRINT_DATE" value="<%=PRINTDATE_FORMAT%>">
  		<input type="hidden" name=PRINT_DATE_LABEL value="<%=PRINTDATE_LABEL%>">
  		<input type="hidden" name="PRINT_WITH_PLANT" value="<%=printWithPLANT%>">
  		<input type="hidden" name="PRINT_WITH_DATE" value="<%=MODEL%>">
	 	</div>
	 	</div>
	 </div>
	 
	 <div class="form-group"> <!-- Author: Azees  Create date: August 30,2021  Description: Print Barcode PDF -->
  	<div class="col-sm-12" id="doubleup">  	
  	</div>
  	</div>
  	<div class="form-group"> <!-- Author: Azees  Create date: Sep 02,2021  Description: Print Barcode PDF -->
  	<div class="col-sm-12" id="singleup" style="display: none">
  	</div>
  	</div>
  	<div class="form-group">
  	<div class="col-sm-12" align="center">  	
  	<button type="button" class="Submit btn btn-default"  value="Print"  name="action" onclick="javascript:return printme();"><b>Generate Label</b></button>
  	<!-- <iframe id="Print_Barcode"  width="100%" height="100%" style="{display: (displayTable ? 'block' : 'none')}"></iframe> -->
  	 </div>
  	</div>
  	
</FORM>
</div>
		</div>
		</div>
<script language="javascript">
$('#doubleup').empty(); //Author: Azees  Create date: August 30,2021  Description: Print Barcode PDF
$('#singleup').empty(); //Author: Azees  Create date: Sep 02,2021  Description: Print Barcode PDF
var icount=0;
var totrecqty=0;
var strchkdDoNo = '<%=totalString%>';
var printwithlot = '<%=printwithlot%>';
var printwithproduct = '<%=printwithproduct%>';
var LabelTypeval = '<%=LabelType%>';
var PageType = '<%=PAGE_TYPE%>';


var doubleElement = document.getElementById('doubleup');
var singleElement =document.getElementById('singleup');
if(LabelTypeval=='Double') {
    doubleElement.style.display='inline-block';
    singleElement.style.display='none';
    }
    else {
    doubleElement.style.display='none';
    singleElement.style.display='inline-block';
    }

if(PageType=="MANUAL") {
	var MANUAL_DESC='<%=MANUAL_DESC%>';
	var MANUAL_BARCODE='<%=MANUAL_BARCODE%>';
	var MANUAL_QUANTITY='<%=MANUAL_QUANTITY%>';
	ViewManualBarcode(MANUAL_BARCODE,MANUAL_DESC,MANUAL_QUANTITY,totrecqty);

	icount=icount+1;
	totrecqty=parseInt(totrecqty)+parseInt(MANUAL_QUANTITY);
} else if(PageType=="PRDBARCODE") {
	var ITEM='<%=ITEM%>';
	var ITEM_DESC='<%=ITEM_DESC%>';
	var ITEM_QUANTITY='<%=ITEM_QUANTITY%>';
	var CURRENCY_SYL ='<%=SELLING_PRICE%>';
	var UNITPRICE='<%=UNITPRICE%>';
	var MODEL='<%=MODEL%>';
	var printwithtmodel='<%=printwithtmodel%>';
	var printWithPLANT='<%=printWithPLANT%>';
	if(printwithtmodel=="0")
		MODEL="";
	var printwithtdate='<%=printwithtdate%>';
	if(printwithtdate=="0")
		$("input[name=printdate]").val('');
	ViewPrtBarcode(ITEM,ITEM_DESC,CURRENCY_SYL,UNITPRICE,ITEM_QUANTITY,totrecqty,MODEL,printWithPLANT);

	icount=icount+1;
	totrecqty=parseInt(totrecqty)+parseInt(ITEM_QUANTITY);
	}else if(PageType=="MULTIPLEPRODUCT") {
		var strdata = strchkdDoNo.split("=");
		//console.log(str);
		for( var i = 0; i < strdata.length-1; i++ ) {
		var str=strdata[i];
		var chkdata = str.split(",");
		var ITEM=chkdata[0];
		var ITEM_DESC=chkdata[1];
		var MODEL=chkdata[2];
		var UNITPRICE=chkdata[3];
		var ITEM_QUANTITY=chkdata[4];
		var CURRENCY_SYL ='<%=SELLING_PRICE%>';
		var printwithtmodel='<%=printwithtmodel%>';
		var printWithPLANT='<%=printWithPLANT%>';
		if(printwithtmodel=="0")
			MODEL="";
		var printwithtdate='<%=printwithtdate%>';
		if(printwithtdate=="0")
			$("input[name=printdate]").val('');
			
		
		ViewPrtBarcode(ITEM,ITEM_DESC,CURRENCY_SYL,UNITPRICE,ITEM_QUANTITY,totrecqty,MODEL,printWithPLANT);

		icount=icount+1;
		totrecqty=parseInt(totrecqty)+parseInt(ITEM_QUANTITY);
		}
			
		}else {
var strdata = strchkdDoNo.split("=");
//console.log(str);
for( var i = 0; i < strdata.length-1; i++ ) {
	var str=strdata[i];

	if(PageType=="RECEIPT") {
	
	if(printwithproduct!="")
	{
		ViewProductBarcode(icount,str,totrecqty);
	} else{
		$.ajax({
			type: "GET",
			url: "../purchaseorder/Get-Barcode",
			dataType: "json",
			async: false,
			beforeSend: function(){
				//showLoader();
			}, 
			success: function(data) {
				ViewBarcode(icount,data.orderPrefix,data.generalDate,data.nextSeqno,str,totrecqty);
			},
			error: function(data) {
				alert('Unable to generate barcode. Please try again later.');
			},
			complete: function(){
				//hideLoader();
			}
		});
		}
	var chkdata = str.split(",");
	console.log(chkdata);
	var recqty = chkdata[5];
	icount=icount+1;
	if(printwithlot!="") {
		totrecqty=icount;
		} else {
			totrecqty=parseInt(totrecqty)+parseInt(recqty);
			}
	} else if(PageType=="LOCATION") {
		ViewLocationBarcode(str,totrecqty);

		var chkdata = str.split(",");
		console.log(chkdata);
		var recqty = chkdata[2];
		icount=icount+1;
		totrecqty=parseInt(totrecqty)+parseInt(recqty);
		}
}

}

$("input[name=totrecqty]").val(totrecqty);
$("input[name=totcount]").val(icount);
</script>		
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>