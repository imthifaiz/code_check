<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<%--New page design begin --%>
<%
String title = "Goods Receipt By Purchase Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<%--New page design end --%>

<script 
	src="js/calendar.js"></script>
<!-- <script 
	src="js/jquery-1.4.2.js"></script> -->
<script src="js/json2.js"></script>
<script src="js/general.js"></script>

<!-- <title>Goods Receipt by Inbound Order</title>
<link rel="stylesheet" href="css/style.css"> -->


<script>

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'InboundOrderList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
  
  
function validatePO(form)//form
{
 
   var frmRoot=document.form;
   
   if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 )
	 {
		alert("Please Enter ORDERNO!");
		frmRoot.ORDERNO.focus();
		return false;
   }
   
    if(frmRoot.LOC.value=="" || frmRoot.LOC.value.length==0 )
	 {
		alert("Please Enter LOC!");
		frmRoot.LOC.focus();
		return false;
   }
   
   if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Please Enter Product ID!");
		frmRoot.ITEMNO.focus();
		return false;
   }
   
     if(frmRoot.BATCH.value.length==0 || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter/Generate Batch!");
		frmRoot.BATCH.focus();
		return false;
   }
  
     if(frmRoot.RECEIVEQTY.value=="" || frmRoot.RECEIVEQTY.value.length==0 ||frmRoot.RECEIVEQTY.value<=0)
	 {
		alert("Please Enter valid Receiving Qty!");
		frmRoot.RECEIVEQTY.focus();
		return false;
   }
      
   
     if(frmRoot.TRANSACTIONDATE.value.length==0 || frmRoot.TRANSACTIONDATE.value.length==0 )
	 {
    	 alert("Please select Transaction Date");	
		frmRoot.TRANSACTIONDATE.focus();
		return false;
   }
  
   if(isNaN(document.form.RECEIVEQTY.value)) {alert("Please enter valid Receiving Qty.");document.form.RECEIVEQTY.focus(); return false;}
   
   else
    {
	   var grnval = document.form.GRNO.value.length;
		 if(grnval>0)
		 {
	   document.form.action ="/track/OrderReceivingByPOServlet?action=Receive";
       // document.form.action.value ="Receive";
       document.form.submit();
		 }
		 else
			 {
			 if (confirm('Are you sure to submit this transaction without GRNO?')) {
				 document.form.action ="/track/OrderReceivingByPOServlet?action=Receive";			       
			       document.form.submit();
			 } else {
				 document.getElementById("GRNO").focus();
			 }		 
			 }
    }
 
}

function validatePOAndSendEmail(form)//form
{
 
   var frmRoot=document.form;
   
   if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 )
	 {
		alert("Please Enter ORDERNO!");
		frmRoot.ORDERNO.focus();
		return false;
   }
   
    if(frmRoot.LOC.value=="" || frmRoot.LOC.value.length==0 )
	 {
		alert("Please Enter LOC!");
		frmRoot.LOC.focus();
		return false;
   }
   
   if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Please Enter Product ID!");
		frmRoot.ITEMNO.focus();
		return false;
   }
   
     if(frmRoot.BATCH.value.length==0 || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter/Generate Batch!");
		frmRoot.BATCH.focus();
		return false;
   }
  
     if(frmRoot.RECEIVEQTY.value=="" || frmRoot.RECEIVEQTY.value.length==0 ||frmRoot.RECEIVEQTY.value<=0)
	 {
		alert("Please Enter valid Receiving Qty!");
		frmRoot.RECEIVEQTY.focus();
		return false;
   }

     if(frmRoot.TRANSACTIONDATE.value.length==0 || frmRoot.TRANSACTIONDATE.value.length==0 )
	 {
    	 alert("Please select Transaction Date");	
		frmRoot.TRANSACTIONDATE.focus();
		return false;
   }
  
   if(isNaN(document.form.RECEIVEQTY.value)) {alert("Please enter valid Receiving Qty.");document.form.RECEIVEQTY.focus(); return false;}
   
   else
    {
	   var grnval = document.form.GRNO.value.length;
		 if(grnval>0)
		 {
       urlStr = "/track/OrderReceivingByPOServlet?action=Receive";
		var formData = $('#frmInboundOrderReceiving').serialize();
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
		 else
			 {
			 if (confirm('Are you sure to submit this transaction without GRNO?')) {
				 document.form.action ="/track/OrderReceivingByPOServlet?action=Receive";			       
			       document.form.submit();
			 } else {
				 document.getElementById("GRNO").focus();
			 }		 
			 }
    }
 
}

  function validateBatchGen(form)
{
 
   var frmRoot=document.form;
   
   if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 )
	 {
		alert("Please Enter ORDERNO!");
		frmRoot.ORDERNO.focus();
		return false;
   }
   
   else if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Please Enter ITEMNO!");
		frmRoot.ITEMNO.focus();
		return false;
   }
   
   
   else
   {
	   return true;
	   
    }
}

