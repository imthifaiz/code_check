<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="db" class="com.track.gates.defaultsBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
       
     
       POUtil itemUtil = new POUtil();
       session = request.getSession();
       String plant = session.getAttribute("PLANT").toString();
       PlantMstDAO plantMstDAO = new PlantMstDAO();
       String SETCURRENTDATE_GOODSRECEIPT =  plantMstDAO.getSETCURRENTDATE_GOODSRECEIPT(plant);//Thanzith
       ArrayList plntList = plantMstDAO.getPlantMstDetails(plant);
       Map plntMap = (Map) plntList.get(0);
       String PLNTDESC = (String) plntMap.get("PLNTDESC");
       String action   = StrUtils.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String userId = (String) session.getAttribute("LOGIN_USER");
       
       com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
       String grno = _TblControlDAO.getNextOrder(plant,userId,"GRN");
      
      
       String  fieldDesc="";
       String   ORDERNO    = "",ORDERLNO="",CUSTNAME = "", ITEMNO   = "", ITEMDESC  = "",
       LOC   = "" ,SRANGE="",ERANGE="",SERIALQTY="", BATCH  = "", REF   = "",SUFFIX="",DTFRMT="",
       ORDERQTY = "", RECEIVEDQTY="",RECEIVEQTY="",EXPIREDATE="", 
	   BALANCEQTY="",CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="",TRANSACTIONDATE="",UOMQTY="",UOM="",UNITCOST="0";
       
       ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=StrUtils.fString(request.getParameter("ORDERLNO"));
       CUSTNAME = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUSTNAME")));
       ITEMNO = StrUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("ITEMDESC")));
       LOC = StrUtils.fString(request.getParameter("LOC"));
       SERIALQTY =  StrUtils.fString(request.getParameter("SERIALQTY"));
       SRANGE=  StrUtils.fString(request.getParameter("SRANGE"));
        ERANGE=  StrUtils.fString(request.getParameter("ERANGE"));
       
       BATCH = StrUtils.fString(request.getParameter("BATCH"));
       EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
       SUFFIX=  StrUtils.fString(request.getParameter("SUFFIX"));
       DTFRMT = StrUtils.fString(request.getParameter("DTFRMT")); 
	  TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
       String curDate =DateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;

       //Get podet
       PoDetDAO  _PoDetDAO  = new PoDetDAO();  
       _PoDetDAO.setmLogger(mLogger);
       List listQry =  _PoDetDAO.getInboundItemListByWMS(plant,ORDERNO,ITEMNO,ORDERLNO);
       for(int i =0; i<listQry.size(); i++) {
          Map m=(Map)listQry.get(i);
          ORDERQTY =StrUtils.formatNum((String)m.get("qtyor"));
          RECEIVEDQTY =StrUtils.formatNum((String)m.get("qtyrc"));
          BALANCEQTY=(String)m.get("balanceqty");
          REF= (String)m.get("ref");
          UOMQTY =StrUtils.formatNum((String)m.get("UOMQTY"));
          UOM= (String)m.get("uom");
          UNITCOST= (String)m.get("UNITCOST");
       }
        //Get end
       
       //Get Supplier Details
         Hashtable ht=new Hashtable();
         String extCond="";
         ht.put("PLANT",plant);
         ht.put("PONO",ORDERNO);
    
  
        if(ORDERNO.length()>0) extCond="a.plant='"+plant+"' and a.pono = '"+ORDERNO+"' and a.custname=b.vname";
         extCond=extCond+" order by a.pono desc";
         itemUtil.setmLogger(mLogger);
         ArrayList listQry1 = itemUtil.getPoHdrDetailsReceiving("isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3",ht,extCond);
         for(int i =0; i<listQry1.size(); i++) {
            Map m1=(Map)listQry1.get(i);
            CONTACTNAME  =  (String)m1.get("contactname");
            TELNO  =  (String)m1.get("telno");
            EMAIL  =  (String)m1.get("email");
            ADD1  =  (String)m1.get("add1");
            ADD2  =  (String)m1.get("add2");
            ADD3 =  (String)m1.get("add3");
       }
     
   
      if(action.equalsIgnoreCase("result"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("RESULT");
       fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
      }
      else if(action.equalsIgnoreCase("resulterror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
       else if(action.equalsIgnoreCase("qtyerror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("QTYERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
     
      else if(action.equalsIgnoreCase("batcherror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("BATCHERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
      else if(action.equalsIgnoreCase("catcherro"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("CATCHBATCHERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
       


%>
<!-- <html> -->

<%--New page design begin --%>
<%
String title = "Purchase Order Receipt (By Range)";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PURCHASE_TRANSACTION%>"/>
</jsp:include>
<%--New page design end --%>

<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<!-- <script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script> -->
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<!-- <title>Inbound Order Receipt&nbsp; (By Range)</title>
<link rel="stylesheet" href="css/style.css"> -->
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
    subWin = window.open(URL, 'InboundOrderList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew()
{
	
	var urlStr = "/track/InboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		ACTION : "GRN"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.grno;
				document.form.GRNO.value= resultV;
	
			} else {
				alert("Unable to genarate GRN NO");
				document.form.GRNO.value = "";
			}
		}
	});	
	}
function validatePO(form) {
   var frmRoot=document.form;
   var table = document.getElementById("MULTIPLE_RECEIVING");
   var rowCount = table.rows.length;
   var locId = document.form.LOC.value;
   var totalQtyReceive=0;
   if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 ) {
		alert("Please Enter ORDERNO!");
		frmRoot.ORDERNO.focus();
		return false;
   }
   
    
   if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 ) {
		alert("Please Enter Product ID!");
		frmRoot.ITEMNO.focus();
		return false;
   }
   
   if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC").focus();
                return false;
               }
               
    var orderQty = <%=StrUtils.removeFormat(ORDERQTY)%>;
    var recvedQty = <%=StrUtils.removeFormat(RECEIVEDQTY)%>;
    var balanceQty =parseFloat(orderQty)-parseFloat(recvedQty);
       
		var qty = document.form.SERIALQTY.value;
              
                var sRange = document.form.SRANGE.value;
                var eRange = document.form.ERANGE.value;
                var rangeSize = parseInt(eRange);
                var locId = document.form.LOC.value;
                  
                var receivingQty = parseFloat(qty)*parseFloat(rangeSize);
		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.form.SERIALQTY.focus();
                        return false;
		} 
                
                if (isNumericInput(qty) == false) {
                    alert("Entered Quantity is not a valid Qty!");
                    document.form.SERIALQTY.value = "";
                    document.form.SERIALQTY.focus();
                    return false;
		} 
                if (sRange == "" || sRange.length == 0) {
			alert("Enter Start Range!");
			document.form.SRANGE.focus();
                        return false;
		} 
                
                 if (eRange == "" || eRange.length == 0) {
                    alert("Enter No. of Batch!");
                    document.form.ERANGE.focus();
                    return false;
		} 
                 if (isNumericInput(sRange) == false) {
                            alert("Entered Start Range is not a valid Number !");
                            document.form.SRANGE.value = "";
                            document.form.SRANGE.focus();
                            return false;
		}                
                if (isNumericInput(eRange) == false) {
                            alert("Entered No. of Batch is not a valid Number !");
                            document.form.ERANGE.value = "";
                            document.form.ERANGE.focus();
                            return false;
		} 
        
          /*       if(parseInt(sRange)>parseInt(eRange)){
                            alert("Entered invalid Range,Start Range is Greater than End Range !");
                             document.form.SRANGE.value = "";
                             document.form.SRANGE.focus();
                            return false;
		}  */
                
               
                if (balanceQty < receivingQty) {
                
                             alert(" Entered Range is greater than the Balance Qty to Receive!");
                             document.form.ERANGE.value = "";
                             document.form.ERANGE.focus();
                             return false;
		}

		   
		   if(frmRoot.TRANSACTIONDATE.value=="" || frmRoot.TRANSACTIONDATE.value.length==0 ) {
			   alert("Please select Transaction Date");	
				frmRoot.TRANSACTIONDATE.focus();
				return false;
		   }
		

   if(rangeSize>0){
	   var grnval = document.form.GRNO.value.length;
		 if(grnval>0)
		 {
	   document.form.RECEIVEQTY.value = receivingQty;   
	   document.form.action ="/track/OrderReceivingByPOServlet?action=ReceiveByRange";
	   document.form.submit();
	   return true;
		 }
		 else
			 {
			 /* if (confirm('Are you sure to submit this transaction without GRNO?')) {
				 document.form.RECEIVEQTY.value = receivingQty;   
				   document.form.action ="/track/OrderReceivingByPOServlet?action=ReceiveByRange";
				   document.form.submit();
				   return true;
			 } else { */
				 alert ("Please Enter GRNO");
				 document.getElementById("GRNO").focus();
			 //}		 
			 }
   }
 
}

function validatePOAndSendEmail(form){
	var frmRoot=document.form;
	   var table = document.getElementById("MULTIPLE_RECEIVING");
	   var rowCount = table.rows.length;
	   var locId = document.form.LOC.value;
	   var totalQtyReceive=0;
	   if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 ) {
			alert("Please Enter ORDERNO!");
			frmRoot.ORDERNO.focus();
			return false;
	   }
	   
	    
	   if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 ) {
			alert("Please Enter Product ID!");
			frmRoot.ITEMNO.focus();
			return false;
	   }
	   
	   if(locId=="" || locId.length==0 ) {
			alert("Enter Location!");
			document.getElementById("LOC").focus();
	                return false;
	               }
	               
	    var orderQty = <%=StrUtils.removeFormat(ORDERQTY)%>;
	    var recvedQty = <%=StrUtils.removeFormat(RECEIVEDQTY)%>;
	    var balanceQty =parseFloat(orderQty)-parseFloat(recvedQty);
	       
			var qty = document.form.SERIALQTY.value;
	              
	                var sRange = document.form.SRANGE.value;
	                var eRange = document.form.ERANGE.value;
	                var rangeSize = parseInt(eRange);
	                var locId = document.form.LOC.value;
	                  
	                var receivingQty = parseFloat(qty)*parseFloat(rangeSize);
			if (qty == "" || qty.length == 0) {
				alert("Enter Quantity!");
				document.form.SERIALQTY.focus();
	                        return false;
			} 
	                
	                if (isNumericInput(qty) == false) {
	                    alert("Entered Quantity is not a valid Qty!");
	                    document.form.SERIALQTY.value = "";
	                    document.form.SERIALQTY.focus();
	                    return false;
			} 
	                if (sRange == "" || sRange.length == 0) {
				alert("Enter Start Range!");
				document.form.SRANGE.focus();
	                        return false;
			} 
	                
	                 if (eRange == "" || eRange.length == 0) {
	                    alert("Enter No. of Batch!");
	                    document.form.ERANGE.focus();
	                    return false;
			} 
	                 if (isNumericInput(sRange) == false) {
	                            alert("Entered Start Range is not a valid Number !");
	                            document.form.SRANGE.value = "";
	                            document.form.SRANGE.focus();
	                            return false;
			}                
	                if (isNumericInput(eRange) == false) {
	                            alert("Entered No. of Batch is not a valid Number !");
	                            document.form.ERANGE.value = "";
	                            document.form.ERANGE.focus();
	                            return false;
			} 
	        
	          /*       if(parseInt(sRange)>parseInt(eRange)){
	                            alert("Entered invalid Range,Start Range is Greater than End Range !");
	                             document.form.SRANGE.value = "";
	                             document.form.SRANGE.focus();
	                            return false;
			}  */
	                
	               
	                if (balanceQty < receivingQty) {
	                
	                             alert(" Entered Range is greater than the Balance Qty to Receive!");
	                             document.form.ERANGE.value = "";
	                             document.form.ERANGE.focus();
	                             return false;
			}

			  if(frmRoot.TRANSACTIONDATE.value=="" || frmRoot.TRANSACTIONDATE.value.length==0 ) {
				   alert("Please select Transaction Date");	
					frmRoot.TRANSACTIONDATE.focus();
					return false;
			   }

	   if(rangeSize>0){
		   var grnval = document.form.GRNO.value.length;
			 if(grnval>0)
			 {
				   document.form.RECEIVEQTY.value = receivingQty;   
				   urlStr = "/track/OrderReceivingByPOServlet?action=ReceiveByRange";
					var formData = $('#frmInboundOrderReceiptByRange').serialize();
					$.ajax( {
						type : "POST",
						url : urlStr,
						data : formData,
						beforeSend: function(){
							showLoader();
						}, 
						complete: function(){
							hideLoader();
						},
						dataType : "json",
						success : function(data) {
							if (data.ERROR_CODE == "100") {
								$('.success-msg').html(data.MESSAGE).css('display', 'inline');
								$('#common_email_modal').modal('toggle');
								$('#send_to').val($('#EMAIL').val()).multiEmail();
								$('#send_subject').val($('#template_subject').val()
														.replace(/\{COMPANY_NAME\}/, $('#plant_desc').val())
														.replace(/\{ORDER_NO\}/, $('#ORDERNO').val())
														.replace(/\{ORDER_NO_2\}/, $('#GRNO').val())
														);
								$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(
											$('#template_body').val()
											.replace(/\{ORDER_NO\}/, $('#ORDERNO').val())
											.replace(/\{SUPPLIER_NAME\}/, $('#CUSTNAME').val())
											);
								$('#send_attachment').val('Goods Receipt');
							} else {
								$('.success-msg').html(data.MESSAGE).addClass('error-msg').removeClass('success-msg').css('display', 'inline');
							}
						}
					});	
			 }
			 else{
				 /* if (confirm('Are you sure to submit this transaction without GRNO?')) {
					 document.form.RECEIVEQTY.value = receivingQty;   
					   document.form.action ="/track/OrderReceivingByPOServlet?action=ReceiveByRange";
					   document.form.submit();
					   return true;
				 } else { */
					 alert ("Please Enter GRNO");
					 document.getElementById("GRNO").focus();
				 //}		 
			}
	   }	
}
function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
	}
