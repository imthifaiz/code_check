<%@page import="com.track.dao.ProductionBomDAO"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.*"%>
<%
//String title = "Semi Processed Product Detail";
String title = "Kitting Detail";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
    <jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>

<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'SemiFinished', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "",action     =   "",pitem="",pitemdesc="",pbatch="",LOC_ID="",pqty="1",DESC="",GRNO="",PCOST="0",HDR_ID="0",
LOC_DESC="",LOC_TYPE_ID="",LOC_TYPE_DESC="",LOC_TYPE_ID2="",fieldDesc="",allChecked="",PUOM="",SerParent="checked",ORDDATE="",RFLAG="2",errcls="error-msg",
RECLOC_ID="PROCESSING";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();
String plant = (String)session.getAttribute("PLANT");

String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
PCOST = StrUtils.addZeroes(Double.parseDouble(PCOST), numberOfDecimal);

ArrayList movQryList  = new ArrayList();
Hashtable ht = new Hashtable();
GRNO = strUtils.fString(request.getParameter("GRNO"));
if(!GRNO.equalsIgnoreCase("")) {
SemiFinishedProductDAO processingReceiveDAO = new SemiFinishedProductDAO();
try {
	String sql = "select ID,GRNO,PARENT_PRODUCT,(select ITEMDESC from [" + plant +"_ITEMMST] where ITEM = PARENT_PRODUCT)ITEMDESC,PARENT_PRODUCT_LOC,PARENT_PRODUCT_BATCH,PARENT_PRODUCT_QTY,PARENT_COST,ORDDATE,PARENTUOM from " + plant +"_SEMI_PROCESSHDR A WHERE A.PLANT='"+ plant+"' AND A.GRNO='"+GRNO+"'";
	movQryList = processingReceiveDAO.selectForReport(sql,ht,"");
	if (movQryList.size() > 0) {
		for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
            Map lineArr = (Map) movQryList.get(iCnt);
            
            PCOST = (String)lineArr.get("PARENT_COST");
            double balCostVal ="".equals(PCOST) ? 0.0f :  Double.parseDouble(PCOST);
            if(balCostVal==0f){
            	PCOST="0.00000";
            }else{
            	PCOST=PCOST.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
            }
            PCOST = StrUtils.addZeroes(Double.parseDouble(PCOST), numberOfDecimal);
            pitem = StrUtils.fString((String)lineArr.get("PARENT_PRODUCT"));
            pitemdesc = StrUtils.fString((String)lineArr.get("ITEMDESC"));
            RECLOC_ID = StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_LOC"));
            pbatch = StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_BATCH"));
            pqty = StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_QTY"));
            PUOM = StrUtils.fString((String)lineArr.get("PARENTUOM"));
            HDR_ID = StrUtils.fString((String)lineArr.get("ID"));
            
	}
	}
} catch (Exception e) {
	e.printStackTrace();
	response.setStatus(500);
}
}
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../SemiFinished/summary"><span class="underline-on-hover">Kitting Summary</span></a></li>	
                <li><label>Kitting Detail</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../SemiFinished/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

 <div class="container-fluid">

<form class="form-horizontal" name="form" method="post" action="">
<INPUT type="hidden" name="RFLAG" value="<%=RFLAG%>">
<INPUT type="hidden" name="plant" value=<%=plant%>>
<input type="hidden" name="LOC_DESC" id="LOC_DESC" value="<%=LOC_DESC%>">
<input type="hidden" name="LOC_TYPE_DESC" id="LOC_TYPE_DESC" value="<%=LOC_TYPE_DESC%>">
<input type="hidden" name="ORDDATE" id="ORDDATE" value="<%=ORDDATE%>">
<input type="hidden" name="DESC" id="DESC">
<input type="hidden" name="DETDESC" id="DETDESC">
<input type="hidden" name="CDETDESC" id="CDETDESC">
<input type="hidden" name="CDESC" id="CDESC">
<input type="hidden" name="LOC_TYPE_ID" id="LOC_TYPE_ID">
<input type="hidden" name="LOC_TYPE_ID2" id="LOC_TYPE_ID2">
<input type="hidden" name="CDESC" id="CDESC">
<input type="hidden" name="ISAUTOGENERATE" value="false">
<input type="hidden" name="PCOST">
<input type="number" id="numberOfDecimal" name="numberOfDecimal" style="display: none;" value=<%=numberOfDecimal%>>
 <div id = "ERROR_MSG"></div>
   <div id = "COMPLETED_MSG"></div>
                 
