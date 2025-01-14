<%@ page import="com.track.gates.DbBean"%>
<%@page import="com.track.dao.SalesDetailDAO"%>
<%
	java.util.List do_list = new java.util.ArrayList();
	com.track.util.StrUtils strUtils = new com.track.util.StrUtils();
	com.track.util.DateUtils dateUtils = new com.track.util.DateUtils();
	com.track.db.util.HTReportUtil htReportUtil = new com.track.db.util.HTReportUtil();
	
	
	java.util.Hashtable ht = new java.util.Hashtable();
	SalesDetailDAO salesdao = new SalesDetailDAO();
	
	java.util.HashMap<String, String> loggerDetailsHasMap = new java.util.HashMap<String, String>();
	loggerDetailsHasMap.put(com.track.util.MLogger.COMPANY_CODE, (String) session
			.getAttribute("PLANT"));
	loggerDetailsHasMap.put(com.track.util.MLogger.USER_CODE, com.track.util.StrUtils.fString(
			(String) session.getAttribute("LOGIN_USER")).trim());
	com.track.util.MLogger mLogger = new com.track.util.MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);
	
	
	htReportUtil.setmLogger(mLogger);
	
	String TRANID = "", TO_DATE = "",CUSTOMER_TO="",TRANTYPE="",userid="",CUSTOMER_LO="", STARTDATE="",TODATE="",DESC="",LOC="",recptno="",DIRTYPE = "",ORDERTYPE,TO_ASSIGNEE="",LOANASSIGNEE="", ITEM = "", fdate = "", tdate = "", JOBNO = "", ITEMNO = "", ORDERNO = "", CUSTOMER = "", xlaction = "";
	String PLANT = (String) session.getAttribute("PLANT");
        String userID = (String) session.getAttribute("LOGIN_USER");
        //TRANID = strUtils.fString(request.getParameter("TRANID"));
        STARTDATE=strUtils.fString(request.getParameter("STARTDATE"));
    	TODATE=strUtils.fString(request.getParameter("TODATE"));
    	ITEM = strUtils.fString(request.getParameter("ITEM")).trim();
        DESC =strUtils.fString(request.getParameter("DESC")).trim();
    	//LOC =strUtils.fString(request.getParameter("LOC")).trim();
    	recptno = strUtils.fString(request.getParameter("recptno")).trim();
    	TRANTYPE = strUtils.fString(request.getParameter("TRANTYPE")).trim();
    	userid = strUtils.fString(request.getParameter("USERID")).trim();
    String	extraCond="";
    	DecimalFormat decformat = new DecimalFormat();
    	if(DESC.length()>0){
            extraCond = " AND REPLACE(ITEMDESC,' ','') LIKE '"+ strUtils.InsertQuotes(DESC.replaceAll(" ","")) + "%' group by tranid,trantype,purchasedate order by cast(purchasedate as date) ";
            }else{
              extraCond = " group by item,itemdesc,qty,trantype,tranid,purchasedate,remarks,rsncode,crby,batch order by cast(purchasedate as date)";
            }
           
	if (TRANID == null)
		TRANID = "";
		
		try {
			
			do_list =  salesdao.getDistinctSalesForMgmt(PLANT,STARTDATE,TODATE,ITEM,LOC,recptno,userid,TRANTYPE,"","","",extraCond);
			
		if (do_list.size() > 0) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment; filename=posDetail.xls");
				java.util.Vector v = null;
			%>	
				
<%@page import="java.text.DecimalFormat"%><TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
				align="center">
				<TR BGCOLOR="#000066">
				<TH><font color="#ffffff" align="center">S/N</TH>
				<TH><font color="#ffffff" align="center">Tran ID</TH>
				<TH><font color="#ffffff" align="center">Receipt No</TH>
                <TH><font color="#ffffff" align="left"><b>User ID</TH>
                <TH><font color="#ffffff" align="left"><b>Transaction Date</TH>
                <TH><font color="#ffffff" align="left"><b>Remarks</TH>
                <TH><font  align="center" color="#ffffff">Product ID</TH>
      			 <TH><font  align="center" color="#ffffff">Description</TH>
        		<TH><font  align="center" color="#ffffff">Batch</TH>
        		<TH><font  align="center" color="#ffffff">Qty</TH>
                <TH><font color="#ffffff" align="left"><b>Total Price</TH>
				
				</TR>
				
	<% //out.println(" Receipt No\tProduct ID\tProduct Description\tQty\tPrice");
							
				for (int i = 0; i < do_list.size(); i++) {
	
									try {
						java.util.Map map = (java.util.Map) do_list
								.get(i);
						int iIndex = i + 1;
                        String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF": "#dddddd";
						float price = Float.parseFloat((String)map.get("price")); 

							/*out.println(TRANID+"\t"+(String) map.get("item") + "\t"
									+ (String) map.get("itemdesc") + "\t"
									+ (String) map.get("qty") + "\t"
									+ (String) map.get("unitprice"));*/%>
									
									<tr bgcolor="<%=bgcolor%>">
									<TD align= "center">&nbsp;&nbsp;&nbsp;<%=iIndex%></TD>
									<TD align= "center">&nbsp;&nbsp;&nbsp;<%=(String)map.get("postranid") %></TD>
									<TD align= "center">&nbsp;&nbsp;&nbsp;<%=(String)map.get("tranid")%></TD>
                                    <TD align= "center">&nbsp;&nbsp;&nbsp;<%=(String)map.get("CRBY")%></TD>
                                    <TD align= "center">&nbsp;&nbsp;&nbsp;<%=(String)map.get("purchasedate")%></TD>
                                    <TD align="center" class="textbold">&nbsp;<%=map.get("remarks")%></TD>
                                    <TD align="center"><%=(String)map.get("item")%></TD>
            						<TD align="center"><%=(String)map.get("itemdesc")%></TD>
            						<TD align="center"><%=(String)map.get("batch")%></TD>
            						 <TD align="center"><%=(String)map.get("qty")%></TD>
                                    <TD align= "center">&nbsp;&nbsp;&nbsp;<%=DbBean.CURRENCYSYMBOL%><%=decformat.format(price)%></TD>
                                    
                                    </tr>
									
									<% 
							} catch (Exception ee) {
						System.out
								.println("######################## WorkOrderExcel ################ :"
										+ ee);
					}
				}
			} else if (do_list.size() < 1) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment; filename=posDetail.xls");
				
			

				out.println("No Records Found To List");
			}

		} catch (Exception e) {
		}
	
%>
