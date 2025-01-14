<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%
	java.util.List do_list = new java.util.ArrayList();
	com.track.util.StrUtils strUtils = new com.track.util.StrUtils();
	com.track.util.DateUtils dateUtils = new com.track.util.DateUtils();
	com.track.db.util.HTReportUtil htReportUtil = new com.track.db.util.HTReportUtil();
	String deliverydateandtime="";
	java.util.Hashtable ht = new java.util.Hashtable();
	java.util.Hashtable ht1 = new java.util.Hashtable();	
	
	java.util.HashMap<String, String> loggerDetailsHasMap = new java.util.HashMap<String, String>();
	loggerDetailsHasMap.put(com.track.util.MLogger.COMPANY_CODE,
			(String) session.getAttribute("PLANT"));
	loggerDetailsHasMap.put(com.track.util.MLogger.USER_CODE,
					com.track.util.StrUtils
							.fString(
									(String) session
											.getAttribute("LOGIN_USER"))
							.trim());
	com.track.util.MLogger mLogger = new com.track.util.MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);

	htReportUtil.setmLogger(mLogger);

	String FROM_DATE = "", TO_DATE = "", CUSTOMER_TO = "", CUSTOMER_LO = "", DIRTYPE = "", ORDERTYPE, TO_ASSIGNEE = "", LOANASSIGNEE = "", ITEM = "",DESC="", fdate = "", tdate = "", JOBNO = "", ITEMNO = "", ORDERNO = "", CUSTOMER = "", xlaction = "",BATCH="",LOC="";
	String PLANT = (String) session.getAttribute("PLANT");
	String userID = (String) session.getAttribute("LOGIN_USER");
	FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));

	JOBNO = strUtils.fString(request.getParameter("JOBNO"));
	ITEMNO = strUtils.fString(request.getParameter("ITEM"));
        DESC = strUtils.fString(request.getParameter("DESC"));

	CUSTOMER_TO = strUtils.fString(request.getParameter("CUSTOMER_TO"));
	CUSTOMER_LO = strUtils.fString(request.getParameter("CUSTOMER_LO"));
	ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
	CUSTOMER = strUtils.fString(request.getParameter("CUSTOMER"));
	
	LOANASSIGNEE = strUtils.fString(request.getParameter("LOANASSIGNEE"));
	TO_ASSIGNEE = strUtils.fString(request.getParameter("TO_ASSIGNEE"));
	DIRTYPE = strUtils.fString(request.getParameter("DIRTYPE"));
	ORDERTYPE = strUtils.fString(request.getParameter("ORDERTYPE"));
        BATCH = strUtils.fString(request.getParameter("BATCH"));
	LOC = strUtils.fString(request.getParameter("LOC"));
        String CUSTOMERID= strUtils.fString(request.getParameter("CUSTOMERID"));
        String status = strUtils.fString(request.getParameter("STATUS"));
        String  PICKSTATUS    = strUtils.fString(request.getParameter("PICKSTATUS"));
        String ISSUESTATUS  = strUtils.fString(request.getParameter("ISSUESTATUS"));
        String MEMBER_STATUS = strUtils.fString(request.getParameter("MEMBER_STATUS"));
         String HPNO = strUtils.fString(request.getParameter("HPNO"));
    
    float subtotal=0,unitprice=0;
    DecimalFormat decformat = new DecimalFormat("#,##0.00");

	if (FROM_DATE == null)
		FROM_DATE = "";
	else
		FROM_DATE = FROM_DATE.trim();
	if (FROM_DATE.length() > 5)
      fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
		//fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5)				+ FROM_DATE.substring(0, 2);

	if (TO_DATE == null)
		TO_DATE = "";
	else
		TO_DATE = TO_DATE.trim();
	if (TO_DATE.length() > 5)
        tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	

	xlaction = strUtils.fString(request.getParameter("xlAction"));

	
	if (DIRTYPE.equalsIgnoreCase("MOBILE_ENQUIRY")) {
        
        if(strUtils.fString(JOBNO).length() > 0)            ht.put("A.JOBNUM",JOBNO);
        if(strUtils.fString(ITEMNO).length() > 0)           ht.put("B.ITEM",ITEMNO);
        if(strUtils.fString(ORDERNO).length() > 0)          ht.put("B.DONO",ORDERNO);
        if(strUtils.fString(CUSTOMER).length() > 0)         ht.put("A.CUSTNAME",CUSTOMER);
        if(strUtils.fString(CUSTOMERID).length() > 0)       ht.put("A.CUSTCODE",CUSTOMERID);
        if(strUtils.fString(ORDERTYPE).length() > 0)        ht.put("A.ORDERTYPE",ORDERTYPE);
        if(strUtils.fString(ISSUESTATUS).length() > 0)      ht.put("B.LNSTAT",ISSUESTATUS);
        if(strUtils.fString(PICKSTATUS).length() > 0)       ht.put("B.PICKSTATUS",PICKSTATUS);
        if(strUtils.fString(HPNO).length() > 0)             ht.put("A.CONTACTNUM",HPNO);
	}
