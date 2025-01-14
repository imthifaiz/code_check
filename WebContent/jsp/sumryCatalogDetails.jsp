<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Catalog Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
	function popUpWin(URL) {
		subWin = window
				.open(
						URL,
						'Items',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
	}
	function popUpMobWin(URL) {
	 	subWin = window.open(URL, 'Catalog', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=350,height=500,left = 420,top = 140');
	}
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
        
	String res = "",sNewEnb="enabled";
	
	String action = "";
	StringBuffer detail1,detail2,detail3,detail4,detail5,detail6,detail7; 
	StrUtils strUtils = new StrUtils();
	detail1 = new StringBuffer();
	detail2 = new StringBuffer();
	detail3 = new StringBuffer();
	detail4 = new StringBuffer();
	detail5 = new StringBuffer();
	detail6 = new StringBuffer();
	detail7 = new StringBuffer();
	
	String RefNo   = "", Terms  = "", TermsDetails   = "",sQuery="",SoNo ="",Item ="",isActive="";
	String description1 = "",description2="",description3="", catlogPath="",OrderQty  = "",productid="",price="";
	
	res =  strUtils.fString(request.getParameter("result"));
	InstructionUtil insUtil = new InstructionUtil();
	Hashtable ht = new Hashtable();
	ht.put(IConstants.PLANT,plant);
	isActive= strUtils.fString(request.getParameter("ISACTIVE"));  
         
        //System.out.println("stepq"+step1.toString());          
	CatalogUtil catlogUtil = new CatalogUtil();
		Hashtable htsel = new Hashtable();
		htsel.put(IConstants.PLANT,plant);
		
	try {
		action = strUtils.fString(request.getParameter("action"));
			productid = request.getParameter("PRODUCTID");
			htsel.put(IConstants.PRODUCTID,productid);
		sQuery ="ISNULL(DETAIL1,'') DETAIL1,ISNULL(DETAIL2,'') DETAIL2,ISNULL(DETAIL3,'') DETAIL3,ISNULL(DETAIL4,'') DETAIL4,";
		sQuery = sQuery +"ISNULL(DETAIL5,'') DETAIL5,ISNULL(DETAIL6,'') DETAIL6,ISNULL(DETAIL7,'') DETAIL7,CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISACTIVE";
	         List m= catlogUtil.listCatalogDet(sQuery,htsel,"");
	         
	         for(int i=0;i<m.size();i++)
	         {
	        	Map insmp= (Map)m.get(i);
	         detail1.append(insmp.get("DETAIL1"));
	         detail2.append(insmp.get("DETAIL2"));
	         detail3.append(insmp.get("DETAIL3"));
	         detail4.append(insmp.get("DETAIL4"));
	         detail5.append(insmp.get("DETAIL5"));
	         detail6.append(insmp.get("DETAIL6"));
	         detail7.append(insmp.get("DETAIL7"));
	         description1 = (String)insmp.get("DESCRIPTION1");
	         description2 = (String)insmp.get("DESCRIPTION2");
	         description3 = (String)insmp.get("DESCRIPTION3");
	         price = StrUtils.currencyWtoutCommSymbol((String)insmp.get(IDBConstants.CATLOGPRICE));
	         isActive= (String)insmp.get(IDBConstants.ISACTIVE);
	         catlogPath = (String)insmp.get(IDBConstants.CATLOGPATH);
	         
	         }
	       
	        
	      
	} catch (Exception e) {
	}
	PlantMstDAO plantMstDAO = new PlantMstDAO();
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
    
    double priceVal ="".equals(price) ? 0.0d :  Double.parseDouble(price);
    price = StrUtils.addZeroes(priceVal, numberOfDecimal);
	
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

  <CENTER><strong><%=res%></strong></CENTER>  


  <form class="form-horizontal" name="form" method="post" enctype="multipart/form-data" action="">
    <input type="hidden" name="PLANT" value="<%=plant%>">
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Product ID">Product ID:</label>
      <div class="col-sm-4">
      <input class="form-control" type="text" name="PRODUCTID" value="<%=productid%>" size="50" MAXLENGTH=40 readonly>
   		 </div>
		</div>
	        
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Description1:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="DESCRIPTION1" value="<%=description1 %>" size="50" MAXLENGTH=80 readonly>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Price">Price:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="PRICE" size="50" MAXLENGTH=35 value="<%=price%>" readonly>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Description2:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="DESCRIPTION2" value="<%=description2 %>" size="50" MAXLENGTH=35 readonly>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Description3:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="DESCRIPTION3" value="<%=description3 %>" size="50" MAXLENGTH=35 readonly>
      </div>
    </div>
    
    <div class="form-group">
       <div class="col-sm-offset-4 col-sm-8">          
      	<input type="button" class="Submit btn btn-default" value="view catalog Image Details" size="50" 
      	onClick="javascript:popUpMobWin('catalogBrowseImage.jsp?PRODUCTID='+document.form.PRODUCTID.value+'&PLANT='+document.form.PLANT.value);">
      </div>
    </div>
           
    <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details1:</label>
      <div class="col-sm-4">          
		<textarea class="form-control" name="DETAIL1"  id="DETAIL1" cols="40" rows="2" readonly><%=detail1.toString()%></textarea>
	        </div>
        </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details2:</label>
      <div class="col-sm-4">          
		<textarea class="form-control" name="DETAIL2" cols="40" rows="2" readonly ><%=detail2.toString()%></textarea>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details3:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL3"  cols="40" rows="2"  readonly><%=detail3.toString()%></textarea>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details4:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL4"  cols="40" rows="2" readonly><%=detail4.toString()%></textarea>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details5:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL5"  cols="40" rows="2" readonly ><%=detail5.toString()%></textarea>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details6:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL6"  cols="40" rows="2"  readonly><%=detail6.toString()%></textarea>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details7:</label>
      <div class="col-sm-4">          
	  <textarea class="form-control" name="DETAIL7" cols="55" rows="2" readonly><%=detail7.toString()%></textarea>
			              </div>
    
    </div>
    
     <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y" disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div>
     
     <div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
	 <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
	 <button type="button" class="Submit btn btn-default" onClick="window.location.href='summryCatalog.jsp'"><b>Back</b></button>&nbsp;&nbsp;
	 	</div>
	 	</div>
        
    </form>
</div>
</div>
</div>


<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<script type="text/javascript">
	function onviewImage() {
		var plant = document.form.PLANT.value;
		var productid = document.form.PRODUCTID.value;
		if (productid.length > 0) {
			//alert(plant+productid);		
			document.form.action = "catalogImage.jsp?PLANT=" + plant
					+ "&PRODUCTID=" + productid + "&PAGE=DETAIL_LOG";

			document.form.submit();
		} else {
			alert('Please Enter ProductID');
		}

	}
	function onviewEnqImage() {
		var plant = document.form.PLANT.value;
		var productid = document.form.PRODUCTID.value;
		if (productid.length > 0) {

			document.form.action = "catalogEnqImage.jsp?PLANT=" + plant
					+ "&PRODUCTID=" + productid + "&PAGE=DETAIL_LOG";

			document.form.submit();
		} else {
			alert('Please Enter ProductID');
		}

	}
	function onviewRegImage()
	{
		var plant = document.form.PLANT.value;
		var productid =document.form.PRODUCTID.value; 
		if(productid.length>0){
		//alert(plant+productid);		
	 document.form.action  = "catalogRegisterImage.jsp?PLANT="+plant+"&PRODUCTID="+productid+"&PAGE=DETAIL_LOG";
			
		    document.form.submit();}
		else{
			alert('Please Enter ProductID');
		}
		
	}    
	function onviewBrowseImage()
	{
		var plant = document.form.PLANT.value;
		var productid =document.form.PRODUCTID.value; 
		if(productid.length>0){
		//alert(plant+productid);		
	 document.form.action  = "catalogBrowseImage.jsp?PLANT="+plant+"&PRODUCTID="+productid+"&PAGE=DETAIL_LOG";
			
		    document.form.submit();}
		else{
			alert('Please Enter ProductID');
		}
		
	}    
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

