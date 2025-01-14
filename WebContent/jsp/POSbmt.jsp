<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.util.DateUtils"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="java.util.*"%>
<title>Saving to the database</title>


<jsp:useBean id="gn"  class="com.track.gates.Generator" />

<jsp:useBean id="tl"  class="com.track.gates.TableList" />
<jsp:useBean id="su"  class="com.track.util.StrUtils"/>

<jsp:useBean id="mov"  class="com.track.db.util.MovHisUtil"/>
<jsp:useBean id="tphb"  class="com.track.tables.POHDR"/>
<jsp:useBean id="tpdb"  class="com.track.tables.PODET"/>
<jsp:useBean id="log"  class="com.track.util.MLogger"/>


<%
// Getting the parameters of USER_INFO from external file
   
    log.log(0,"Entering to POSubmit.jsp");
    session=request.getSession();
    String plant=su.fString((String)session.getAttribute("PLANT")).trim();
    String action  = request.getParameter("Submit").trim();
    String result ="", sql="";       int n=1;
    MovHisDAO movdao = new MovHisDAO();
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    if (action.equalsIgnoreCase("Add")) {
      String pono    = su.fString(request.getParameter("PONO")).trim();
      String vendno  = su.fString(request.getParameter("VENDNO")).trim();
      String vendname  = su.fString(request.getParameter("VENDNAME")).trim();
      String deldate = su.fString(request.getParameter("DELDATE")).trim();
      String origin  = su.fString(request.getParameter("USERFLD1")).trim();
      String polnno  = su.fString(request.getParameter("POLNNO")).trim();
      String item    = su.fString(request.getParameter("ITEM")).trim();
      String itemdesc    = su.fString(request.getParameter("DESC")).trim();
      String qtyor   = su.fString(request.getParameter("QTYOR")).trim();
      String uom   = su.fString(request.getParameter("UOM")).trim();
       String RFLAG   = su.fString(request.getParameter("RFLAG")).trim();
      tpdb.setPONO(pono);
      tpdb.setPOLNNO(polnno);
      tpdb.setLNSTAT("N");
      tpdb.setITEM(item);
      tpdb.setUSERFLD1(itemdesc);
      tpdb.setUSERFLD2(vendno);
      tpdb.setUSERFLD3(vendname);
      tpdb.setDELDATE(deldate);
      tpdb.setQTYOR(Float.parseFloat(qtyor));
      tpdb.setUNITMO(uom);
      tpdb.setCRBY(enrolledBy);
      tpdb.setCRAT(gn.getDateTime());
      n = tpdb.insertPODET();
      if(n==1) result = "<font color=\"green\">Product Added Successfully</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='createPO.jsp?PONO="+pono+"'\">";
      else     result = "<font color=\"red\"> Error in Product Addition   <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";

      result = "Purchase Order<br><h3>"+result;
      session.setAttribute("RESULT",result);
      response.sendRedirect("displayResult2User.jsp");
    }
    if (action.equalsIgnoreCase("Delete")) {
      String pono    = request.getParameter("PONO").trim();
      String polnno  = request.getParameter("POLNNO").trim();
      String item  = request.getParameter("ITEM").trim();
       String rflag  = request.getParameter("RFLAG").trim();
      DateUtils dateUtils = new DateUtils();
      boolean flag = false;
      n = tpdb.deletePODETML(pono,polnno,plant);//.insertPODET();
          Hashtable htRecvHis = new Hashtable();
            htRecvHis.clear();
            htRecvHis.put(IDBConstants.PLANT,plant);
            htRecvHis.put("DIRTYPE",TransactionConstants.PURCHASER_ORDER_DELETE_PRODUCT);
            htRecvHis.put(IDBConstants.ITEM,item);
            htRecvHis.put(IDBConstants.MOVHIS_ORDNUM,pono);
            htRecvHis.put("LNNO",polnno);
            htRecvHis.put(IDBConstants.CREATED_BY,enrolledBy);
            htRecvHis.put("MOVTID","");
            htRecvHis.put("RECID","");
            htRecvHis.put(IDBConstants.TRAN_DATE,dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate())); 
             htRecvHis.put(IDBConstants.CREATED_AT,dateUtils.getDateTime());
     
            flag = movdao.insertIntoMovHis(htRecvHis);
      if(n==1) result = "<font color=\"green\">Product Deleted Successfully</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='/track/purchaseorderservlet?PONO="+pono+"&RFLAG="+rflag+"&Submit=View'\">";
      else     result = "<font color=\"red\"> Error in Product Deletion   <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";

      result = "Inbound Order <br><h3>"+result;
      session.setAttribute("RESULT",result);
      response.sendRedirect("displayResult2User.jsp");
    }

   if (action.equalsIgnoreCase("Modify")) {
      String pono    = request.getParameter("PONO").trim();
      String polnno  = request.getParameter("POLNNO").trim();
      float qty      = Float.parseFloat(request.getParameter("QTYOR").trim());

      n = tpdb.updatePODET(pono,polnno,qty);//.insertPODET();
      if(n==1) result = "<font color=\"green\">Product Updated Successfully</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='createPO.jsp?PONO="+pono+"'\">";
      else     result = "<font color=\"red\"> Error in Item Updation   <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";

      result = "Inbound Order <br><h3>"+result;
      session.setAttribute("RESULT",result);
      response.sendRedirect("displayResult2User.jsp");
    }

%>

<%@ include file="footer.jsp" %>

