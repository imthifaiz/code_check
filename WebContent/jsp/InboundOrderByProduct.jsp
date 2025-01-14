<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<%--New page design begin --%>
<%
String title = "Purchase Order By Product";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PURCHASE_TRANSACTION%>"/>
</jsp:include>
<style type="text/css">
.backpageul
{
	background-color: rgb(255, 255, 255);
    padding: 0px 10px;
    margin-bottom: 0px;
    margin-top: 15px;
}
.underline-on-hover:hover {
  		text-decoration: underline;
	}</style>
<%--New page design end --%>

 <!-- <script
	src="js/jquery-1.4.2.js"></script>  -->
<script src="js/json2.js"></script>
<script
	src="js/calendar.js"></script>
<script src="js/general.js"></script>
<script>

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'InboundOrderSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function popWin(vname){
    window.open('vendSumm.jsp?VENDNAME='+vname+'&P=Y');
}

function onReceive(form){
 var ischeck = false;
 var Traveler ;
 var concatTraveler="";
 var j=0;
var pono=document.form.ITEM.value;
   var i = 0;
   var noofcheckbox=1;
   if(pono==null||pono=="")
	{
	alert("Please select Product ID");	
	return false;
	}    
    if(document.form.AFLAG.value==null||document.form.AFLAG.value=="" ||document.form.AFLAG.value!="DATA")
    {
	   	alert("No Data's Found For Receiving");
	 	return false;
    }
   var noofcheckbox = document.form.chkdDoNo.length;

   
	
    if(form.chkdDoNo.length == undefined)
    {
    		
            
            if(form.chkdDoNo.checked)
            {
              document.form.TRAVELER.value=form.chkdDoNo.value+"=";
              return true;
            }
            
            else
            {
               alert("Please Select Order For Receive");
               return false;
            }
    
    }else 
    {

    	
             for (i = 0; i < noofcheckbox; i++ )
              {
               ischeck = document.form.chkdDoNo[i].checked;
                   if(ischeck)
                    {
                      j=j+1;
                      Traveler=document.form.chkdDoNo[i].value;
                      concatTraveler=concatTraveler+Traveler+"=";
                    }
   
               }
               
              
              if(j==0)
              {
                alert("Please Select Order For Receive");
                return false;
              }
              document.form.TRAVELER.value=concatTraveler;
             return true;
            
    }
   
    
    return false;
   }
   
  function ref()
{

  document.form.ITEM.focus();
  
}  
    
</script>

<%-- 
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp"%> --%>
<jsp:useBean id="gn" class="com.track.gates.Generator" />

<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />

<%
    POUtil _poUtil=new POUtil();
    _poUtil.setmLogger(mLogger);
    ItemMstDAO itemdao = new ItemMstDAO();
    session = request.getSession();String itemno="",  itemdesc="";

    String action   = StrUtils.fString(request.getParameter("action")).trim();
    String pono     = StrUtils.fString(request.getParameter("PONO"));
    String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    String RFLAG=    (String) session.getAttribute("RFLAG");
    String AFLAG=    (String) session.getAttribute("AFLAG");
   itemno=StrUtils.fString(request.getParameter("ITEM"));
   itemdesc=StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("DESC")));
   String uom=StrUtils.fString(request.getParameter("UOM"));
    String vend = "",deldate="",jobNum = "",custName = "",personIncharge = "",contactNum = "",chkString ="";
    String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="",
    contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="";
    String sSaveEnb    = "disabled";
    String fieldDesc="<tr><td align=\"center\"> Please enter any search criteria</td></tr>";
    ArrayList al=new ArrayList();
     if(action.equalsIgnoreCase("View")){
     // try{
     //fieldDesc="<tr><td> </td></tr>";
     fieldDesc=(String)request.getSession().getAttribute("RESULTINBRECEIVE");
     fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
     

     itemdesc=itemdao.getItemDesc(plant,itemno);
     itemdesc = StrUtils.replaceCharacters2Send(itemdesc);
     itemdesc = StrUtils.replaceCharacters2Recv(itemdesc);

        //al= _poUtil.listInboundSummaryByProd(itemno,plant);
        al= _poUtil.listInboundSummaryByProdWithext(itemno,plant);  //not by Draft - azees
      
    }
  
    else if(action.equalsIgnoreCase("catcerror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
    
    request.getSession().setAttribute("RESULTINBRECEIVE","");
    request.getSession().setAttribute("podetVal","");
    request.getSession().setAttribute("CATCHERROR","");
      
  //}
 
%>

<%--New page design begin --%>
<center>
<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
 <div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <Ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../purchaseTransactionDashboard"><span class="underline-on-hover">Purchase Transaction Dashboard</span> </a></li>
                <li>Purchase Order By Product</li>                                   
            </Ul>             
     <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
         <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../purchaseTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	 </h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

  <form class="form-horizontal" name="form" method="post" action="/track/OrderReceivingServlet?">
    	    	
    	<div class="form-group">
     	<label class="control-label col-sm-2" for="Product id">
      	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product ID:</label>
      	<div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" type="TEXT" size="30" MAXLENGTH=50 name="ITEM" value="<%=StrUtils.forHTMLTag(itemno)%>" onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){viewInboundOrders();}">
   		<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/list/item_list_po.jsp?ITEM='+form.ITEM.value);">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px"></i></a></span>
  		</div>
  		</div>
    	<div class="form-inline">
     	<label class="control-label col-sm-2" for="Product desc">Description:</label>
      	<div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="DESC" type="TEXT" value="<%=StrUtils.forHTMLTag(itemdesc)%>" size="30" MAXLENGTH=80>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/list/item_list_po.jsp?ITEM='+form.ITEM.value+'&DESC='+form.DESC.value);">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px"></i></a></span>
  		</div>
  		</div>
    	</div>
    	<div class="form-inline">
     	<div class="col-sm-1">
      	<button type="button" class="Submit btn btn-default" name="actionButton" onClick="viewInboundOrders();"><b>View</b></button>
  		</div>
    	</div>
    	<INPUT  type="hidden"  name="UOM" value="" >
 		</div>
 		
 		<INPUT type="hidden" name="CUST_CODE" value="">
        <INPUT type="hidden" name="UNITCOST" value="">
        <INPUT type="hidden" name="CUST_CODE1" value=""> 
        <INPUT type="hidden" name="LOGIN_USER" value="<%=sUserId%>">
        <input type="hidden" name="DETAILDESC">
        <input type="hidden" name="MANUFACT">    
     
     	<TABLE class="table" border="0">
		<thead style="background: #eaeafa; font-size: 15px;" >			
		<tr>
		<th width="5%">SELECT</th>
        <th width="15%">ORDER NO</th>
		<th width="5%">ORDERLINE NO</th>
	    <th width="10%">SUPPLIER NAME</th>
	    <th width="10%">UOM</th>
		<th width="10%">&nbsp;&nbsp;ORDER QUANTITY</th>
		<th width="10%">&nbsp;&nbsp;RECEIVED QUANTITY</th>
		<th width="5%">&nbsp;&nbsp;STATUS </th>
		</tr>
		</thead>
		<tbody style="font-size: 15px;">
		<% 
               
       if(al.size()==0)
       {
    	   AFLAG="";
       }
      
       if(al.size()>0)
       {
        ItemMstDAO _ItemMstDAO= new ItemMstDAO();
        InvMstDAO _InvMstDAO= new InvMstDAO();
        _ItemMstDAO.setmLogger(mLogger);
        _InvMstDAO.setmLogger(mLogger);
        AFLAG="DATA";
      
       for(int i=0 ; i<al.size();i++)
       {
          
          Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
          String strpono = (String)m.get("pono");
          String polnno = (String)m.get("polnno");
          String sname= (String)m.get("sname");
          String item= (String)m.get("item");
          Hashtable htCond= new Hashtable();
          //Retrieve order hdr details
                htCond.put("PLANT", plant);
        htCond.put("PONO", strpono);
    
        String query = " a.pono,a.custName,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
       List alist = _poUtil.getSupplierHdrDetails(query, htCond, "");
    
        if (alist.size() > 0) {
               Map linemap = (Map) alist.get(0);
                jobNum=(String)linemap.get("jobNum");
       custName=(String)linemap.get("custName");
       personIncharge=(String)linemap.get("contactname");
      
       contactNum=(String)linemap.get("contactNum");
       telno=(String)linemap.get("telno");
       email=(String)linemap.get("email");
       add1=(String)linemap.get("add1");
       add2=(String)linemap.get("add2");
       add3=(String)linemap.get("add3");
       add4=(String)linemap.get("add4");
      
            }

          String qtyor= (String)m.get("qtyor");
          String qtyrc= (String)m.get("qtyrc");
          String ref=(String)m.get("ref");
          String lnstat= (String)m.get("lnstat");
          String uom1= (String)m.get("uom");
          String UOMQTY= (String)m.get("UOMQTY");
          chkString  =strpono+","+polnno+","+item+","+StrUtils.replaceCharacters2Send(itemdesc)+","+custName+","+uom1+","+UOMQTY+","+personIncharge+","+telno+","+email+","+add1+","+add2+","+add3+","+qtyor+","+qtyrc+","+ref;
         
      %>
					<TR bgcolor="<%=bgcolor%>">
					<TD width="5%" align="left"><font color="black"><INPUT Type=radio style="border: 0;" name="chkdDoNo" value="<%=chkString%>"></font></TD>
                    <TD align="left" width="15%"><%=(String)m.get("pono")%></TD>
					<TD width="5%" align="center"><font color="black"><%=(String)m.get("polnno")%></font></TD>
                    <TD align="left" width="10%"><%=StrUtils.forHTMLTag(custName)%></TD>
                    <TD align="left" width="10%"><%=StrUtils.forHTMLTag(uom1)%></TD>
					<TD align="center" width="10%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
					<TD align="center" width="10%"><%=StrUtils.formatNum((String)m.get("qtyrc"))%></TD>
					<TD align="center" width="5%"><%=(String)m.get("lnstat")%></TD>
					</TR>
					<%}} else 
    	   {%>
					<TR>
						<td colspan="7" align="center">Data's Not Found</TD>
					</TR>
					<%}%>
				</tbody>
				</table>
				
				<INPUT type="hidden" size="20" MAXLENGTH=20	name="CONTACT_NUM" value="<%=contactNum%>">
				<INPUT type="Hidden" name="ENCRYPT_FLAG" value="1"> 
				<INPUT type="Hidden" name="RFLAG" value="1"> 
				<INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
                <INPUT type="hidden" size="20" MAXLENGTH=20	name="RECFLAG" value="ByProduct">
         
    	<div class="form-group">        
		<div class="col-sm-12" align="center">
	<!--	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='inboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="if(onReceive(document.form)) {submitForm();}"><b>Receive</b></button>&nbsp;&nbsp;
      	<INPUT	type="hidden" name="TRAVELER" value="">
      	</div>
    	</div>
  		</form>
		</div>
		
		
		
		 
		
		
<script>

ref();
function viewInboundOrders(){
			document.form.action="/track/OrderReceivingServlet?action=ViewProductOrders";
//                        document.form.action="InboundOrderByProduct.jsp?ITEM="
//							+ document.form.ITEM.value + "&action=View";
			//document.form.action.value ="View";
            document.form.submit();
		}
function submitForm(){
	document.form.action ="/track/OrderReceivingServlet?action=Receive";
    document.form.submit();  
}

        
        function validateProduct() {
	var productId = document.form.ITEM.value;
      
	if(document.form.ITEM.value=="" || document.form.ITEM.value.length==0 ) {
		alert("Enter Product ID!");
		document.form.ITEM.focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=plant%>",
				ACTION : "VALIDATE_PRODUCT_GETDETAIL"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						document.form.DESC.value = resultVal.discription;
                                                document.form.DETAILDESC.value = resultVal.detaildesc;
                                               
					} else {
						alert("Not a valid product or It is a parent item");
						document.form.ITEM.value = "";
						document.form.ITEM.focus();
					}
				}
			});
		}
	}
</script>

</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>