function onClear(){
 

  document.form.ORDERNO.value="";
  document.form.CUSTNAME.value="";
  document.form.ITEMNO.value="";
  document.form.ITEMDESC.value="";
  document.form.LOC.value="";
  document.form.BATCH.value="";
  document.form.REF.value="";
  document.form.ORDERQTY.value="";
  document.form.RECEIVEDQTY.value="";
  document.form.RECEIVEQTY.value="";
  document.form.CONTACTNAME.value="";
  document.form.TELNO.value="";
  document.form.EMAIL.value="";
  document.form.ADD1.value="";
  document.form.ADD2.value="";
  document.form.ADD3.value="";
 
  
  return true;
}

</script>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
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
       String pono     = StrUtils.fString(request.getParameter("PONO"));
       
       String  fieldDesc="";
       String   ORDERNO    = "",ORDERLNO="",CUSTNAME = "", ITEMNO   = "", ITEMDESC  = "",recflg="",
       LOC   = "" , BATCH  = "", REF   = "",
       ORDERQTY = "", RECEIVEDQTY="",RECEIVEQTY="",EXPIREDATE="", 
       BALANCEQTY="",CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="",UOM="",TRANSACTIONDATE="",UOMQTY="";
       
       ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=StrUtils.fString(request.getParameter("ORDERLNO"));
       CUSTNAME = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUSTNAME")));
       ITEMNO = StrUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("ITEMDESC")));
       LOC = StrUtils.fString(request.getParameter("LOC"));
       BATCH = StrUtils.fString(request.getParameter("BATCH"));
       EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
       recflg =StrUtils.fString(request.getParameter("RECFLAG"));
       ItemMstDAO itemdao = new ItemMstDAO();
       //UOM = itemdao.getItemUOM(plant,ITEMNO);
       UOM = StrUtils.fString(request.getParameter("UOM"));
       UOMQTY = StrUtils.fString(request.getParameter("UOMQTY"));
	   TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
       String curDate =DateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
      
       //Get podet 
       PoDetDAO  _PoDetDAO  = new PoDetDAO();  
       //List listQry =  _PoDetDAO.getInboundItemListByWMS(plant,ORDERNO,ITEMNO);
       //Adding lnno to resolve duplicates
       List listQry =  _PoDetDAO.getInboundItemListByWMS(plant,ORDERNO,ITEMNO,ORDERLNO);
       for(int i =0; i<listQry.size(); i++) {
          Map m=(Map)listQry.get(i);
          
          ORDERQTY =(String)m.get("qtyor");
          RECEIVEDQTY =StrUtils.formatNum((String)m.get("qtyrc"));
          BALANCEQTY=(String)m.get("balanceqty");
          REF= (String)m.get("ref");
       }
        //Get end
       
       //Get Supplier Details
          Hashtable ht=new Hashtable();
         String extCond="";
         ht.put("PLANT",plant);
         ht.put("PONO",ORDERNO);
    
  
        if(ORDERNO.length()>0) extCond="a.plant='"+plant+"' and a.pono = '"+ORDERNO+"' and a.custname=b.vname";
         extCond=extCond+" order by a.pono desc";
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
<%--New page design begin --%>
<center>
	<h2><small class="success-msg"> <%=fieldDesc%></small></h2>
</center>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
             <ul class="breadcrumb backpageul" >      	
                  <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                        
                  <li><a href="../purchaseorder/summary"><span class="underline-on-hover">Purchase Order Summary</span></a></li> 
                  <li><a href="../purchaseorder/detail?pono=<%=ORDERNO%>"><span class="underline-on-hover"> Purchase Order Detail</span></a></li>
                  <li><label>Purchase Receipt</label></li>                                    
             </ul>   
     <!-- Muruganantham Modified on 16.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>