<div class="form-group">
     <label class="control-label col-form-label col-sm-2 required">Kitting Product</label>
    <div class="col-sm-4 ac-box">
				<div class="input-group">   
    		<input type="TEXT"  name="ITEM" id="ITEM" value="<%=pitem%>" readonly size="20" MAXLENGTH=100  class="ac-selected  form-control typeahead">
  		</div>
  		</div>

  		</div>
  		
  		  		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Product Description:</label>
					<div class="col-sm-4">
						<input type="text" readonly class="form-control" id="ITEM_DESC" name="ITEM_DESC" value="<%=pitemdesc%>" >
					</div>
				</div>
  		
  		<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Kitting Location:</label>
					<div class="col-sm-4">
					
						<input type="text" readonly class="form-control" id="LOC_ID" name="LOC_ID" value="<%=RECLOC_ID%>" >
					</div>
				</div>               
<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Batch:</label>
					<div class="col-sm-4">
					<div class="input-group">
						<input class="ac-selected  form-control typeahead" name="BATCH_0" id="BATCH_0" type="TEXT" value="<%=pbatch%>" readonly size="20" MAXLENGTH=40>   		 	
					</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Cost:</label>
					<div class="col-sm-4">
					<div class="input-group">
						<input class="form-control" name="COST" id="COST" readonly type="TEXT" value="<%=PCOST%>" size="20" MAXLENGTH=40>
					</div>
					</div>
				</div>

<div class="form-group">
      <label class="control-label col-form-label col-sm-2 required">Kitting Quantity</label>
     <div class="col-sm-2">
				<div class="input-group">     
          <INPUT  class="form-control" name="PARENTQTY" type="TEXT" id="PARENTQTY" value="<%=pqty%>" size="4" MAXLENGTH=50 readonly>
			</div>
            </div>
            <div class="col-sm-2">
    	<input type="text" name="PARENTUOM" id="PARENTUOM" class="form-control" placeholder="Parent UOM" readonly value="<%=PUOM%>">
	 	</div>
            
            </div>
   		
  		         
  
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  
<input type="hidden" name="PTYPE" id="PTYPE" value="CREATEKITBOM">
</form>
</div>
  </div>
  </div>
       

<script> 

function onGo() {

	var hdrid=<%=HDR_ID%>;
	
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    var urlStr = "/track/SemiFinished/VIEW_PROCESSED_DETAIL";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {HDR_ID:hdrid,PLANT:"<%=plant%>"},dataType: "json", success: callback });

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
        $.each(data.kittingbom, function(i,item){
                   
        	outPutdata = outPutdata+item.KITBOMDATA;
                    	ii++;
            
          });
        
	}
    outPutdata = outPutdata +'</TABLE>';
                                                  
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
    document.getElementById('spinnerImg').innerHTML =''; 
     var errorMsg = data.errorMsg;
     if(typeof(errorMsg) == "undefined"){
    	 errorMsg = "";
     }
     errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
     document.getElementById('ERROR_MSG').innerHTML = errorHTML;
     var comMsg = data.completedMes;
     if(typeof(comMsg) == "undefined"){
    	 comMsg = "";
     }
     mesHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+comMsg+"</td></tr></table>";
     document.getElementById('COMPLETED_MSG').innerHTML = mesHTML;
    
      	     
}

function getTable(){
        return '<TABLE class="table table-bordred table-striped" id="tabledata" WIDTH="100%" align = "center">'+
               '<thead>'+
        	   '<tr >'+
        		'<th width="2%">No</th>'+
        		'<th width="5%">GINO</th>'+
        		'<th width="10%">Child Product</th>'+
        		'<th width="11%">Child Product Desc</th>'+
        		'<th width="10%">Child UOM</th>'+
        		'<th width="11%">Child Product Location</th>'+
        		'<th width="7%">Batch</th>'+
        		'<th width="4%">Tran.Qty</th>'+
        		'<th width="10%">Tran.Cost</th>'+
        		'</tr>'+
         		'</thead>';
       
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

</script>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
    onGo();  
});
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>