function generatePdfMail(dataUrl,attachName){	
	formData = new FormData();
	formData.append("GRNO_PONO", $("#ORDERNO").val());
	progressBar();
	sendMailTemplate(formData);
	  //return formData;
	  
}
function toDataURL(src, callback, outputFormat) {
	  var img = new Image();
	  img.crossOrigin = 'Anonymous';
	  img.onload = function() {
	    var canvas = document.createElement('CANVAS');
	    var ctx = canvas.getContext('2d');
	    var dataURL;
	    canvas.height = this.naturalHeight;
	    canvas.width = this.naturalWidth;
	    ctx.drawImage(this, 0, 0);
	    dataURL = canvas.toDataURL(outputFormat);
	    callback(dataURL);
	  };
	  img.src = src;
	  if (img.complete || img.complete === undefined) {
	    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
	    img.src = src;
	  }
}
</script>
<%--New page design begin --%>
<center>
	<h2><small class="success-msg"> <%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../purchasetransaction/receiptsummarybyserial'">
			  <i class="glyphicon glyphicon-remove"></i></h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

<form class="form-horizontal" name="form" id="frmInboundOrderReceiptByRange" method="post" action="/track/OrderReceivingByPOServlet?">
   
       <div class="form-group">
       <label class="control-label col-sm-2" for="inbound Order">Order Number:</label>
       <div class="col-sm-3">
       <INPUT name="ORDERNO" id="ORDERNO" type="TEXT" value="<%=ORDERNO%>" size="30" MAXLENGTH=20 class="form-control" readonly="readonly">
   	   </div> 
   	   <div class="form-inline">
        <label class="control-label col-sm-2" for="Order Qty">Order Quantity:</label>
        <div class="col-sm-3">
        <INPUT name="ORDERQTY" class="form-control"	type="TEXT" value="<%=ORDERQTY%>" style="width: 100%" MAXLENGTH=80 readonly>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Email Address">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT name="EMAIL" id="EMAIL" value="<%=EMAIL%>" class="form-control" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		</div> 
 		</div>
 		
 	<div class="form-group">
  	   <label class="control-label col-sm-2" for="supplier name">Supplier Name:</label>
       <div class="col-sm-3">
       <INPUT name="CUSTNAME" id="CUSTNAME" class="form-control" type="TEXT" value="<%=StrUtils.forHTMLTag(CUSTNAME)%>" size="30" MAXLENGTH=80 readonly>
    	</div>
    	<div class="form-inline">
        <label class="control-label col-sm-2" for="Received Qty">Received Quantity:</label>
        <div class="col-sm-3">
        <INPUT name="RECEIVEDQTY" class="form-control" type="TEXT" value="<%=RECEIVEDQTY%>" style="width: 100%" MAXLENGTH=80 readonly>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Unit Number">Unit No:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD1" class="form-control"	value="<%=ADD1%>" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		</div>
 		</div>
 		
 		<div class="form-group">
    	<label class="control-label col-sm-2" for="Contact Name">Contact Name:</label>
        <div class="col-sm-3">
        <INPUT name="CONTACTNAME" class="form-control" value="<%=CONTACTNAME%>" type="TEXT" size="30" MAXLENGTH=80 readonly>
        </div>
        <div class="form-inline">
        <label class="control-label col-sm-2" for="Receiving Qty">Receiving Quantity:</label>
        <div class="col-sm-3">
        <INPUT name="RECEIVEQTY" value="<%= RECEIVEQTY%>" type="TEXT" style="width: 100%" MAXLENGTH=80 readOnly  class="form-control">
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Buiding Name">Building:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD2" value="<%=ADD2%>" class="form-control" type="hidden" value="" size="30" MAXLENGTH=80	readonly>
        <!-- </div>
 		</div> -->
 		</div>
 		</div>
 		
        <div class="form-group">
        <label class="control-label col-sm-2" for="Product Id">Product ID:</label>
        <div class="col-sm-3">
        <INPUT name="ITEMNO" type="TEXT" value="<%=ITEMNO%>" size="30" MAXLENGTH=80 class="form-control" readonly="readonly">
       </div>
       <div class="form-inline">
        <label class="control-label col-sm-2" for="GRNO">GRNO:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <input name="GRNO" id="GRNO" value="<%= grno %>" class="form-control" type="TEXT" value="" style="width: 100%" MAXLENGTH=80 />
        <span class="input-group-addon"  onClick="onNew()">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
   		 	</div>
        </div>
        </div>
 		</div>
 		 
        <div class="form-group">
        <label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-3">
        <INPUT name="ITEMDESC" class="form-control" type="TEXT" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="30" MAXLENGTH=80 readonly>
        </div>
        <div class="form-inline">
        <label class="control-label col-sm-2" for="Remarks">Remarks:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="REF" type="TEXT" value="<%=REF%>"	style="width: 100%" MAXLENGTH=100>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="street">Street:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD3" value="<%=ADD3%>" class="form-control" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		</div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Telephone">Telephone:</label>
        <div class="col-sm-3"> -->
        <INPUT name="TELNO" class="form-control"	value="<%=TELNO%>" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		</div>
 <div class="form-group">
        <label class="control-label col-sm-2" for="Description">UOM:</label>
        <div class="col-sm-3">
        <INPUT name="UOM" class="form-control" type="TEXT" value="<%=StrUtils.forHTMLTag(UOM)%>" size="30" MAXLENGTH=80 readonly>
        </div>
        <INPUT name="UOMQTY" class="form-control"	value="<%=UOMQTY%>" type="hidden" >
        <INPUT name="UNITCOST" class="form-control"	value="<%=UNITCOST%>" type="hidden" >
        </div>
 		
 		<div class="form-group">
       <label class="control-label col-sm-1" for="Location" >Location:</label>
       <div class="col-sm-2">
       <div class="input-group">
       <INPUT class="form-control" name="LOC" id="LOC" type="TEXT" value="<%=LOC%>" size="15"
    	onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value);}"	MAXLENGTH="20"> 
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/loc_list_receivewms.jsp?LOC='+form.LOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-1 pull-left" for="Suffix" >Suffix:</label>
  		<div class="col-sm-1">   
  		<INPUT class="form-control" name="SUFFIX" id="SUFFIX" value="<%=SUFFIX%>" type="TEXT" size="6" MAXLENGTH="7">
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-1" for="Dt Frmt" >Dt Frmt:</label>
        <div class="col-sm-1">
      	<INPUT class="form-control" name="DTFRMT" id="DTFRMT" value="<%=DTFRMT%>" type="TEXT" size="7" MAXLENGTH="7">
    	</div>
 		</div> 		
 		<div class="form-inline">
       <label class="control-label col-sm-1" for="Range Start" >Range-Start:</label>
       <div class="col-sm-1">
       <INPUT class="form-control" name="SRANGE" id="SRANGE" type="TEXT" value="<%=SRANGE%>" size="7" MAXLENGTH="10"> 
   		</div>
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-1" for="No of Batch" >No of Batch:</label>
  		<div class="col-sm-1">   
  		<INPUT class="form-control" name="ERANGE" id="ERANGE" type="TEXT" value="<%=ERANGE%>" size="10" MAXLENGTH="10">
  		</div>
  		</div>  		
 		</div>
 		
 		
 		
 		<div class="form-group">
  		<label class="control-label col-sm-1" for="Receiving Qty" style="align:left;">Receiving Qty:</label>
        <div class="col-sm-1">
      	<INPUT class="form-control" name="SERIALQTY" id="SERIALQTY"	value="1" type="TEXT" size="10" MAXLENGTH="6">
    	</div>
 		  
 		<div class="form-inline">
 		<label class="control-label col-sm-2" for="Transaction Date">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Transaction Date:</label>