if (DIRTYPE.equalsIgnoreCase("MOBILE_REGISTER")) {
        
        if(strUtils.fString(JOBNO).length() > 0)            ht.put("A.JOBNUM",JOBNO);
        if(strUtils.fString(ITEMNO).length() > 0)           ht.put("B.ITEM",ITEMNO);
        if(strUtils.fString(ORDERNO).length() > 0)          ht.put("B.DONO",ORDERNO);
        if(strUtils.fString(CUSTOMER).length() > 0)         ht.put("A.CUSTNAME",CUSTOMER);
        if(strUtils.fString(CUSTOMERID).length() > 0)       ht.put("A.CUSTCODE",CUSTOMERID);
        if(strUtils.fString(ORDERTYPE).length() > 0)        ht.put("A.ORDERTYPE",ORDERTYPE);
        if(strUtils.fString(ISSUESTATUS).length() > 0)      ht.put("B.LNSTAT",ISSUESTATUS);
        if(strUtils.fString(PICKSTATUS).length() > 0)       ht.put("B.PICKSTATUS",PICKSTATUS);
        if(strUtils.fString(HPNO).length() > 0)             ht.put("A.CONTACTNUM",HPNO);
	}
	if (DIRTYPE.equalsIgnoreCase("MOBILE ORDER")) {
		
		
	          if(strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
                  if(strUtils.fString(ITEMNO).length() > 0)       ht.put("B.ITEM",ITEMNO);
                  if(strUtils.fString(ORDERNO).length() > 0)      ht.put("B.DONO",ORDERNO);
                  if(strUtils.fString(CUSTOMER).length() > 0)     ht.put("B.CUSTNAME",CUSTOMER);
                  if(strUtils.fString(CUSTOMERID).length() > 0)   ht.put("A.CUSTCODE",CUSTOMERID);
                  if(strUtils.fString(ORDERTYPE).length() > 0)    ht.put("ORDERTYPE",ORDERTYPE);
                  if(strUtils.fString(ISSUESTATUS).length() > 0)  ht.put("B.LNSTAT",ISSUESTATUS);
                  if(strUtils.fString(PICKSTATUS).length() > 0)   ht.put("B.PICKSTATUS",PICKSTATUS);
                  if(strUtils.fString(MEMBER_STATUS).length() > 0)   ht.put("B.ISMEMBER",MEMBER_STATUS);
                   if(strUtils.fString(HPNO).length() > 0)       ht.put("A.CONTACTNUM",HPNO);
	        
	        
	      
	     
    }
      

	if (xlaction.equalsIgnoreCase("GenerateXLSheet")) {

		try {
                    if (DIRTYPE.equalsIgnoreCase("MOBILE ORDER")){
			  do_list = htReportUtil.getMobileShoppingSummary(ht,fdate, tdate, DIRTYPE, PLANT, DESC,"");
                    }
                     if (DIRTYPE.equalsIgnoreCase("MOBILE_ENQUIRY")){
			  do_list = htReportUtil.getMobileEnquirySummaryList(ht,fdate,tdate,DIRTYPE,PLANT,DESC);
                    }
          if (DIRTYPE.equalsIgnoreCase("MOBILE_REGISTER")){
           			  do_list = htReportUtil.getMobileRegisterSummaryList(ht,fdate,tdate,DIRTYPE,PLANT,DESC);
                               }
			
			if (do_list.size() > 0) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment; filename=OrderReport.xls");
				java.util.Vector v = null;
			
%>

	

<%
					
  if (DIRTYPE.equalsIgnoreCase("MOBILE_ENQUIRY")) {
%>
<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	<TR BGCOLOR="#000066">

        <TH><font color="#ffffff" align="center">S/N</TH>
        <TH><font color="#ffffff" align="left"><b>Order No</TH>
        <TH><font color="#ffffff" align="left"><b>Order Type</TH>
        <TH><font color="#ffffff" align="left"><b>Ref No</TH>
        <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
        <TH><font color="#ffffff" align="left"><b>Product ID</TH>
        <TH><font color="#ffffff" align="left"><b>Description</TH>
      <!--  <TH><font color="#ffffff" align="left"><b>Unit Price</TH>-->
        <TH><font color="#ffffff" align="left"><b>Order Date</TH>
        
        <TH><font color="#ffffff" align="left"><b> Status</TH>
	</tr>
        
	<%
	} else if (DIRTYPE.equalsIgnoreCase("MOBILE ORDER")) {
  %>
<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	<TR BGCOLOR="#000066">
            <TH><font color="#ffffff" align="center">S/N</TH>
            <TH><font color="#ffffff" align="left"><b>Order No</TH>
            <TH><font color="#ffffff" align="left"><b>Order Type</TH>
            <TH><font color="#ffffff" align="left"><b>Ref No</TH>
            <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
            <TH><font color="#ffffff" align="left"><b>Product ID</TH>
            <TH><font color="#ffffff" align="left"><b>Order Date</TH>
            <TH><font color="#ffffff" align="left"><b>Deliver Date & time</TH>
            <TH><font color="#ffffff" align="left"><b>Order Qty</TH>
            <TH><font color="#ffffff" align="left"><b>Pick Qty</TH>
            <TH><font color="#ffffff" align="left"><b>Issue Qty</TH>
            <TH><font color="#ffffff" align="left"><b>Unit Price</TH>
            <TH><font color="#ffffff" align="left"><b>Total</TH>
	</tr>
	
<%} else if (DIRTYPE.equalsIgnoreCase("MOBILE_REGISTER")){ %>
	<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	<TR BGCOLOR="#000066">

        <TH><font color="#ffffff" align="center">S/N</TH>
        <TH><font color="#ffffff" align="left"><b>Order No</TH>
        <TH><font color="#ffffff" align="left"><b>Order Type</TH>
        <TH><font color="#ffffff" align="left"><b>Ref No</TH>
        <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
        <TH><font color="#ffffff" align="left"><b>Product ID</TH>
        <TH><font color="#ffffff" align="left"><b>Description</TH>
     
        <TH><font color="#ffffff" align="left"><b>Order Date</TH>
        
        <TH><font color="#ffffff" align="left"><b> Status</TH>
	</tr>
        	
	
<%} %>
    <%
				
                                        for (int i = 0; i < do_list.size(); i++) {
					try {
						java.util.Map map = (java.util.Map) do_list.get(i);
                                                int iIndex = i + 1;
                                                String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF": "#dddddd";
                                                
                                                String pkstatus= (String)map.get("lnstat"); 
                                                if(pkstatus.equalsIgnoreCase("N"))
                                                	pkstatus="UNATTENDED";
                                                else if(pkstatus.equalsIgnoreCase("C"))
                                                	pkstatus = "ATTENDED";
    %>
         
    <%
         if (DIRTYPE.equalsIgnoreCase("MOBILE_ENQUIRY")) {
     %>
               <TR bgcolor="<%=bgcolor%>">
                                <TD align="center"><%=iIndex%></TD>
                                <TD><%=(String)map.get("dono")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("ordertype")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("jobNum")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("custname")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("item") %></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("itemdesc") %></TD>
                            <!--    <TD align= "left">&nbsp;&nbsp;&nbsp;<%=StrUtils.currencyWtoutSymbol((String)map.get("unitprice"))%></TD>-->
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("TRANDATE") %></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=pkstatus%></TD> 
               </TR>
                            
           <%} if (DIRTYPE.equalsIgnoreCase("MOBILE_REGISTER")) {
        	  // deliverydateandtime=(String)map.get("deliverydate")+ " " +(String)map.get("deliverytime");
        	   %>
        	   <%  String lnstatus= (String)map.get("lnstat");
               if(lnstatus.equalsIgnoreCase("N"))
            	   lnstatus="REGISTERED";
               else if(lnstatus.equalsIgnoreCase("C"))
            	   lnstatus = "ATTENDED";  %>
          <TR bgcolor="<%=bgcolor%>">
                                <TD align="center"><%=iIndex%></TD>
                                <TD><%=(String)map.get("dono")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("ordertype")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("jobNum")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("custname")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("item") %></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("itemdesc") %></TD>
                           
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("TRANDATE") %></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=lnstatus %></TD>
                                
                            
               </TR>
                                                  
                                                    
       <%	}

                        if (DIRTYPE.equalsIgnoreCase("MOBILE ORDER")) {
                                
                            unitprice = Float.parseFloat((String)map.get("unitprice"));
                            unitprice = StrUtils.Round(unitprice,2);
                              
                            subtotal = Float.parseFloat((String)map.get("total"));
                            subtotal = StrUtils.Round(subtotal,2);
                            deliverydateandtime=(String)map.get("deliverydate")+ " " +(String)map.get("deliverytime");
	 %>
                            <TR bgcolor="<%=bgcolor%>">
                                <TD align="center"><%=iIndex%></TD>
                                <TD><%=(String)map.get("dono")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("ordertype")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("jobnum")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("custname")%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("item") %></TD>
                                 <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("trandate") %></TD>
                                 <TD align= "left">&nbsp;&nbsp;&nbsp;<%=deliverydateandtime%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)map.get("qtyor")) %></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)map.get("qtypick")) %></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)map.get("qty")) %></TD>
                                <!-- <TD align= "left">&nbsp;&nbsp;&nbsp;<%=StrUtils.currencyWtoutSymbol((String)map.get("unitprice"))%></TD>
                                <TD align= "left">&nbsp;&nbsp;&nbsp;<%=StrUtils.currencyWtoutSymbol((String)map.get("total"))%></TD>-->
                                <TD align="center" class="textbold">&nbsp; <%=decformat.format(unitprice)%></TD>
                                <TD align="center" class="textbold">&nbsp; <%=decformat.format(subtotal)%></TD>
                                                                            
                            </TR>                                             
                                                    

	<%	}
		} catch (Exception ee) {
					System.out.println("######################## MobileOrderExcel ################ :"+ ee);
					}
	}%></TABLE><%
			} else if (do_list.size() < 1) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition","attachment; filename=OrderReport.xls");
				out.println("No Records Found To List");
			}

		} catch (Exception e) {
		}
	}
%>
