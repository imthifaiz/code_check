<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.util.DateUtils"%>
<%@ page import="java.util.Hashtable"  %>
<%@ page language="java" import="java.util.*,java.sql.*,java.io.*,java.net.*"%>
	<%@ page import="java.util.*"%>
<title>Saving to the database</title>



<jsp:useBean id="misc"  class="com.track.gates.miscBean" />
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sb"  class="com.track.gates.sqlBean" />
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<jsp:useBean id="tl"  class="com.track.gates.TableList" />
<jsp:useBean id="df"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="pm"  class="com.track.tables.PLNTMST"/>
<jsp:useBean id="dhb"  class="com.track.tables.DOHDR"/>
<jsp:useBean id="ddb"  class="com.track.tables.DODET"/>
<jsp:useBean id="itb"  class="com.track.tables.ITEMMST"/>
<jsp:useBean id="inv"  class="com.track.tables.INVMST"/>
<jsp:useBean id="rcb"  class="com.track.tables.RECVHIS"/>
<jsp:useBean id="ica"  class="com.track.tables.ITEMCLATT"/>
<jsp:useBean id="mov"  class="com.track.db.util.MovHisUtil"/>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>

<%



HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);

sb.setmLogger(mLogger);
ub.setmLogger(mLogger);
df.setmLogger(mLogger);
pm.setmLogger(mLogger);
dhb.setmLogger(mLogger);
ddb.setmLogger(mLogger);
itb.setmLogger(mLogger);
inv.setmLogger(mLogger);
rcb.setmLogger(mLogger);
ica.setmLogger(mLogger);
mov.setmLogger(mLogger);
	session = request.getSession();
	String plant = (String)session.getAttribute("PLANT");
    String action  = request.getParameter("Submit").trim();
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    if (action.equalsIgnoreCase("Add")) {
      String dono    = su.fString(request.getParameter("DONO")).trim();
      String custno  = su.fString(request.getParameter("CUSTNO")).trim();
      String custname  = su.fString(request.getParameter("CUSTNAME")).trim();
      String orddate = su.fString(request.getParameter("ORDDATE")).trim();
      String origin  = su.fString(request.getParameter("USERFLD1")).trim();
      String dolnno  = su.fString(request.getParameter("DOLNNO")).trim();
      String item    = su.fString(request.getParameter("ITEM")).trim();
      String itemdesc    = su.fString(request.getParameter("DESC")).trim();
      String uom    = su.fString(request.getParameter("UOM")).trim();
      String dodate    = su.fString(request.getParameter("DODATE")).trim();

      if(item.length() <= 0 ){
        result = "<font color=\"red\"> Item Code is Empty <br><br><center>"+
          "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
          "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";
        result = "Sales Order...<br><h3>"+result;
        session.setAttribute("RESULT",result);
        response.sendRedirect("displayResult2User.jsp");
      }


      String qtyor   = su.fString(request.getParameter("QTYOR")).trim();

      if(qtyor.trim().length() <= 0 ){
        result = "<font color=\"red\"> Qty is Empty<br><br><center>"+
          "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
          "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";
        result = "Sales Order...<br><h3>"+result;
        session.setAttribute("RESULT",result);
        response.sendRedirect("displayResult2User.jsp");
     }
     try{
        if(Float.parseFloat(qtyor) <= 0 ){
          result = "<font color=\"red\"> Component Qty must be greater than zero <br><br><center>"+
          "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
          "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";
          result = "Sales Order...<br><h3>"+result;
          session.setAttribute("RESULT",result);
         response.sendRedirect("displayResult2User.jsp");
        }
     }
    catch(Exception e){
          result = "<font color=\"red\"> Invalid Qty entered <br><br><center>"+
          "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
          "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";
          result = "Sales Order...<br><h3>"+result;
          session.setAttribute("RESULT",result);
         response.sendRedirect("displayResult2User.jsp");
    }


      ddb.setDONO(dono);
      ddb.setDOLNNO(dolnno);
      ddb.setLNSTAT("N");
      ddb.setITEM(item);
      ddb.setUSERFLD1(itemdesc);

      ddb.setQTYOR(Float.parseFloat(qtyor));
      ddb.setUNITMO(uom);
//Unit Price is missing USERDBL1
      ddb.setUSERFLD2(custno);
      ddb.setUSERFLD3(custname);
      //ddb.setDELDATE(orddate);
	  ddb.setDELDATE(gn.getDBDateShort(orddate));
      ddb.setUSERTIME1(dodate) ;
      ddb.setCRBY(enrolledBy);
      ddb.setCRAT(gn.getDateTime());

//      if (inv.isQtyAvaliable(item,clas,attrib)){
        ddb.insertDODET();
        if(n==1) result = "<font color=\"green\">Item Added Successfully</font><br><br><center>"+
                         "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='createSO.jsp?DONO="+dono+"'\">";
        else     result = "<font color=\"red\"> Error in Item Addition  -  <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";

        result = "Sales Order Item Add <br><h3>"+result;
        session.setAttribute("RESULT",result);
        response.sendRedirect("displayResult2User.jsp");
    //  }
   //   else
   //   {
    //       result = "<font color=\"red\"> Stock Not Available for item "+item+"  <br><br><center>"+
    //                    "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
    //                    "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";
    //       result = "Please Check Item Classification.. <br><h3>"+result;
    //       session.setAttribute("RESULT",result);
    //       response.sendRedirect("displayResult2User.jsp");
    //  }
    }
    if (action.equalsIgnoreCase("Delete")) {
      String dono    = request.getParameter("DONO").trim();
      String dolnno  = request.getParameter("DOLNNO").trim();
      session.setAttribute("dono",dono);
      String item  = request.getParameter("ITEM").trim();
      n= ddb.deleteDODET(dono,dolnno,plant);
      
      DateUtils dateUtils = new DateUtils();
      boolean flag = false;
            Hashtable htRecvHis = new Hashtable();
            htRecvHis.clear();
            htRecvHis.put(IDBConstants.PLANT,plant);
            htRecvHis.put("DIRTYPE","SALES_ORDER_DELETE_PRODUCT");
            htRecvHis.put(IDBConstants.ITEM,item);
            htRecvHis.put(IDBConstants.MOVHIS_ORDNUM,dono);
            htRecvHis.put("LNNO",dolnno);
            htRecvHis.put(IDBConstants.CREATED_BY,enrolledBy);
            htRecvHis.put("MOVTID","");
            htRecvHis.put("RECID","");
            htRecvHis.put(IDBConstants.TRAN_DATE,dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate())); 
             htRecvHis.put(IDBConstants.CREATED_AT,dateUtils.getDateTime());
     
            flag = mov.insertIntoMovHis(htRecvHis);
      if(n==1) result = "<font color=\"green\">Product Deleted Successfully</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='/track/deleveryorderservlet?DONO="+dono+"&Submit=View'\">";
      else     result = "<font color=\"red\"> Error in Product Deletion  -  <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";

      result = "Outbound Order  <br><h3>"+result;
      session.setAttribute("RESULT",result);
      response.sendRedirect("displayResult2User.jsp");
    }
 if (action.equalsIgnoreCase("Modify")) {
      String dono    = su.fString(request.getParameter("DONO")).trim();
      String dolnno  = su.fString(request.getParameter("DOLNNO")).trim();
      String deldate = su.fString(request.getParameter("DELDATE")).trim();
      float qty = Float.parseFloat(su.fString(request.getParameter("QTYOR").trim()));
      n = ddb.updateDODET(dono,dolnno,qty,deldate);
      if(n==1) result = "<font color=\"green\">Item Updated Successfully</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='createSO.jsp?DONO="+dono+"'\">";
      else     result = "<font color=\"red\"> Error in Item Updation  -  <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";

      result = "Outbound Item Update <br><h3>"+result;
      session.setAttribute("RESULT",result);
      response.sendRedirect("displayResult2User.jsp");
    }

%>

<%@ include file="footer.jsp" %>