<form class="form-horizontal" name="form" id="frmInboundOrderReceiving" method="post" action="/track/OrderReceivingByPOServlet?">

	      
       <div class="form-group">
       <label class="control-label col-sm-2" for="Order No">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
        <div class="col-sm-3"> 
        <INPUT class="form-control" readonly name="ORDERNO" id="ORDERNO" type = "TEXT" value="<%=ORDERNO%>" size="30"  MAXLENGTH=20>
   		</div>
   		
  		<div class="form-inline">
        <label class="control-label col-sm-2" for="location">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Location:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="LOC" type = "TEXT" value="<%=LOC%>" size="30"  onkeypress="if((event.keyCode=='13') && ( document.form.LOC.value.length > 0)){validateLocation();}" MAXLENGTH=80>
        <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/loc_list_receivewms.jsp?LOC='+form.LOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
        </div>
        <!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Unit No">Unit No:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD1"  class="form-control" value="<%=ADD1%>"  type = "hidden" size="30"  MAXLENGTH=80 readonly >
        <!-- </div>
 		</div> -->
 		</div>
 		<input type="hidden" name="RECFLAG" value='<%=recflg%>'>
 					
        <div class="form-group">
        <label class="control-label col-sm-2" for="Supplier Name">Supplier Name:</label>
        <div class="col-sm-3">
       <INPUT name="CUSTNAME" id="CUSTNAME" class="form-control" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUSTNAME)%>" size="30"  MAXLENGTH=80 readonly>
       </div>
       <div class="form-inline">
        <label class="control-label col-sm-2" for="Batch">Batch:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="BATCH" type = "TEXT" value="<%=BATCH%>" size="30"   onkeypress="javascript:selectAll();"  MAXLENGTH=40>
        <span class="input-group-addon" name="actionBatch" onClick="generateBatch();">
   		 	<a href="#" data-toggle="tooltip"  data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Building">Building:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD2" value="<%=ADD2%>" class="form-control" type = "hidden" value="" size="30"  MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
       </div>
       
    	<div class="form-group">
    	<label class="control-label col-sm-2" for="Person Incharge">Contact Name:</label>
        <div class="col-sm-3">
        <INPUT name="CONTACTNAME" class="form-control" value="<%=CONTACTNAME%>"  type = "TEXT"  size="30"  MAXLENGTH=80 readonly>
        </div>
        <div class="form-inline">
        <label class="control-label col-sm-2" for="Transaction Date">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Transaction Date:</label>
