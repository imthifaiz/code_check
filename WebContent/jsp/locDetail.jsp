<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Location Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">

	var subWin = null;
	function popUpWin(URL) {
	 subWin = window.open(URL, 'LOCATIONS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
	function onNew(){
   	 document.form1.action  = "/Wms/locmstservlet?action=NEW";
   	 document.form1.submit();
	}
	function onGenID(){
	 document.form1.action  = "../jsp/LocMst_View.jsp?action=Auto_ID";
 	 document.form1.submit();
   }
   function onAdd(){
     var LOC_ID   = document.form1.LOC_ID.value;
     if(LOC_ID == "" || LOC_ID == null) {alert("Please Enter Location Id"); return false; }
     document.form1.action  = "/Wms/locmstservlet?action=ADD";
     document.form1.submit();
	}
	function onUpdate(){
   	 var LOC_ID   = document.form1.LOC_ID.value;
     if(LOC_ID == "" || LOC_ID == null) {alert("Please Enter Location Id"); return false; }
     document.form1.action  = "/Wms/locmstservlet?action=UPDATE&LOC_ID=" + LOC_ID;
     document.form1.submit();
	}

	function onDelete(){
   	 var LOC_ID   = document.form1.LOC_ID.value;
     if(LOC_ID == "" || LOC_ID == null) {alert("Please Enter Location Id");  return false; }
     confirm("Do you want to delete location?");
     document.form1.action  = "/Wms/locmstservlet?action=DELETE&LOC_ID=" + LOC_ID;
     document.form1.submit();
    }

	function onView(){

   	 var LOC_ID   = document.form1.LOC_ID.value;
     if(LOC_ID == "" || LOC_ID == null) 
     {
      alert("Please Enter Location Id"); 
      return false; 
     }

     document.form1.action  = "/Wms/locmstservlet?action=VIEW";
     document.form1.submit();
    }
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String loc_type_id = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
	String loc_type_id2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
	String loc_type_id3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
	String res        = "";

	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String   sAddEnb    = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb   = "enabled";

	String action     = "";
	String sLocId  = "",
         isActive  = "",
    sLocDesc  = "",
    sRemarks     = "";
	String sAddr1  = "", scomname = "", srcbno = "",
    sAddr2  = "",
    sAddr3  = "", sAddr4  = "",
    sCountry   = "",sState   = "", 
    sZip   = "",
    sTelNo = "", sFax = "" ;
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	LocTypeUtil loctypeutil = new LocTypeUtil();

	try	{action= strUtils.fString(request.getParameter("action"));}
	catch(Exception e){	}
	//start code by deen to get location type list on 26 dec 2013
	List loctypelist=loctypeutil.getLocTypeList("",plant," AND ISACTIVE ='Y'");
	List loctypelisttwo=loctypeutil.getLocTypeListtwo("",plant," AND ISACTIVE ='Y'");
	List loctypelistthree=loctypeutil.getLocTypeListthree("",plant," AND ISACTIVE ='Y'");
	//end code by deen to get location type list on 26 dec 2013


	//1. >> New
	if(action.equalsIgnoreCase("Clear")){
  	   System.out.println("Action : " + action) ;
       action     = "";
       sLocId  = "";
       sLocDesc  = "";
       sRemarks     = "";
       scomname = "" ;
       srcbno = "";
       sAddr1  = "" ;
       sAddr2  = "";
       sAddr3  = "";
       sAddr4  = "";
       sCountry   = "";sState   = "";
       sZip   = "";
       sTelNo = "";
       sFax = "";
     }
    else if(action.equalsIgnoreCase("view")){
    	String sLoctype = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
    	String sLoctype2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
    	String sLoctype3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
    	String sLoc = strUtils.fString(request.getParameter("LOC_ID"));
    	
    	 Hashtable ht = new Hashtable();
     	 ht.put("LOC_TYPE_ID",sLoctype);
     	 ht.put("LOC_TYPE_ID2",sLoctype2);
     	 ht.put("LOC_TYPE_ID3",sLoctype3);
   	ArrayList arrCust=new ArrayList();
	LocUtil _LocUtil = new LocUtil();
    	arrCust = _LocUtil.getLocDetails(sLoc,plant," ",ht);
		
		  Map arrCustLine = (Map)arrCust.get(0);
		  sLocId  = strUtils.fString((String)arrCustLine.get("LOC"));
  	 sLocDesc = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOCDESC")));
  	 sRemarks   = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("USERFLD1")));
  	 isActive  = strUtils.fString((String)arrCustLine.get("ISACTIVE"));
      //below lines added by deen
       scomname =  strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("COMNAME")));
       srcbno =  strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("RCBNO")));
       sAddr1    =  strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD1")));
       sAddr2    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD2")));
       sAddr3    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD3")));
       sAddr4    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD4")));
       sState  = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("STATE")));
      sCountry  = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("COUNTRY")));
      sZip      = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ZIP")));
      sTelNo    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("TELNO")));
      sFax      = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("FAX")));
  //   schkstatus = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("CHKSTATUS")));
       loc_type_id = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID")));
       loc_type_id2 = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID2")));
       loc_type_id3 = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID3")));
      
