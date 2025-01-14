<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="java.util.*"%>
<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.dao.ItemMstDAO"%>
<%@page import="com.track.dao.CustomerBeanDAO"%>
<%@page import="com.track.dao.MerchantBeanDAO"%>
<%@page import="com.track.dao.CustomerReturnDAO"%>
<%
	StrUtils _strUtils = new StrUtils();
	Generator generator = new Generator();

	DateUtils _dateUtils = new DateUtils();
	ArrayList movQryList = new ArrayList();

	CustomerReturnDAO custretdao = new CustomerReturnDAO();
	com.track.util.StrUtils strUtils = new com.track.util.StrUtils();

	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String userID = (String) session.getAttribute("LOGIN_USER");
	String FROM_DATE = "", TO_DATE = "", DIRTYPE = "", BATCH = "", EXPIREDATE = "", USER = "", MERCHANT_NAME = "", ITEM = "", fdate = "", tdate = "", JOBNO = "", ITEMNO = "", ORDERNO = "", CUSTOMER = "", PGaction = "", ITEMDESC = "", xlaction = "";

	PGaction = _strUtils.fString(request.getParameter("PGaction"))
			.trim();
	String html = "", cntRec = "false";
	String fieldDesc = "";
	ITEMNO = _strUtils.fString(request.getParameter("ITEM"));
	EXPIREDATE = _strUtils.fString(request.getParameter("EXPIREDATE"));
	CUSTOMER = _strUtils.fString(request.getParameter("CUST_NAME"));
	FROM_DATE = _strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = _strUtils.fString(request.getParameter("TO_DATE"));
	MERCHANT_NAME = _strUtils.fString(request
			.getParameter("MERCHANT_NAME"));

	xlaction = strUtils.fString(request.getParameter("xlAction"));

	if (xlaction.equalsIgnoreCase("GenerateXLSheetForCustomerReturns")) {

		try {

			if (_strUtils.fString(ITEMNO).length() > 0) {
				ItemMstUtil itemMstUtil = new ItemMstUtil();
				String temItem = itemMstUtil
						.isValidAlternateItemInItemmst(plant, ITEMNO);
				if (temItem != "") {
					ITEMNO = temItem;
				} else {
					throw new Exception("Product not found!");
				}
			}
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			if (ITEMNO.length() > 0)
				ht.put(IDBConstants.ITEM, ITEMNO);
			if (CUSTOMER.length() > 0)
				ht.put(IDBConstants.CUSTNAME, CUSTOMER);
                        if(ORDERNO.length()>0)
                                  ht.put(IDBConstants.ORDERNO,ORDERNO);
			if (MERCHANT_NAME.length() > 0)
				ht.put(IDBConstants.MERCHANT_NAME, MERCHANT_NAME);
			if (EXPIREDATE.length() > 0)
				ht.put(IDBConstants.EXPIREDATE, EXPIREDATE);
				//ht.put(IDBConstants.EXPIREDATE, DateUtils.getDateinyyyy_mm_dd(EXPIREDATE));
			movQryList = custretdao
					.CustomerReturnSummary(
							"item,itemdesc,serialno,isnull(custname,'') custname,isnull(orderno,'') orderno,isnull(rsncode,'') rsncode,expiredate,isnull(remarks,'') as remarks, (SUBSTRING(CRAT,7,2) + '/' + SUBSTRING(CRAT,5,2) + '/' + SUBSTRING(CRAT,1,4)) as returndate ",
							ht, FROM_DATE, TO_DATE, "");

			//do_list = masterDetailDAO.MovementHistoryDetails(doNumber,customerNo,status,cfromdate,ctodate,userId,PLANT);
			if (movQryList.size() > 0) {
				response.setContentType("application/vnd.ms-excel");
				response
						.setHeader("Content-disposition",
								"attachment; filename=CustomerReturnsSummary.xls");
%>

<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	<TR BGCOLOR="#000066">

		<TH><font color="#ffffff" align="center">S/N</TH>
		<TH><font color="#ffffff" align="left"><b>Product ID</TH>
		<TH><font color="#ffffff" align="left"><b>Serial No</TH>
		<TH><font color="#ffffff" align="left"><b>Customer Name</TH>
		<TH><font color="#ffffff" align="left"><b>Merchant Name</TH>
		<TH><font color="#ffffff" align="left"><b>Return date</TH>
		<TH><font color="#ffffff" align="left"><b>Expire date</TH>
		<TH><font color="#ffffff" align="left"><b>Remarks</TH>
		
	</tr>
	
	<%
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#dddddd";
			
	%>

	<TR bgcolor="<%=bgcolor%>">
		<TD align="center"><%=iIndex%></TD>
		<TD><%=(String) lineArr.get("item")%></TD>
		<TD align="left"><%=(String) lineArr.get("serialno")%></TD>
		<TD align="left"><%=(String) lineArr.get("custname")%></TD>
		<TD align="left"><%=(String) lineArr.get("rsncode")%></TD>
		<TD align="left"><%=(String) lineArr.get("returndate")%></TD>
		<TD align="left"><%=(String) lineArr.get("expiredate")%></TD>
		<TD align="left"><%=(String) lineArr.get("remarks")%></TD>
	
	</TR>
	<%
		}
	%>
</TABLE>

<%
	} else if (movQryList.size() < 1) {
				response.setContentType("application/vnd.ms-excel");
				response
						.setHeader("Content-disposition",
								"attachment; filename=CustomerReturnsSummary.xls");

				out.println("No Records Found To List");
			}

		} catch (Exception e) {
		}
	}
%>