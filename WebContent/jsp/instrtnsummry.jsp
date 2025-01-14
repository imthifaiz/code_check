<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>


<%@page import="com.track.constants.IConstants"%><html>
<head>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

function onGo(){
  document.form.submit();
}
</script>
<title>Instructions</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	
	List insQryList  = new ArrayList();
	String fieldDesc="";
	String PLANT="",step1 ="",step2="",step3="",step4="",step5="",productid="",pagename="";
	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	String qty="",scanqty="",TotalQty="";
	
	if(session.getAttribute("PLANT")!=null)
		PLANT=(String) session.getAttribute("PLANT");
	else
		PLANT = request.getParameter("PLANT");
		
	productid = StrUtils.fString(request.getParameter("PRODUCTID"));
	pagename = StrUtils.fString(request.getParameter("PAGENAME"));
	InstructionUtil _insutil = new InstructionUtil();
	//_insutil.setmLogger(mLogger);
	int index=1;
	 try{
	      Hashtable ht = new Hashtable();
	      if(strUtils.fString(PLANT).length() > 0)
	      ht.put(IConstants.PLANT,PLANT);
	      insQryList= _insutil.listInstrns("step1,step2,step3,step4,step5",ht,"");
	      if(insQryList.size()>0)
	      {	       
	      for(int i=0;i<insQryList.size();i++)
	      {
	    	  Map linemap = (Map)insQryList.get(i);
	    	  step1 =(String)linemap.get("step1");
	    	  step2 =(String)linemap.get("step2");
	    	  step3 =(String)linemap.get("step3");
	    	  step4 =(String)linemap.get("step4");
	    	  step5 =(String)linemap.get("step5");
	      }
	      }
	 }catch(Exception e) { }

%>

<FORM name="form" method="post" action="">
  <br>
 <input type="hidden" name="PRODUCTID" value="<%=productid%>" >
 <input type="hidden" name="PAGENAME" value="<%=pagename%>" >
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#669900" COLSPAN="11"><font color="white" size="13">Instructions</font></TH>
    </TR>
  </TABLE>
    <br>
  <center>
   <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" >
      <font class="mainred"> <%=fieldDesc%></font>
   </table>
   </center>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#ffffff">
  <tr>
         <TH ALIGN="right" width="25%">&nbsp;</TH>
          <TD ALIGN="left" >&nbsp; </TD>
       
  </TR>
          <%if(step1.length()>0){ %>
   <TR >
         <TH ALIGN="right"  width="25%"  valign="top"><font size="8"></font></TH>
         <TD  ALIGN="left" valign="top"><p><font size="8"> <%=step1%> </font></p>  </TD>
          
   </tr> <%}%>
   <%if(step2.length()>0){ %>
          <tr>
          <TH ALIGN="right" width="25%" valign="top"><font size="8"></font></TH>
          <TD ALIGN="left" valign="top"><p><font size="8">  <%=step2%> </font></p> </TD>
          </TR>  <%}%>
        
         <%if(step3.length()>0){ %> 
	 <TR>
         <TH ALIGN="right" width="25%" valign="top"><font size="8"></font></TH>
         <TD  ALIGN="left" valign="top"><p><font size="8"> <%=step3%> </font></p></TD>
         </tr><%} %> 
         <%if(step4.length()>0){ %> 
         <tr>
                 <TH  ALIGN="right" width="25%" valign="top"> <font size="8"></font></TH>
	         <TD  ALIGN="left" valign="top"><p><font size="8"> <%=step4%> </font></p></TD>
          </TR> <%} %> 
          
          <%if(step5.length()>0){ %> 
            <tr>            
           <TH ALIGN="right" width="25%" valign="top"><font size="8"></font></TH>
           <TD  ALIGN="left" valign="top"><p><font size="8"> <%=step5%> </font></p> </TD>
          </TR>
          <%} %>
          <tr>
           <TH ALIGN="right" width="25%"></TH>
           <TD  ALIGN="left" > &nbsp;</TD>
          </TR>
           <tr>
          
	  <TD width="25%" ALIGN="left" ><input type="button" name="Back" class="mobileButton"  value="Back" onclick="onPrevious();"></TD>
         <TD ALIGN="left" > &nbsp;</TD>
       
          </TR>
            <tr>
          <TH ALIGN="right" width="25%"></TH>
          <TD ALIGN="left" > &nbsp;</TD>
         
          </TR>
  </TABLE>
  <br>
 
  </FORM>
  <script>
function onPrevious(){
	 var plant ="<%=PLANT%>";
	 var pagename= document.form.PAGENAME.value;
	 var productid= document.form.PRODUCTID.value;
	
	if(pagename=='mobileshopping'){
	 window.history.back();   	
	}
	else if(pagename=='mobileenquiry'){
		window.history.back(); 	
	}
	else if(pagename=='shopimage'){
		document.form.action  ="catalogImage.jsp?PAGE=EDIT_LOG&PLANT="+plant+"&PRODUCTID="+productid;
   	    document.form.submit();		
	}
	else if(pagename=='enquiryimage'){
		document.form.action  ="catalogEnqImage.jsp?PAGE=EDIT_LOG&PLANT="+plant+"&PRODUCTID="+productid;
   	    document.form.submit();		
	}
	else if(pagename=='registerimage'){
		document.form.action  ="catalogRegisterImage.jsp?PAGE=EDIT_LOG&PLANT="+plant+"&PRODUCTID="+productid;
   	    document.form.submit();		
	}
	else if(pagename=='browseimage'){
		document.form.action  ="catalogBrowseImage.jsp?PAGE=EDIT_LOG&PLANT="+plant+"&PRODUCTID="+productid;
   	    document.form.submit();		
	}
	}

</script>
</html>