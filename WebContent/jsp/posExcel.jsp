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
	
	String TRANID = "", TO_DATE = "",CUSTOMER_TO="",CUSTOMER_LO="", DIRTYPE = "",ORDERTYPE,TO_ASSIGNEE="",LOANASSIGNEE="", ITEM = "", fdate = "", tdate = "", JOBNO = "", ITEMNO = "", ORDERNO = "", CUSTOMER = "", xlaction = "";
	String PLANT = (String) session.getAttribute("PLANT");
        String userID = (String) session.getAttribute("LOGIN_USER");
        TRANID = strUtils.fString(request.getParameter("TRANID"));
	
	

	if (TRANID == null)
		TRANID = "";
		
		try {
			
			do_list = salesdao.getSalesByTranid(PLANT,TRANID,"");
			
		if (do_list.size() > 0) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment; filename=posDetail.xls");
				java.util.Vector v = null;
			%>	
				<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
				align="center">
				<TR BGCOLOR="#000066">
				
				<TH><font color="#ffffff" align="center">Receipt No</TH>
                <TH><font color="#ffffff" align="left"><b>Product ID</TH>
                <TH><font color="#ffffff" align="left"><b>Product Description</TH>
                <TH><font color="#ffffff" align="left"><b>Qty</TH>
                <TH><font color="#ffffff" align="left"><b>Price</TH>
				
				</TR>
				
	<% //out.println(" Receipt No\tProduct ID\tProduct Description\tQty\tPrice");
							
				for (int i = 0; i < do_list.size(); i++) {

				

					


					try {
						java.util.Map map = (java.util.Map) do_list
								.get(i);

					

							/*out.println(TRANID+"\t"+(String) map.get("item") + "\t"
									+ (String) map.get("itemdesc") + "\t"
									+ (String) map.get("qty") + "\t"
									+ (String) map.get("unitprice"));*/%>
									
									<tr>
									<TD align= "center"><%=TRANID %></TD>
									<TD align= "center">&nbsp;<%=(String)map.get("item")%></TD>
                                    <TD align= "center"><%=(String)map.get("itemdesc")%></TD>
                                    <TD align= "right"><%=(String)map.get("qty")%></TD>
                                    <TD align= "right"><%=DbBean.CURRENCYSYMBOL%><%=(String)map.get("unitprice") %></TD>
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