<!--         <label class="control-label col-sm-2 required" for="Transaction Date" style="align:left;">Transaction Date:</label> -->
        <div class="col-sm-2">
        <div class="input-group">   
        <%if(SETCURRENTDATE_GOODSRECEIPT.equals("1")){%>       
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE" readonly="readonly" value="<%=TRANSACTIONDATE%>" type="TEXT" size="20" MAXLENGTH="80"> 
        <%}else {%>
       <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE" readonly="readonly" value="" type="TEXT" size="20" MAXLENGTH="80">
        <%}%>
        </div>
    	</div>
    	</div>
    	<div class="form-inline">
    	<label class="control-label col-sm-2" for="Expiry Date" style="align:left;">Expiry Date:</label>
        <div class="col-sm-2">
        <div class="input-group">   
        <INPUT class="form-control datepicker" name="EXPIREDATE" id="EXPIREDATE" readonly="readonly" value="<%=EXPIREDATE%>" type="TEXT" size="20" MAXLENGTH="80">
        </div>
 		</div>
 		</div>
 		<div class="form-inline">
 		<div class="col-sm-1">
 		<button type="button" class="Submit btn btn-default" onClick="validateInputs()" ><b>Receive</b></button>
 		</div>
 		</div>
		</div>
		
		<table class="table" border ="0" cellspacing="0"  align = "center"  id="MULTIPLE_RECEIVING">
		<thead style="background: #eaeafa; font-size: 15px">
                    <TR>
                    <TH>Serial Batch Number</TH>
                    <TH>Quantity  </TH>
                    </TR>
		</thead>
		</TABLE>
     	
    	<div class="form-group">        
      	<div class="col-sm-12" align="left">
      	<div class="dropup">
		<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
	    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Receive
	    <span class="caret"></span></button>
	    <ul class="dropdown-menu">
	      <li><a id="btnRedeive" href="#" onclick="validatePO(document.form);">Receive</a></li>
	      <li><a id="btnReceiveEmail" href="#" onclick="validatePOAndSendEmail(document.form);">Receive and Send Email</a></li>
	    </ul>