<!--         <label class="control-label col-sm-2 required" for="Transaction Date">Transaction Date:</label> -->
        <div class="col-sm-3">
        <div class="input-group">
        <%if(SETCURRENTDATE_GOODSRECEIPT.equals("1")){%>          
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" value="<%=TRANSACTIONDATE%>"type = "TEXT" readonly="readonly"  size="30"  MAXLENGTH=80 
        onkeypress="if((event.keyCode=='13') && ( document.form.RECEIVEQTY.value.length > 0)){document.form.actionSubmit.focus();}"  >
        <%}else {%>
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" value=""type = "TEXT" readonly="readonly"  size="30"  MAXLENGTH=80 
        onkeypress="if((event.keyCode=='13') && ( document.form.RECEIVEQTY.value.length > 0)){document.form.actionSubmit.focus();}"  >
        <%}%>
      	</div>
    	</div>
    	</div>
 		</div>
 		
 
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" readonly="readonly" name="ITEMNO" type = "TEXT" value="<%=ITEMNO%>" size="30"  MAXLENGTH=80>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Email Address">Telephone No:</label>
        <div class="col-sm-3"> -->
        <INPUT name="TELNO" class="form-control" value="<%=TELNO%>"  type = "hidden"  size="30"  MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="Expiry Date">Expiry Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="EXPIREDATE" value="<%= EXPIREDATE%>"type = "TEXT" readonly="readonly"  size="30"  MAXLENGTH=80 
        onkeypress="if((event.keyCode=='13') && ( document.form.RECEIVEQTY.value.length > 0)){document.form.actionSubmit.focus();}"  >
      	</div>
    	</div>
    	</div>
 		</div>
 		
 		<div class="form-group">
 		<label class="control-label col-sm-2" for="Product Description">Description:</label>
        <div class="col-sm-3">
        <INPUT name="ITEMDESC" class="form-control" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="35"  MAXLENGTH=80 readonly>
        </div>
        <!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Email">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT name="EMAIL" id="EMAIL" value="<%=EMAIL%>" class="form-control" type = "hidden"  size="30"  MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="GRNO">GRNO:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <input name="GRNO" ID="GRNO"  value="<%= grno %>" class="form-control" type="TEXT" value="" style="width: 100%" MAXLENGTH=80 />
        <span class="input-group-addon"  onClick="onNew()">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
   		 	</div>
        </div>
        </div>
 		</div>
  		
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Order Qty">Order Quantity:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT name="ORDERQTY" class="form-control" type = "TEXT" value="<%=StrUtils.formatNum(ORDERQTY)%>" size="30"  MAXLENGTH=80 readonly>
        <span class="input-group-btn"></span>
   		 	<INPUT name="UOM" class="form-control" type = "TEXT" value="<%=UOM%>" size="10"  MAXLENGTH=15 readonly>   		 	
        </div>
        </div>
        <div class="form-inline">
        <label class="control-label col-sm-2" for="Remarks">Remarks:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="REF" type = "TEXT" value="<%=REF%>" style="width: 100%"  MAXLENGTH=100>
        </div>
    	</div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Street">Street:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD3" value="<%=ADD3%>" class="form-control" type = "hidden" size="30"  MAXLENGTH=80 readonly >
        <INPUT name="UOMQTY" class="form-control" type = "hidden" value="<%=UOMQTY%>" >
        <!-- </div>
 		</div> -->
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Received Qty">Recevied Quantity:</label>
        <div class="col-sm-3">
        <INPUT name="RECEIVEDQTY" class="form-control" type = "TEXT" value="<%=RECEIVEDQTY%>"  size="35"  MAXLENGTH=80 readonly>
        </div>
        <div class="form-inline">
        <label class="control-label col-sm-2" for="Receiving Qty">
        <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Receving Quantity:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="RECEIVEQTY" value="<%= RECEIVEQTY%>"type = "TEXT"  style="width: 100%"  MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.RECEIVEQTY.value.length > 0)){document.form.actionSubmit.focus();}"  >
        </div>
    	</div>
    	</div> 
 	 		      	
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
      	 <%if(recflg.equalsIgnoreCase("ByProduct")){%>
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../purchasetransaction/receiptsummarybyproduct?ITEM=<%=ITEMNO%>&action=View'"><b>Back</b></button>
      	<% }else{%>
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='InboundOrderSummary.jsp?PONO=<%=ORDERNO%>&action=View'"><b>Back</b></button>
      	<% }%>&nbsp;&nbsp;
	  	</div>
      	</div>
    	</div>

		<INPUT     name="ORDERLNO"  type ="hidden" value="<%=ORDERLNO%>" size="1"   MAXLENGTH=80 >
  		<INPUT     name="BALANCEQTY"  type ="hidden" value="<%=BALANCEQTY%>" size="1"   MAXLENGTH=80 >
   		<INPUT     name="LOGIN_USER"  type ="hidden" value="<%=sUserId%>" size="1"   MAXLENGTH=80 >
    	  		
  		</form>
		</div>
		</div>
		</div>



<script>

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

function validateLocation() {
	var locId = document.form.LOC.value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.form.LOC.focus();
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
						document.form.RECEIVEQTY.value = "";
						document.form.RECEIVEQTY.focus();
					} else {
						alert("Not a valid Location");
						document.form.LOC.value = "";
						document.form.LOC.focus();
					}
				}
			});
		}
	}

function generateBatch(){

	var urlStr = "/track/InboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		ACTION : "GENERATE_BATCH"
	},
	dataType : "json",
	success : function(data) {
		if (data.status == "100") {
			var resultVal = data.result;
			document.form.BATCH.value = resultVal.batchCode;
			document.form.RECEIVEQTY.focus();

		} else {
			alert("Unable to genarate Batch");
			document.form.BATCH.value = "NOBATCH";
			document.form.BATCH.focus();
		}
	}
});
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
document.form.LOC.focus();

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
