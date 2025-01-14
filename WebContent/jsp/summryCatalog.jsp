<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.ItemSesBeanDAO"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%>

<%
String title = "Catalog Summary";
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
    subWin = window.open(URL, 'Catalog', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
  
  function popUpMobWin(URL) {
	 	subWin = window.open(URL, 'Catalog', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
 function onGo(){
 
     document.form.submit();
}
</script>

<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	ArrayList locQryList = new ArrayList();
	
	String fieldDesc = "";
	String PLANT = "", item = "",descr="",extraCond ="",imageFile="",RECS_PER_PAGE="",PRD_CLS_ID="",PRD_TYPE_ID="",PRD_BRAND_ID= "";
	int Total = 0;
    int listRec=0;
	String SumColor = "";
	boolean flag = false;
	session = request.getSession();
	String PGaction = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT = session.getAttribute("PLANT").toString();
	item = strUtils.fString(request.getParameter("PRODUCTID"));
	descr = strUtils.fString(request.getParameter("DESCR"));
	PRD_CLS_ID= strUtils.fString(request.getParameter("PRD_CLS_ID"));
	PRD_TYPE_ID= strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	PRD_BRAND_ID= strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	CatalogUtil _catalogUtil = new CatalogUtil();
	_catalogUtil.setmLogger(mLogger);
    RECS_PER_PAGE = strUtils.fString(request.getParameter("RECS_PER_PAGE"));
	   
    int curPage      = 1;
	if(RECS_PER_PAGE.length()==0) RECS_PER_PAGE = "25";
	int recPerPage   = Integer.parseInt(RECS_PER_PAGE);
	long totalRec     = 0;
	String isDisabled ="disabled";
	String currentPage        = strUtils.fString(request.getParameter("cur_page"));
	String listRecSize =strUtils.fString(request.getParameter("listRecSize")); 
	if(listRecSize.length()==0){
	listRecSize ="0";
	}
	
	if (PGaction.equalsIgnoreCase("View")) {
		try {
			 currentPage="1";
			Hashtable ht = new Hashtable();
			if (strUtils.fString(PLANT).length() > 0)
				 ht.put("PLANT", PLANT);
			if(item.length()>0)
				 ht.put(IDBConstants.PRODUCTID, item);
			 if(descr.length()>0){
				 descr = new StrUtils().InsertQuotes(descr);
				 extraCond= "  DESCRIPTION1 like '%"+descr+"%'";
			 }
			 listRec = _catalogUtil.NoofRecords(ht, extraCond);
            
            int start =  Integer.parseInt(currentPage);
            int end = Integer.parseInt(currentPage)*recPerPage;
            
	
			locQryList = _catalogUtil.getCatalogList(PLANT,item,descr,"", start,end,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID);

			if (locQryList.size() == 0) {
				fieldDesc = "Data Not Found";
			}
		      if (listRec >0 )  {         totalRec = listRec; listRecSize=new Long(totalRec).toString();
	             isDisabled="";
	             }
	             if (currentPage.length() > 0)
	             {
	            try  {   curPage = (new Integer(currentPage)).intValue(); 
	            System.out.println("curPage :: "+curPage); 
	            }
	            catch (Exception e)   {   curPage = 1;                                      }
	            }

		} catch (Exception e) {
		}
	}
	
	
	if(PGaction.equalsIgnoreCase("PREVORNEXT")){
		 try{

		int start = ((new Integer(currentPage)-1)*recPerPage)+1;
		int end = (new Integer(currentPage)*recPerPage);//+1
		Hashtable ht = new Hashtable();
		if (strUtils.fString(PLANT).length() > 0)
			ht.put("PLANT", PLANT);
		if(item.length()>0)
			ht.put(IDBConstants.PRODUCTID, item);
		 if(descr.length()>0){
			 descr = new StrUtils().InsertQuotes(descr);
			 extraCond= " AND DESCRIPTION1 like '%"+descr+"%'";
	           }
		 locQryList = _catalogUtil.getCatalogList(PLANT,item,descr,"", start,end,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID);
		     if (locQryList != null)  {         totalRec = locQryList.size();
		     isDisabled="";
		     }
		     if (currentPage.length() > 0)
		     {
		     
		    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
		    catch (Exception e)   {   curPage = 1;                                      }
		    }
		   
		 }catch(Exception e) {System.out.println("Exception :getCatalogList"+e.toString()); }
		}

		    int totalPages = (Integer.parseInt(listRecSize) + recPerPage -1)/recPerPage;
		    if (curPage > Integer.parseInt(listRecSize)) curPage = 1;                    // Out of range

		    System.out.println("totalPages :: "+totalPages);
		    System.out.println("curPage :: "+curPage);
		    
		    PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
		%>
		<SCRIPT LANGUAGE="JavaScript">

		    var cur_page     = <%=curPage%>;                            // Current display page
		    var total_pages   = <%=totalPages%>;                         // The total number of records

		    // Display previous page of user list
		    function onPrev()
		    {
			if (cur_page <= 1)  return false;
				cur_page = parseInt(cur_page) -1;
				document.form.cur_page.value = cur_page;
				document.form.PGaction.value="PREVORNEXT";
				document.form.action = "summryCatalog.jsp";
				document.form.submit();
		    }
		    //////////////////////////////////////////////////////////////////////////////////////////////////
		    // Display next page of user list
		    function onNext()
		    {
			    if (cur_page >= total_pages)  {
			      return false;
			    }else{
			      cur_page = parseInt(cur_page) + 1;
			      document.form.cur_page.value = cur_page;
			      document.form.PGaction.value="PREVORNEXT";
			      document.form.action = "summryCatalog.jsp";
			      document.form.submit();
			      }
		    }
		 </SCRIPT>
		 
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
<div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div>
	
<FORM class="form-horizontal" name="form" method="post" action="summryCatalog.jsp">
<input 	type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View"> 
<INPUT type="hidden" name="listRecSize" value="<%=listRecSize%>">
<input type="hidden" name="PRICE"></input>	
<input type="hidden" name="DESCRIPTION1"></input>	
	
	
	
<div id="target" style="display:none">
<div class="form-group">
  <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
      <div class="col-sm-2">
         <div class="input-group"> 
         <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />    
    		<input class="form-control" name="PRODUCTID" type="TEXT" value="<%=item%>" size="20" MAXLENGTH=50>
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('list/labellistcatalog.jsp?ITEM='+form.PRODUCTID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product ID Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class="form-inline">
   <label class="control-label col-sm-2" for="Product Description">Description:</label>
      <div class="col-sm-2">
         <div class="input-group">    
    		<input class="form-control" name="DESCR" type="TEXT" value="<%=descr%>" size="20" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/labellistcatalog.jsp?DESCR='+form.DESCR.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Description Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
       
<div class="form-inline">
   <label class="control-label col-sm-2" for="Product Class ID">Product Class ID:</label>
      <div class="col-sm-2">
         <div class="input-group">    
    		<input class="form-control" name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="20"  MAXLENGTH=100>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('PrdClsIdList.jsp?PRD_CLS_ID='+form.PRD_CLS_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Class ID Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
       </div>	
	
<div class="form-group">
  <label class="control-label col-sm-2" for="Product Type ID">Product Type ID:</label>
      <div class="col-sm-2">
         <div class="input-group">    
    		<input class="form-control" name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="20"  MAXLENGTH=100>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('productTypeIdList.jsp?ITEM_ID='+form.PRD_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Type ID Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class="form-inline">
   <label class="control-label col-sm-2" for="Product Brand ID">Product Brand ID:</label>
      <div class="col-sm-2">
         <div class="input-group">    
    		<input class="form-control" name="PRD_BRAND_ID"  type = "TEXT" value="<%=PRD_BRAND_ID%>" size="20"  MAXLENGTH=100>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand ID Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
       <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;&nbsp;
       <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
       
            <input type="hidden" name="PRD_CLS_DESC" value="">
            <input type="hidden" name="PRD_CLS_ID1" value="">
            <input type="hidden" name="PRD_TYPE_DESC" value="">
            <input type="hidden" name="PRD_TYPE_ID1" value="">
            <input type="hidden" name="PRD_BRAND_DESC" value="">
            <INPUT name="ACTIVE" type = "hidden" value="">
           
       </div>
       </div>
       
       
       <div class="form-group">
       <div class="col-sm-3">
       <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
       <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
       </div>
       <div class="ShowSingle">
       <div class="col-sm-offset-10">
        <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
        <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button> -->
  	 </div>
         </div>
       	    </div>
      
	<INPUT name="RECS_PER_PAGE" type = "hidden" value="<%=RECS_PER_PAGE%>" size="10"  MAXLENGTH=4 class="form-control">
	
<div style="overflow-x:auto;">
<table id="table" class="table table-bordered table-hover dataTable no-footer "
   role="grid" aria-describedby="tableInventorySummary_info" > 
   
   <thead style="background: #eaeafa;text-align: center">  
          <tr>  
            <th>S/N</th>  
            <th>Catalog Image</th>
            <th>ProductID</th>
            <th>Description1</th>
            <th>Price</th>
            <th>IsActive</th>    
          </tr>  
        </thead> 
        
        <tbody>

	<%
		for (int iCnt = 0; iCnt < locQryList.size(); iCnt++) {
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#dddddd";

			Map lineArr = (Map) locQryList.get(iCnt);
			imageFile = StrUtils.fString((String)lineArr.get(IDBConstants.CATLOGPATH));
			/* String catlogprice =StrUtils.currencyWtoutSymbol((String) lineArr.get(IConstants.CATLOGPRICE)); */
			String	prodid= (String) lineArr.get(IDBConstants.PRODUCTID);
			String strCatlogPrice=strUtils.fString((String)lineArr.get(IConstants.CATLOGPRICE));
			float catlogprice="".equals(strCatlogPrice) ? 0.0f :  Float.parseFloat(strCatlogPrice);
			strCatlogPrice = StrUtils.addZeroes(catlogprice, numberOfDecimal);
	%>

	<TR>
		<TD align="center"><%=(String)lineArr.get("ID")%></TD>

		<TD align="center">
         <a href="#" onClick="javascript:popUpMobWin('catalogBrowseImage.jsp?PRODUCTID=<%=prodid%>&PLANT=<%=PLANT%>');"><img src="/track/ReadFileServlet/?fileLocation=<%=imageFile%>" width="62" height="50"/></a>
		</TD>
		<TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;<a href="sumryCatalogDetails.jsp?PLANT=<%=PLANT%>&PRODUCTID=<%=prodid%>&PAGE=view"> <%=strUtils.fString((String)lineArr.get(IDBConstants.PRODUCTID))%></a></TD>
		<TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get(IConstants.DESCRIPTION1))%></TD>
		<TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strCatlogPrice%></TD>
		<TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=(String)lineArr.get(IConstants.ISACTIVE)%></TD>
	</TR>
	<%
		}
	%>

  <%-- <table align="center" >
     <TR>
   <td>  <input type="button" value="        Back      " onClick="window.location.href='../home'">&nbsp; </td>
   <td><input type = "submit" value= "Prev" onclick="return onPrev();" <%=isDisabled%> >&nbsp;

   <input type = "submit" value= "Next" onclick="return onNext();" <%=isDisabled%> >&nbsp;
           Page :<%=curPage%>
             <input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >&nbsp;&nbsp;
	    /&nbsp;&nbsp;
            <%=totalPages%>&nbsp;&nbsp;
   </td>
   </TR>
  
    </table>
</FORM> --%>
 </tbody> 
    
</table>
</div>

<input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >

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

             <!-- Below Jquery Script used for Show/Hide Function--> 
 <script>
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
 });
 </script>
	  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>	  