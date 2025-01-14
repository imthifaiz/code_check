<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>

<html>
<head>
<title>Setting</title>
<link rel="stylesheet" href="../css/style.css">
<script src="js/jquery.min.js"></script>
<script src="js/JsBarcode.all.js"></script>
<script src="js/jspdf.js"></script>
<script language="javascript">



function onGenerateReport()
{
  //window.focus();
//   document.form1.chkRefresh.value="2";
//   document.form1.action ="/track/LabelPrintServlet?action=ViewReport";
//   document.form1.submit();
//   setTimeout(window.close,6000);
  
  var urlStr = "/track/LabelPrintServlet?action=GETLABELPRINTDETAILS";
	
	$.ajax({type: "GET",
		url: urlStr,
		//data:{ACTION:"GETLABELPRINTDETAILS"},
		dataType: "json",
		success: onGetLabelPrintDetails
		});
  
 }
 
function onGetLabelPrintDetails(data){
	
// 	var labeType=document.getElementById("LABELTYPE").value;
	var barCodeWidth=document.getElementById("BARCODEWIDTH").value;
	
	var ptype = document.getElementById("PRINTTYPE").value;
	var fontsize = document.getElementById("TEXTFONTSIZE").value;
	
if(ptype=="1"){
	
	window.opener.withoutBatch100x50(data,barCodeWidth,fontsize);
}
	
else if(ptype=="2"){
	window.opener.withBatch100x50(data,barCodeWidth,fontsize);
}
else if(ptype=="8"){
	window.opener.labelManual100x50(data,barCodeWidth,fontsize);
}

// document.form.PGaction.value="UpdatePrintStatus";

window.close();
}
 
function onChange()
{
  document.form1.chkRefresh.value="1";
  document.form1.action ="/track/LabelPrintServlet?action=ChangeLabelSetting"; 
  document.form1.submit();
  
 }
 
 /*
    function detectRefresh
    1. If parent window exist the return will be 'undefined'
    2. If parent window does not exist, an exception will be raised which we will catch
    
  */
 function detectRefresh(){
   if(document.form1.chkRefresh.value=="2")
	{
     try
        
     {
       if(window.opener.title == undefined){
         isRefresh = true;
        window.close();
      }
   
     }catch(err)
     {
         isRefresh = false;
      }  
     }
 }
 

</script>