//    	 	sLocId   = request.getParameter("LOC");
//      	sLocDesc   = strUtils.replaceCharacters2Recv(request.getParameter("LOCDESC"));
//      	sRemarks      = request.getParameter("USERFLD1");
//         isActive      = request.getParameter("ISACTIVE");
//         scomname   =  request.getParameter("COMNAME");
//         srcbno   =  request.getParameter("RCBNO");
//         System.out.println(scomname);
//         System.out.println(request.getParameter("ADD1"));
//         sAddr1    = strUtils.replaceCharacters2Recv(request.getParameter("ADD1"));
//        	sAddr2    = strUtils.replaceCharacters2Recv(request.getParameter("ADD2"));
//        	sAddr3   = strUtils.replaceCharacters2Recv(request.getParameter("ADD3"));
//        	sAddr4   = strUtils.replaceCharacters2Recv(request.getParameter("ADD4"));
       	
//        	sState  = request.getParameter("STATE");
//        	sCountry  = request.getParameter("COUNTRY");
//        	sZip     = request.getParameter("ZIP");
//        	sTelNo   = request.getParameter("TELNO");
//        	sFax   = request.getParameter("FAX");
       	
}

	else if(action.equalsIgnoreCase("SHOW_RESULT")){

    	res =request.getParameter("result");
    	Hashtable arrCust=(Hashtable)request.getSession().getAttribute("locMstData");
    	sLocId   = (String)arrCust.get("LOC");
    	sLocDesc   = (String)arrCust.get("LOCDESC");
    	sRemarks      = (String)arrCust.get("USERFLD1");
       	isActive      = request.getParameter("ISACTIVE");
       	scomname = (String) arrCust.get("COMNAME");
       	srcbno = (String) arrCust.get("RCBNO");
		sAddr1  = (String) arrCust.get("ADD1");
		sAddr2  = (String) arrCust.get("ADD2");
		sAddr3  = (String) arrCust.get("ADD3");
		sAddr4  = (String) arrCust.get("ADD4");
		sState  = (String) arrCust.get("STATE");
		sCountry  = (String) arrCust.get("COUNTRY");
		sZip   = (String) arrCust.get("ZIP");
		sTelNo = (String) arrCust.get("TELNO");
		sFax = (String) arrCust.get("FAX");
        	
  
	}
	else if(action.equalsIgnoreCase("UPDATE")){
  		sCustEnb    = "disabled";
  
	}


