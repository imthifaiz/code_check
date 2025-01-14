<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%
	java.util.List do_list = new java.util.ArrayList();
	com.track.util.StrUtils strUtils = new com.track.util.StrUtils();
	com.track.util.DateUtils dateUtils = new com.track.util.DateUtils();
	com.track.db.util.HTReportUtil htReportUtil = new com.track.db.util.HTReportUtil();
	com.track.db.util.DOUtil _DOUti= new com.track.db.util.DOUtil();

	java.util.Hashtable ht = new java.util.Hashtable();
	java.util.Hashtable ht1 = new java.util.Hashtable();	

	java.util.HashMap<String, String> loggerDetailsHasMap = new java.util.HashMap<String, String>();
	loggerDetailsHasMap.put(com.track.util.MLogger.COMPANY_CODE,(String) session.getAttribute("PLANT"));
	loggerDetailsHasMap.put(com.track.util.MLogger.USER_CODE,com.track.util.StrUtils.fString((String) session
											                             .getAttribute("LOGIN_USER")).trim());
	com.track.util.MLogger mLogger = new com.track.util.MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);

	htReportUtil.setmLogger(mLogger);
	String deliverydateandtime="";
	String DIRTYPE = "",   ITEM = "",BATCH="",PRD_DESCRIP="",CUSTOMERNAME="",CONTAINER="",ORDERNO="", xlaction = "",CUSTOMERTYPE="";
	String PLANT = (String) session.getAttribute("PLANT");
	String userID = (String) session.getAttribute("LOGIN_USER");
	//-----Added by Deen on Feb 7 2014, Description: Include From and To date in container summary search
	String FROM_DATE ="",  TO_DATE = "";
	//-----Added by Deen on Feb 7 2014 end
	
	CUSTOMERNAME    = strUtils.fString(request.getParameter("CUSTOMER"));
	CONTAINER = strUtils.fString(request.getParameter("CONTAINER"));
	ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
	ITEM    = strUtils.fString(request.getParameter("ITEM"));
	BATCH   = strUtils.fString(request.getParameter("BATCH"));
	PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
	FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
	CUSTOMERTYPE = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
	String curDate =new DateUtils().getDate();
	if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
	
    float subtotal=0,unitprice=0;
    DecimalFormat decformat = new DecimalFormat("#,##0.00");
	xlaction = strUtils.fString(request.getParameter("xlAction"));
	if (xlaction.equalsIgnoreCase("GenerateXLSheet")) {
		try {
		    if(strUtils.fString(CUSTOMERNAME).length() > 0)        ht.put("CNAME",CUSTOMERNAME);
            if(strUtils.fString(CONTAINER).length() > 0)           ht.put("CONTAINER",CONTAINER);
            if(strUtils.fString(ORDERNO).length() > 0)             ht.put("DONO",ORDERNO) ; 
            if(strUtils.fString(ITEM).length() > 0)                ht.put("ITEM",ITEM);
            if(strUtils.fString(PRD_DESCRIP).length() > 0)         ht.put("ITEMDESC",PRD_DESCRIP) ;  
            if(strUtils.fString(BATCH).length() > 0)               ht.put("BATCH",BATCH);
            if(strUtils.fString(CUSTOMERTYPE).length() > 0)        ht.put("CUSTTYPE",CUSTOMERTYPE);
 	       
		    do_list = _DOUti.getContainerSummary(ht,FROM_DATE,TO_DATE,PLANT);
            			
			if (do_list.size() > 0) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition","attachment; filename=SalesOrderSummaryByContainer.xls");
				java.util.Vector v = null;
				
%>
<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	align="center">
	   <TR BGCOLOR="#000066">
                <TH><font color="#ffffff" align="center">S/N</TH>
                <TH><font color="#ffffff" align="left"><b>Customer</TH>
                <TH><font color="#ffffff" align="left"><b>Container</TH>
                 <TH><font color="#ffffff" align="left"><b>Issued Date</TH>
                <TH><font color="#ffffff" align="left"><b>Order No</TH>
                <TH><font color="#ffffff" align="left"><b>Product ID</TH>
                <TH><font color="#ffffff" align="left"><b>Description</TH>
                <TH><font color="#ffffff" align="left"><b>Batch/Serial No</TH>
                <TH><font color="#ffffff" align="left"><b>Quantity</TH>
       </TR>
<%
				for (int i = 0; i < do_list.size(); i++) {
					try {
						java.util.Map map = (java.util.Map) do_list.get(i);
                        int iIndex = i + 1;
                        String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF": "#dddddd";
				%>
                        <TR bgcolor="<%=bgcolor%>">
                            <TD align="center"><%=iIndex%></TD>
                            <TD><%=(String)map.get("cname")%></TD>
                            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("container")%></TD>
                             <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("transactiondate")%></TD>
                             <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("dono")%></TD>
                            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("item")%></TD>
                            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("itemdesc")%></TD>
                            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("batch") %></TD>
                            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)map.get("qty") %></TD>
                                             
                       </TR>
                                                    
     <%		
				} catch (Exception ee) {
						System.out
								.println("######################## WorkOrderExcel ################ :"
										+ ee);
					}
				}%></TABLE><%
			} else if (do_list.size() < 1) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition","attachment; filename=SalesOrderSummaryByContainer.xls");

				out.println("No Records Found To List");
			}

		} catch (Exception e) {
		}
	}
%>