</head>
<link rel="stylesheet" href="css/style.css">
<body bgcolor="#ffffff"   >
<img id="productBarCode"/ hidden>
<img id="batchBarCode"/ hidden>
<form method="post" name="form1" >
<%
	  HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	  loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	  loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
	  (String) session.getAttribute("LOGIN_USER")).trim());
	  MLogger mLogger = new MLogger();
	  mLogger.setLoggerConstans(loggerDetailsHasMap);
	  LabelPrintUtil _LabelPrintUtil = new LabelPrintUtil();
	  _LabelPrintUtil.setmLogger(mLogger);
	  StrUtils strUtils = new StrUtils();
	  String sLabelType ="",sBarcodeFontSize="",sTextFontSize="", fieldDesc="",errorDesc="",PGaction="";
	  String PLANT=(String)session.getAttribute("PLANT");
	  String LABELTYPE 			= strUtils.fString(request.getParameter("LABELTYPE"));
	  String BARCODEFONTSIZE    = strUtils.fString(request.getParameter("BARCODEFONTSIZE"));
	  String TEXTFONTSIZE       = strUtils.fString(request.getParameter("TEXTFONTSIZE"));
	  String RENO = strUtils.fString(request.getParameter("REFNO"));
      String PRINTTYPE = strUtils.fString(request.getParameter("PRINTTYPE")); 
      PGaction  = strUtils.fString(request.getParameter("PGaction")).trim();
      fieldDesc=(String)request.getSession().getAttribute("RESULT");
      request.getSession().setAttribute("ISPOPUP","CLOSED");
      fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
      errorDesc="";
      errorDesc=(String)request.getSession().getAttribute("RESULTERROR");
      if(errorDesc.length() != 0 && !errorDesc.equals(null))
  	   {
      	    fieldDesc = "<font class='mainred'>" + errorDesc + "</font>";
    		
  	   }
      //Batch variable
        String s100mmX50mm_With_Batch="100mm X 50mm With Batch";
        String s100mmX50mm_No_Batch="100mm X 50mm No Batch";
        String s100mmX35mm_With_Batch="100mm X 35mm With Batch";  
        String s100mmX35mm_No_Batch="100mm X 35mm No Batch";  
        String s50mmX25mm_With_Batch="50mm X 25mm With Batch";
        String s50mmX25mm_No_Batch="50mm X 25mm No Batch";
        String s35mmX15mm_No_Batch="35mm X 15mm No Batch";
  
     //Batch variable end
	  Hashtable ht=new Hashtable();
	  String extCondData="";
	  session = request.getSession();
	  ht.put("PLANT",PLANT);
	  ht.put("PRINTTYPE",PRINTTYPE);
	  if(errorDesc.length() != 0 && !errorDesc.equals(null))
	  {
		  sLabelType       = LABELTYPE;
	      sBarcodeFontSize = BARCODEFONTSIZE;
	      sTextFontSize    = TEXTFONTSIZE;
	  }
	  else
	  {
	 	 ArrayList listQry = _LabelPrintUtil.getLabelSetting("labeltype,barcodefontsize,textfontsize",ht,extCondData);
	     for(int i =0; i<listQry.size(); i++) {
	         Map m=(Map)listQry.get(i);
	         sLabelType     = (String)m.get("labeltype");
	         sBarcodeFontSize    = (String)m.get("barcodefontsize");
	         sTextFontSize    = (String)m.get("textfontsize");
	     }
	  }
	  request.getSession().setAttribute("RESULT","");
	   request.getSession().setAttribute("RESULTERROR","");
	  /*if(PRINTTYPE.equals("8"))
	   {
		   try{
		   boolean flag=false;
		   ItemMstDAO _ItemMstDAO=new ItemMstDAO() ;
		  flag= _ItemMstDAO.removeLabelDetailsTemp(PLANT,"");
		   if(!flag)
			{
				throw new Exception();
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
	   }*/
	   
	   if(PGaction.equalsIgnoreCase("UpdatePrintStatus")){
		  	try{
		  		boolean flag=false;
		  		LabelPrintDAO _LabelPrintDAO=new LabelPrintDAO() ;
		  		String strStatus="C";
			 	StringBuffer sql1 = new StringBuffer(" SET ");
			 	sql1.append("STATUS  = '" + strStatus + "' ");
			 	Hashtable htUpdate= new Hashtable();
			 	htUpdate.clear();
			 	htUpdate.put(IDBConstants.PLANT,PLANT); 
			 	htUpdate.put("REFNO", RENO);
			 	flag = _LabelPrintDAO.updateLabelType(sql1.toString(), htUpdate, "");
			 	if(!flag)
				{
					throw new Exception();
				}
		     
		 }catch(Exception e) { 
			  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
			  if(e.getMessage()==null)
			  {
				  fieldDesc="";
			  }
		 }
		 	
		}
	        
%>
<CENTER>
<table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
   		<%=fieldDesc%>
 </table>
  <br></br>
 <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
   	<TR>
	<TH ALIGN="left" width="40%"><font color="blue" size="2">Note: Suggested Barcode Width is 20 and Text Font Size is 12</font></TH>
	</TR>
 </table>

