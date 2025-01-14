<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>

<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%>
<%@page import="com.track.constants.IDBConstants"%>
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script src="js/JsBarcode.all.js"></script>
<script src="js/jspdf.js"></script>
<script language="javascript">

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Label Setting', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function checkAll(isChk)
{
	var len = document.form.chkdLnNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form .chkdLnNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkdLnNo.checked = isChk;
               	}
              	else{
              		document.form.chkdLnNo[i].checked = isChk;
              	}
            	
        }
    }
}

function onPrint()
{
	 var checkFound = false;  
	 var orderLNo;
	 var len = document.form.chkdLnNo.length; 
	 if(len == undefined) len = 1;
	for (var i = 0; i < len ; i++)
    {
    	if(len ==1 && document.form.chkdLnNo.checked)  
    	{
    		chkstring = document.form.chkdLnNo.value;
     	}
    	else
    	{
    		chkstring = document.form.chkdLnNo[i].value;
     	}
    	chkdvalue = chkstring.split(',');
		if(len == 1 && (!document.form.chkdLnNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdLnNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
	    	 
	    }
	
	     else {
		     if(document.form.chkdLnNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
		    	 
		    }
		   
	     }
              	     
    }
	
    if (checkFound!=true) {
    	alert("Please check at least one checkbox.");
		return false;
	}
    
   //document.form.action ="/track/LabelPrintServlet?action=PrintProduct";
   //document.form.submit();
   
   var formData = $('form#LabelPrintEmployee').serialize();
	$.ajax({
		
      type: 'post',
      url: '/track/LabelPrintServlet?action=PrintEmployee', 
      dataType:'html',
      data:  formData,
      async:    false,
      
     
      success: function (data) {
      	$('#labelSettingPopUpContent').html(data);
      	
	         
      },
      error: function (data) {	        	
          alert(data.responseText);
      }
  });
 
 }


function isNumericInput(strString) {
	var strValidChars = "0123456789";
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

 
function onGo(){
   var flag    = "false";
    var DIRTYPE        = document.form.DIRTYPE.value;
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   if(flag == "false"){ alert("Please select the Dirtype"); return false;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
  document.form .action="LabelPrintEmployee.jsp";
  document.form .submit();
}




</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Label Print (Employee)</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils _strUtils     = new StrUtils();
	Generator generator   = new Generator();
	HTReportUtil movHisUtil       = new HTReportUtil();
	//movHisUtil.setmLogger(mLogger);
	DateUtils _dateUtils = new DateUtils();
	List QryList  = new ArrayList();
	ItemUtil itemUtil = new ItemUtil();
	String  fieldDesc="",errorDesc="",chkString ="",MODULETYPE ="";
	session= request.getSession();
	String plant 	= (String)session.getAttribute("PLANT");
    String userID 	= (String)session.getAttribute("LOGIN_USER");
	String DIRTYPE ="",USER="",ITEM="",ISPOPUP="",
	ITEMDESC="",TRANSACTIONDATE,strTransactionDate,PGaction="";
	PGaction        = _strUtils.fString(request.getParameter("PGaction")).trim();
	String html 	= "",cntRec ="false",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="";
	String PLANT 	= "",CUST_NAME= "",empno="",firstname="",lastname="",PrintStatus="",telno="";
	String action   = su.fString(request.getParameter("action")).trim();
	String REFNO="",PRINTTYPE="",ACTIVE="",PRINTSTATUS="",RECS_PER_PAGE="";

	String value=null,allChecked = "";
	String bgcolor="";
	int iColor=0;
	
	double iordertotal=0;
	double ipicktotal=0;
	double iissuetotal=0;
	double ireversetotal=0;
	int k=0;
	long listRec=0;
	
	PLANT= session.getAttribute("PLANT").toString();
	DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
	CUST_NAME     = _strUtils.fString(request.getParameter("CUST_NAME"));
	ACTIVE=_strUtils.fString(request.getParameter("ACTIVE"));
	//PRINTSTATUS=_strUtils.fString(request.getParameter("PRINTSTATUS"));
	//RECS_PER_PAGE = _strUtils.fString(request.getParameter("RECS_PER_PAGE"));
	String listRecSize =_strUtils.fString(request.getParameter("listRecSize")); 
		
	
	
	/*if(listRecSize.length()==0){
		listRecSize ="0";
		}
	  int curPage      = 1;
	 if(RECS_PER_PAGE.length()==0) RECS_PER_PAGE = "100";
	    int recPerPage   = Integer.parseInt(RECS_PER_PAGE);
	    long totalRec     = 0;
	    String currentPage        = _strUtils.fString(request.getParameter("cur_page"));
	    String isDisabled ="disabled";
	    
	if(PRINTSTATUS.equals(""))
    {
		PRINTSTATUS="N";
	}*/
	//ISPOPUP=_strUtils.fString(request.getParameter("ISPOPUP"));
	
	if(DIRTYPE.length()<=0){
		DIRTYPE = "LABEL PRINT LOCATION";
	}
//	ItemMstDAO itemdao = new ItemMstDAO();
	//itemdao.setmLogger(mLogger);
	if(PGaction.equalsIgnoreCase("View")){
  	try{
  		String extcond="";
  		/*if(PRINTSTATUS.equals("N")){
				extcond=extcond +" AND ISNULL(PRINTSTATUS,'N') = 'N'";
		}
		else if(PRINTSTATUS.equals("C")){
			extcond=extcond+" AND ISNULL(PRINTSTATUS,'N') = 'C'";
		}*/
			
		//currentPage="1";
		//ItemSesBeanDAO _ItemSesBeanDAO=new ItemSesBeanDAO();
        //listRec=_ItemSesBeanDAO.getProductCount(ITEM,ITEMDESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,plant,"");
       // int start =  Integer.parseInt(currentPage);
        //int end = Integer.parseInt(currentPage)*recPerPage;
        //locQryList= itemUtil.queryItemMstForSearchCriteriaNew(ITEM,ITEMDESC ,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,plant,extcond,start,end);
       EmployeeUtil custUtil = new EmployeeUtil();
         Hashtable ht = new Hashtable();
         if(_strUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
         QryList= custUtil.getEmployeeListStartsWithName(CUST_NAME,PLANT);
         if(QryList.size()== 0)
         {
           fieldDesc="Data Not Found";
         }
        REFNO=(String)request.getSession().getAttribute("refNo");
        ISPOPUP=(String)request.getSession().getAttribute("ISPOPUP");
       	errorDesc=(String)request.getSession().getAttribute("RESULTERROR");
       	
        if(QryList.size()== 0)
	      {
	        fieldDesc="Data Not Found";
	      }
 		 /*if (listRec >0 )  {         totalRec = listRec; listRecSize=new Long(totalRec).toString();
             isDisabled="";
       }
     /*  if (currentPage.length() > 0)
       {
       try  {   curPage = (new Integer(currentPage)).intValue(); 
        System.out.println("curPage :: "+curPage); 
       }
         catch (Exception e)   {   curPage = 1;                                      }
     }*/
       
       
    	if(errorDesc.length() != 0 && !errorDesc.equals(null))
    	{
        	fieldDesc = "<font class='mainred'>" + errorDesc + "</font>";
      		allChecked = su.fString(request.getParameter("allChecked"));
    	}
    	else
    	{    
    		System.out.println("ISPOPUP"+ISPOPUP);
    		if(ISPOPUP.equals("OPENED")){
    		if(REFNO.length()>0  )
    		{
    			
    			/* PrintWriter outWriter = response.getWriter();
    			outWriter.println("<script type=\"text/javascript\">");
    			outWriter.println("window.open('LabelSettings.jsp?PRINTTYPE=1','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')");
    		    outWriter.println("</script>"); */
    		  }
    		}
    	
    	}
  request.getSession().setAttribute("RESULT","");
  request.getSession().setAttribute("RESULTERROR","");
     
 }catch(Exception e) { 
	  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
	  if(e.getMessage()==null)
	  {
		  fieldDesc="";
	  }
 }
 	
}
	
/*	if(PGaction.equalsIgnoreCase("PREVORNEXT")){
		 try{

		int start = (new Integer(currentPage)-1)*recPerPage;
		int end = (new Integer(currentPage)*recPerPage)+1;
		   Hashtable ht = new Hashtable();
			      if(_strUtils.fString(PLANT).length() > 0)
			      ht.put("PLANT",PLANT);
			      locQryList= itemUtil.queryItemMstForSearchCriteriaNew(ITEM,ITEMDESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,plant,"",start,end);
		     if (locQryList != null)  {         totalRec = locQryList.size();
		     isDisabled="";
		     }
		     if (currentPage.length() > 0)
		     {
		     
		    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
		    catch (Exception e)   {   curPage = 1;                                      }
		    }
		     
		   
		   
		 }catch(Exception e) {System.out.println("Exception :getStockTakeList"+e.toString()); }
		}

		 int totalPages = (Integer.parseInt(listRecSize) + recPerPage -1)/recPerPage;
		    if (curPage > Integer.parseInt(listRecSize)) curPage = 1;                    // Out of range

		    System.out.println("totalPages :: "+totalPages);
		    System.out.println("curPage :: "+curPage);*/

%>

<%@ include file="body.jsp"%>
<img id="productBarCode"/ hidden>
<img id="batchBarCode"/ hidden>
<FORM name="form" method="post" id="LabelPrintEmployee" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
 <br>
 
 <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Label Print(Employee)</font></TH>
    </TR>
  </TABLE>
  <br>
  <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
   <div id="labelSettingPopUpContent" class='mainred'></div>
 </table>
  <TABLE border="0" width="70%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  
   <TR align="Center">
    
          <TH ALIGN="right" width="15%">Employee Name or ID:</TH>
         <TD width="25%" align="center"><INPUT name="CUST_NAME" type = "TEXT" value="<%=CUST_NAME%>" size="20"  MAXLENGTH=20>
          <a href="#" onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.CUST_NAME.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
                    
          <TD ALIGN="center" width="20%"> 
          <input type="button" value="View"   align="left" onClick="javascript:return onGo();"></TD>
        </TR>
  </TABLE>
	<br>
	

     
     
<table BORDER = "1" WIDTH = "90%" align="center" bgcolor="#dddddd" >
		<tr>
		<td width = "15%">  
			<INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
        	&nbsp; Select/Unselect All 
         </td>
		<tr>
</table>
  <TABLE WIDTH="92%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
 
    <TR BGCOLOR="#000066">
        <!--  <TH><font color="#ffffff" align="center">S/N</TH> -->
         <TH><font color="#ffffff" align="left"><b>Chk</TH>
            <TH><font color="#ffffff" align="left"><b>Employee Code</TH>
          <TH><font color="#ffffff" align="left"><b>First Name</TH>
          <TH><font color="#ffffff" align="left"><b>Last Name</TH>
            <TH><font color="#ffffff" align="left"><b>Telephone</TH>
	    
         </tr>
       <%
	      if(QryList.size()<=0 ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

			<%	
			 for (int iCnt =0; iCnt<QryList.size(); iCnt++){
				    int iIndex = iCnt + 1;
	         		 bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	           		 Map lineArr = (Map) QryList.get(iCnt);
	     			 empno= (String)lineArr.get("EMPNO");
				     firstname= (String)lineArr.get("FNAME");
				     lastname= (String)lineArr.get("LNAME");  
				     telno=(String)lineArr.get("TELNO");  
		            chkString  =empno+",,,"+StrUtils.replaceCharacters2Send(firstname);
		             String lineno=String.valueOf(iIndex)+"_"+empno;
		        
		       
        	%>
        	
        	       
	       

            <TR bgcolor = "<%=bgcolor%>">
            
            <TD width="5%" align="CENTER"><font color="black"><INPUT Type=checkbox style="border: 0;" name="chkdLnNo" 	value="<%=chkString%>" ></font></TD>
            				
		
			<TD align="center" width="13%"><%=empno%></TD>
			<input type="hidden" name = "EMPNO_<%=lineno%>" id = "EMPNO_<%=lineno%>"   value = <%=empno%> ></input>
			 
			 
			<TD align="center" width="15%"><%=firstname%></TD>
            <input type="hidden" name = "FIRSTNAME_<%=lineno%>" id = "FIRSTNAME_<%=lineno%>"  value = <%=firstname%> ></input>
			
			
			
			<TD align="center" width="10%"><%=lastname%></TD>
			<input type="hidden" name = "LASTNAME_<%=lineno%>" id = "LASTNAME_<%=lineno%>"  value = <%=lastname%> ></input>
			
			<TD align="center" width="10%"><%=lastname%></TD>
			<input type="hidden" name = "LASTNAME_<%=lineno%>" id = "LASTNAME_<%=lineno%>"  value = <%=lastname%> ></input>
			
			
			
			<!-- <TD align="center" width="9%"><%=PrintStatus%></TD>
			<input type="hidden" name = "PrintStatus_<%=lineno%>" id = "PrintStatus_<%=lineno%>"  value = <%=PrintStatus%> ></input>
			-->             
          </TR>
                    
       <%
      // iColor=iColor+1;
     }
		   
     	/*if(k==0)
		//	k=1;
     	// if( locQryList.size()<=0){ cntRec ="true";
	  }*/
	  %>
		  
	
    </TABLE>
      <br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value="Back" onClick="window.location.href='LabelPrintMenu.jsp'">&nbsp; </td>
   <td>   <input type="button" value="Generate Label" onClick="onPrint();" > </td>
   
    
            
           
   <INPUT type="Hidden" name="DIRTYPE" value="EMPLOYEE">
   <INPUT type="Hidden" name="REFNO" value=<%=REFNO%>>

    <INPUT type="Hidden" name="ACTIVE" value=<%=ACTIVE%>>
    <input type="hidden" name="visited" value="" />
      
   <input type="hidden" name="CUST_CODE" value="">
<input type="hidden" name="L_CUST_NAME" value="">
 
  </TR>
    </table>
  </FORM>
<SCRIPT>



    function withoutBatch50x25(data,barcodeWidth,fontsize){
    	var imgData=[];
    	var productID=[];	
    	var Description=[];
		var dataLength=data.items.length;
			$.each(data.items, function(i,prodItem){
			// and the formula is:
			//random = random+(Math.floor(Math.random() * (max - min + 1)) + min);
				
				var item = prodItem['ITEM'];
				var itemDesc = prodItem['ITEMDESC'];
			productID.push(item);
			Description.push(itemDesc);
			JsBarcode("#productBarCode",
					item,
					{displayValue: false ,
						orientation: 'landscape',
						format:'CODE128',
						lineColor: "black",
						textPosition: "bottom",
						width:barcodeWidth,
						height:150,
						text:item,
						textAlign:"center",
						textMargin:2,
						fontSize:10,
						background:"",
						lineColor:"",
						marginLeft:1,
						marginRight:0,				
						marginTop:10,
						marginBottom:10,
			});
			imgData.push($("#productBarCode").attr("src"));
			});

				var doc;
				doc = new jsPDF('l', 'mm', [50, 25]);
				//var widths = doc.internal.pageSize.width;    
				//var heights = doc.internal.pageSize.height;
				
				doc.setFont("Arial Narrow","bold");
				doc.setFontSize(fontsize);
	
				
			//	doc.text(0, 60, "_________________________________________________________________________________________________________________________________________________________________________");
				doc.text(2, 4, "Product ID: "+productID[0]);
				doc.addImage(imgData[0], 'JPEG', 2,5,45,16);
				doc.text(2, 23,Description[0]);
					if(dataLength>=1){
				 for(var i=1;i<dataLength;i++){
				doc.addPage();
				doc.text(2, 4, "Product ID: "+productID[i]);
				 doc.addImage(imgData[i], 'JPEG', 2, 5,45,16);
				 doc.text(2, 23,Description[i]);
				 }
				 }
				data = [];
				doc.save("withoutBatch50x25.pdf");				
	

		}
    
    function withoutBatch100x50(data,barcodeWidth,fontsize){
    	var imgData=[];
    	var productID=[];	
    	var Description=[];
		var dataLength=data.items.length;
			$.each(data.items, function(i,prodItem){
			// and the formula is:
			//random = random+(Math.floor(Math.random() * (max - min + 1)) + min);
				
				var item = prodItem['ITEM'];
				var itemDesc = prodItem['ITEMDESC'];
			productID.push(item);
			Description.push(itemDesc);
			JsBarcode("#productBarCode",
					item,
					{displayValue: false ,
						orientation: 'landscape',
						format:'CODE128',
						lineColor: "black",
						textPosition: "bottom",
						width:barcodeWidth,
						height:150,
						text:item,
						textAlign:"center",
						textMargin:2,
						fontSize:10,
						background:"",
						lineColor:"",
						marginLeft:1,
						marginRight:0,				
						marginTop:10,
						marginBottom:10,
			});
			imgData.push($("#productBarCode").attr("src"));
			});

				var doc;
				doc = new jsPDF('l', 'mm', [100, 50]);
				//var widths = doc.internal.pageSize.width;    
				//var heights = doc.internal.pageSize.height;
				
				doc.setFont("Arial Narrow","bold");
				doc.setFontSize(fontsize);
	
				
			//	doc.text(0, 60, "_________________________________________________________________________________________________________________________________________________________________________");
				doc.text(11, 4, "Product ID: "+productID[0]);
				doc.addImage(imgData[0], 'JPEG', 11,3,85,45);
				doc.text(11, 49,Description[0]);
					if(dataLength>=1){
				 for(var i=1;i<dataLength;i++){
				doc.addPage();
				doc.text(11, 4, "Product ID: "+productID[i]);
				 doc.addImage(imgData[i], 'JPEG', 11, 3,85,45);
				 doc.text(11, 49,Description[i]);
				 }
				 }
				data = [];
				doc.save("withoutBatch100x50.pdf");				
	

		}
              
</SCRIPT>
<%@ include file="footer.jsp"%>
 