<%-- 	    <button type="button" class="Submit btn btn-default" onClick="window.location.href='IBOrderSummaryForRangeReceipt.jsp?PONO=<%=ORDERNO%>&action=View'"><b>Back</b></button>&nbsp;&nbsp; --%>
	  	</div>
      	</div>
        </div>
      	
      	<INPUT name="ORDERLNO" type="hidden" value="<%=ORDERLNO%>" size="1"
			MAXLENGTH=80>
	
		<INPUT name="BALANCEQTY" type="hidden" value="<%=BALANCEQTY%>"
			size="1" MAXLENGTH=80>
		
		<INPUT name="LOGIN_USER" type="hidden" value="<%=sUserId%>" size="1"
			MAXLENGTH=80>
  		</form>
		</div>
		</div>
		</div>


<script>

function addRow() {

	var table = document.getElementById("MULTIPLE_RECEIVING");
        var stratRangeValue = document.getElementById("SRANGE").value;
        var endRangeValue = document.getElementById("ERANGE").value;
        var sfix = document.getElementById("SUFFIX").value;
        var dtFmt = document.getElementById("DTFRMT").value;
        var rangeCnt = parseInt(endRangeValue);
        var rowCount = table.rows.length;	
              
        for(var i = rowCount; i > 1;i--)
        {
          table.deleteRow(i -1);
        }
        rowCount = 1

        for(var index = 1; index<=rangeCnt; index++) {
        var rowColor = ((index == 0) || (index % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF"; 
    
        var row = table.insertRow(index);
        row.bgColor = rowColor;
        var cell2 = row.insertCell(0);
        cell2.align="left";
        var batch = parseInt(stratRangeValue)+index -1;
        cell2.innerHTML =sfix+dtFmt+batch;

        var cell3 = row.insertCell(1);
        cell3.innerHTML = document.getElementById("SERIALQTY").value;
        cell3.align="left";
        }
             

	}


    function deleteRow(tableID) {
            try {
                    var table = document.getElementById(tableID);
                    var rowCount = table.rows.length;
                    rowCount = rowCount * 1 - 1;
                    if (rowCount == 0) {
                            alert("Can not remove the default Receiving");
                    } else {
                            table.deleteRow(rowCount);
                    }
            } catch (e) {
                    alert(e);
            }
    }

            
            
            function validateLocation(locId) {
            if(locId=="" || locId.length==0 ) {
                    alert("Enter Location!");
                    document.getElementById("LOC").focus();
            }else{
                    var urlStr = "/track/InboundOrderHandlerServlet";
                    $.ajax( {
                            type : "POST",
                            url : urlStr,
                            data : {
                                    LOC : locId,
                    USERID : "<%=sUserId%>",
                                    PLANT : "<%=plant%>",
                                    ACTION : "VALIDATE_LOCATION"
                                    },
                                    dataType : "json",
                                    success : function(data) {
                                            if (data.status != "100") {
                                            
                                                    alert("Not a valid Location");
                                                    var location = document.getElementById("LOC");
                                                    location.value="";
                                                    location.focus();
                                                    return false;
                                            }
                                    }
                            });
                    }
            }

  function validateInputs() {
   var orderQty = <%=StrUtils.removeFormat(ORDERQTY)%>;
    var recvedQty = <%=StrUtils.removeFormat(RECEIVEDQTY)%>;
    var balanceQty =parseFloat(orderQty)-parseFloat(recvedQty);
              
		var qty = document.form.SERIALQTY.value;
              
                var sRange = document.form.SRANGE.value;
                var eRange = document.form.ERANGE.value;
                var rangeSize = eRange-sRange;
                rangeSize= rangeSize+1;
                var locId = document.form.LOC.value;
                  
                  var receivingQty = parseFloat(qty)*parseFloat(rangeSize);
		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.form.SERIALQTY.focus();
                        return false;
		} 
                
                if (isNumericInput(qty) == false) {
                            alert("Entered Quantity is not a valid Qty!");
                            document.form.SERIALQTY.value = "";
                            document.form.SERIALQTY.focus();
                            return false;
		} 
                if (sRange == "" || sRange.length == 0) {
			alert("Enter Start Range!");
			document.form.SRANGE.focus();
                        return false;
		} 
                
                 if (eRange == "" || eRange.length == 0) {
			alert("Enter No. of Batch!");
			document.form.ERANGE.focus();
                        return false;
		} 
                 if (isNumericInput(sRange) == false) {
                            alert("Entered Start Range is not a valid Number !");
                            document.form.SRANGE.value = "";
                            document.form.SRANGE.focus();
                            return false;
		} 
                
                if (isNumericInput(eRange) == false) {
                            alert("Entered No of Batch is not a valid Number !");
                            document.form.ERANGE.value = "";
                            document.form.ERANGE.focus();
                            return false;
		} 
        
             /*    if(parseInt(sRange)>parseInt(eRange)){
                            alert("Entered invalid Range,Start Range is Greater than End Range !");
                             document.form.SRANGE.value = "";
                             document.form.SRANGE.focus();
                            return false;
		}  */
                

                if (balanceQty < receivingQty) {
                
                            alert(" Entered Range is greater than the Balance Qty to Receive!");
                            document.form.ERANGE.value = "";
                            document.form.ERANGE.focus();
                            return false;
		}


		if(locId=="" || locId.length==0 ) {
                    alert("Enter Location!");
                    document.getElementById("LOC").focus();
                    return false;
	       }else{
		var urlStr = "/track/InboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
                USERID : "<%=sUserId%>",
				PLANT : "<%=plant%>",
				ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
					addRow();   
                                        return true;
					} else {
						alert("Not a valid Location");
						var location = document.getElementById("LOC");
						location.value="";
						location.focus();
                                                return false;
					}
				}
			});
		}
               
  }
        
function isNumericInput(strString) {
	var strValidChars = "0123456789.-";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for (i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
}

</script>
<%
	EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
	Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER_AR);
	String template_subject = (String)emailMsg.get("SUBJECT");
	String template_body = (String)emailMsg.get("BODY1");
%>
<input type="hidden" id="plant_desc" value="<%=PLNTDESC %>" />
<input type="hidden" id="template_subject" value="<%=template_subject %>" />
<input type="hidden" id="template_body" value="<%=template_body %>" />
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