<table border="0" width="100%" cellspacing="1" cellpadding="0"	bgcolor="#dddddd">

	<TR width="55%" hidden> 
	        <TD WIDTH="30%">           
	 		<TH ALIGN="Right" width="15%">&nbsp;Label Type : </TH>
			<TD>
				<SELECT NAME="LABELTYPE" size="1" maxlength="9">
					<OPTION value="<%=s50mmX25mm_With_Batch%>"
						<%if(s100mmX50mm_With_Batch.equalsIgnoreCase(sLabelType)){%> selected <%}%>><%=s50mmX25mm_With_Batch%>
					</OPTION>
					<OPTION value="<%=s50mmX25mm_No_Batch%>"
						<%if(s100mmX50mm_No_Batch.equalsIgnoreCase(sLabelType)){%> selected <%}%>><%=s50mmX25mm_No_Batch%>
					</OPTION>
					<OPTION value="<%=s100mmX50mm_With_Batch%>"
						<%if(s100mmX50mm_With_Batch.equalsIgnoreCase(sLabelType)){%> selected <%}%>><%=s100mmX50mm_With_Batch%>
					</OPTION>
					<OPTION value="<%=s100mmX50mm_No_Batch%>"
						<%if(s100mmX50mm_No_Batch.equalsIgnoreCase(sLabelType)){%> selected <%}%>><%=s100mmX50mm_No_Batch%>
					</OPTION>
					<OPTION value="<%=s100mmX35mm_With_Batch%>"
						<%if(s100mmX35mm_With_Batch.equalsIgnoreCase(sLabelType)){%> selected <%}%>><%=s100mmX35mm_With_Batch%>
					</OPTION>
					<OPTION value="<%=s100mmX35mm_No_Batch%>"
						<%if(s100mmX35mm_No_Batch.equalsIgnoreCase(sLabelType)){%> selected <%}%>><%=s100mmX35mm_No_Batch%>
					</OPTION>
					<OPTION value="<%=s35mmX15mm_No_Batch%>"
						<%if(s35mmX15mm_No_Batch.equalsIgnoreCase(sLabelType)){%> selected <%}%>><%=s35mmX15mm_No_Batch%>
					</OPTION>
					
				</SELECT>
          	</TD>
          </TR>
          
          <TR width="55%"> 
          <TD WIDTH="30%">                   
	 		<TH WIDTH="15%" ALIGN="center">&nbsp;Barcode Width&nbsp;:</TH>
			<TD><SELECT NAME="BARCODEFONTSIZE" id="BARCODEWIDTH" size="1" maxlength="9">
			<% for (int i =1; i<=30; i+=1){ %>
			<OPTION value="<%=i%>"
				<%if((Integer.toString(i)).equalsIgnoreCase(sBarcodeFontSize)){%> selected <%}%>><%=i%>
			</OPTION>
			<%}%>
		</SELECT>
         </TD>
         </TR>
         
         <TR width="55%"> 
          <TD WIDTH="30%">                   
	 		<TH WIDTH="15%" ALIGN="center">&nbsp;Text Font Size&nbsp;:</TH>
			<TD><SELECT NAME="TEXTFONTSIZE" id="TEXTFONTSIZE" size="1" maxlength="9">
			<% for (float j =5; j<=15; j+=0.5){%>
			<OPTION value="<%=j%>"
				<%if((Float.toString(j)).equalsIgnoreCase(sTextFontSize)){%> selected <%}%>><%=j%>
			</OPTION>
			<%}%>
		</SELECT>
         </TD>
        </TR>
</CENTER>
   </table>
   <br>
   <table align="center" >
    <TR>
    			<input type="hidden" name = "chkRefresh" id = "chkRefresh"  value = "1" ></input>
    			<input type="hidden" name = "PRINTTYPE" id = "PRINTTYPE"  value = "<%=PRINTTYPE%>" ></input>
        		<TD ><input type="button" value="Label Generate"  onClick="onGenerateReport();">&nbsp;</TD>
				 <TD><input type="button" value="Change" onClick=" onChange();">&nbsp;</TD>
				<TD><a href="#"	onclick="window.close();"><input type="submit" value="Close"></a></TD>
		   </TR>
  </table>
  <input type="hidden" name="PGaction" value="">
</body>
</html>