%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../location/summary"><span class="underline-on-hover">Location Summary</span></a></li>                      
                <li><label>Location Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                 <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../location/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
 
   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group" style="text-align:left">
      <label class="control-label col-form-label col-sm-4" for="Location ID">Location ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Location ID:</label> -->
      <div class="col-sm-4">
      	   
    		<input name="LOC_ID" type="TEXT" value="<%=sLocId%>"
			size="50" MAXLENGTH=100<%=sCustEnb%> class="form-control" readonly>
   		 	
  		
  		<INPUT type="hidden" name="LOC_ID1" value="<%=sLocId%>">
      	
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Location Description">Location Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="LOC_DESC" type="TEXT" value="<%=sLocDesc%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Location Type">Location Type One</label>
      <div class="col-sm-4">          
        	
        	           
            <SELECT class="form-control" data-toggle="dropdown" data-placement="right" NAME="LOC_TYPE_ID" size="1" disabled="disabled">
			<OPTION selected value="NOLOCTYPE"></OPTION>
			<%       for (int i =0; i<loctypelist.size(); i++){
  			Map map = (Map) loctypelist.get(i);
 
  			String loctypeid         = (String) map.get("LOC_TYPE_ID");
  			String desc   = (String) map.get("LOC_TYPE_DESC");%>
			<OPTION value="<%=loctypeid %>" <%if(loc_type_id.equalsIgnoreCase(loctypeid)){%> selected <%}%>><%=desc%></OPTION>
			<%}%></SELECT>


  	</div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Location Type">Location Type Two</label>
      <div class="col-sm-4">          
        	
        	           
            <SELECT class="form-control" data-toggle="dropdown" data-placement="right" NAME="LOC_TYPE_ID2" size="1" disabled="disabled">
			<OPTION selected value="NOLOCTYPE"></OPTION>
			<%       for (int i =0; i<loctypelisttwo.size(); i++){
  			Map map = (Map) loctypelisttwo.get(i);
 
  			String loctypeid2         = (String) map.get("LOC_TYPE_ID2");
  			String desc2   = (String) map.get("LOC_TYPE_DESC2");%>
			<OPTION value="<%=loctypeid2 %>" <%if(loc_type_id2.equalsIgnoreCase(loctypeid2)){%> selected <%}%>><%=desc2%></OPTION>
			<%}%></SELECT>


  	</div>
      </div>
      
            <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Location Type">Location Type Three</label>
      <div class="col-sm-4">          
        	
        	           
            <SELECT class="form-control" data-toggle="dropdown" data-placement="right" NAME="LOC_TYPE_ID3" size="1" disabled="disabled">
			<OPTION selected value="NOLOCTYPE"></OPTION>
			<%       for (int i =0; i<loctypelistthree.size(); i++){
  			Map map = (Map) loctypelistthree.get(i);
 
  			String loctypeid3         = (String) map.get("LOC_TYPE_ID3");
  			String desc3   = (String) map.get("LOC_TYPE_DESC3");%>
			<OPTION value="<%=loctypeid3 %>" <%if(loc_type_id3.equalsIgnoreCase(loctypeid3)){%> selected <%}%>><%=desc3%></OPTION>
			<%}%></SELECT>


  	</div>
      </div>
      
            <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Remarks">Remarks</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100 readonly>
			</div>
    </div>
     <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Company Name">Company Name</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="COMNAME" type="TEXT" value="<%=scomname%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
	<div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Bussiness Registration">Business Registration No</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="RCBNO" type="TEXT" value="<%=srcbno%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Unit No">Unit No</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ADDR1" type="TEXT" value="<%=sAddr1%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Building">Building</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ADDR2" type="TEXT" value="<%=sAddr2%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Street">Street</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ADDR3" type="TEXT" value="<%=sAddr3%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="City">City</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ADDR4" type="TEXT" value="<%=sAddr4%>"
			size="50" MAXLENGTH=50 readonly>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="State">State</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="STATE" type="TEXT" value="<%=sState%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Country">Country</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Postal Code">Postal Code</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ZIP" type="TEXT" value="<%=sZip%>"
			size="50" MAXLENGTH=15 readonly>
      </div>
      </div>
     
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Tel No">Telephone No</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="TELNO" type="TEXT" value="<%=sTelNo%>"
			size="50" MAXLENGTH=20 readonly>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Fax">Fax No</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="FAX" type="TEXT" value="<%=sFax%>"
			size="50" MAXLENGTH=20 readonly>
      </div>
      </div>  
       <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y" disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%> >Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%> >Non Active
    </label>
     </div> 
</div>

<div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
<!--      <button type="button" class="Submit btn btn-default" onClick="window.location.href='locSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
	 <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
	 	</div>
	 	</div>
	 	
      </form>
</div>
</div>
</div>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

