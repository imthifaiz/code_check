<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript"  src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>

<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

</script>
<script language="javascript">

function validateForm(){
 
  if(document.form.ITEM.value == "")
  {
    alert("Please select an Product");
    document.form.ITEM.focus();
    return false;
  }
   else if(isNaN(document.form.QTY.value)) {alert("Please enter valid Quantity.");document.form.QTY.focus(); return false;}
 
  else if(document.form.QTY.value == ""){
    alert("Please enter Quantity");
    document.form.QTY.focus();
    return false;
  }
  if(!IsNumeric(form.QTY.value))
   {
     alert(" Please Enter valid  Qty !");
     form.QTY.focus();  form.QTY.select(); return false;
   }   
 
  else if(document.form.UNITPRICE.value == ""){
    alert("Please enter Unit Price");
    document.form.UNITPRICE.focus();
    return false;
  }
  //else if(!IsNumeric(removeCommas(form.UNITPRICE.value)))
  //{
    //alert(" Please Enter valid Unit Cost!");
   // form.UNITPRICE.focus();  form.UNITPRICE.select(); return false;
  //}
  else if(isNaN(removeCommas(document.form.UNITPRICE.value)))
  {
	  alert("Please Enter valid Unit Cost");
	    document.form.UNITPRICE.focus();
	    return false;
    
  }
  else{
   document.form.Submit.value="Add Product";
   }
}

</script>



<title>Mobile Order- Add Products</title>
<link rel="stylesheet" href="css/style.css">

<%@ page language="java"
	import="java.util.*,java.sql.*,java.io.*,java.net.*"%>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="tpdb"  class="com.track.tables.DODET" />
<jsp:useBean id="tphb"  class="com.track.tables.DOHDR" />
<jsp:useBean id="imb"  class="com.track.tables.ITEMMST" />
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>

<%


	session= request.getSession();
	String plant=su.fString((String)session.getAttribute("PLANT"));
    String action  = su.fString(request.getParameter("Submit")).trim();
    String dono    = su.fString(request.getParameter("DONO"));
    String jobNum  = su.fString(request.getParameter("JOB_NUM"));
    String custName  =  su.replaceCharacters2Recv(su.fString(request.getParameter("CUST_NAME")));
    String custCode  =su.fString(request.getParameter("CUST_CODE"));
    String deldate = su.fString(request.getParameter("DELDATE"));
   
	
	
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    String RFLAG=     su.fString(request.getParameter("RFLAG"));
  

  

%>
<!--<FORM name="form1" method="post" action="POSbmt.jsp" onSubmit="return validateReceiving(document.form1)">-->
<body>
<FORM name="form" method="post" action="/track/deleveryorderservlet?" onSubmit="return validateForm()">
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">
            Add Products
          </font>  </table>
  <br>
  <table  width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <font face="Times New Roman" size="2">
        <center>
          <table border="0" width="90%">
            <tr>
              <td width="100%">
                <CENTER>
                  <TABLE  CELLSPACING=0 WIDTH="100%">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT"> Order No:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="40%" ALIGN = "LEFT"><INPUT type = "TEXT" class="inactivegry" size="20"  MAXLENGTH=20 name="DONO"   value="<%=dono%>" READONLY>
                     </Tr>
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Customer&nbsp;Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="40%" ALIGN = "LEFT"><INPUT type = "TEXT"  class="inactivegry" size="20"  MAXLENGTH=20 name="CUST_NAME"  value="<%=su.forHTMLTag(custName)%>" READONLY>
                      <INPUT type = "hidden"  class="inactive" size="20"  MAXLENGTH=20 name="CUST_CODE"  value="<%=custCode%>" READONLY>
                    </Tr>
                   
                   <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > Order Line No:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT"  class="inactivegry" size="20"  MAXLENGTH=6 name="DOLNNO" value="<%=tpdb.getNextLineNo(dono,plant)%>" READONLY>
                    </Tr>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Product ID:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  MAXLENGTH=50 name="ITEM" ><a href="#" onClick="javascript:popUpWin('item_list_do.jsp?ITEM='+form.ITEM.value);"><img src="images/populate.gif" border="0"></a>
                    </TR>
                    
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Description:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT  type = "TEXT" size="40"  MAXLENGTH=100 name="DESC" ><a href="#" onClick="javascript:popUpWin('item_list_do.jsp?ITEM='+form.ITEM.value+'&DESC='+form.DESC.value);"><img src="images/populate.gif" border="0"></a>
                      
                 <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK1">
                  <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK2">
                   <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK3">
                    <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="ITEM_CONDITION">
                  </Tr>
                   <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >UOM:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="UOM">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT  type = "hidden" size="20"  MAXLENGTH=20 name="QTY" value="1" readonly>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <INPUT  type = "hidden" size="10"  MAXLENGTH=20 name="UNITPRICE">

                 </TR>
                  </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
        </center>
       
        <INPUT type = "Hidden"     name="JOB_NUM"      value="<%=jobNum%>" >
        <INPUT type = "Hidden"     name="ENCRYPT_FLAG" value="1">
        <INPUT type = "Hidden"     name="VENDNAME"     value="<%=su.forHTMLTag(custName)%>">
        <INPUT type = "Hidden"     name="LOGIN_USER"  value="<%=enrolledBy%>">
        <INPUT type=  "Hidden"     name="RFLAG" value="<%=RFLAG%>">

        <div align="center"><center>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  <input type="button"  value="Back" onClick="window.location.href='/track/deleveryorderservlet?DONO=<%=dono%>&RFLAG=<%=RFLAG%>&Submit=View';">
                  &nbsp;
                 <input type="Submit" value="Add Product & Save" name="Submit">
                </td>
              </tr>
            </table>
          </center>
        </div>
        <div align="center">
          <center>
            <p>&nbsp;</p>
          </center>
        </div>
        </font></td>
    </tr>
  </table>
</FORM>
</body>
</html>
<Script>
function validateProduct() {
	var productId = document.form.ITEM.value;
        var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=plant%>",
                           
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						document.form.DESC.value = resultVal.sItemDesc;
                                                 document.form.UOM.value = resultVal.sUOM;
                                                 document.form.UNITPRICE.value = resultVal.price;
						
					} 
				}
			});
		
	}
        
        </Script>





