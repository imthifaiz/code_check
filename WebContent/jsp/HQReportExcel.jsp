<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	InvUtil invUtil = new InvUtil();
	ArrayList invQryList = new ArrayList();
	List saleList = null, stockin = null;
	ArrayList invQryListSumTotal = new ArrayList();
	ShipHisDAO shipdao = new ShipHisDAO();
CustomerReturnDAO cusrtnDao = new CustomerReturnDAO();
	String fieldDesc = "";
	String USERID = "", PLANT = "", LOC = "", ITEM = "", STOCK_SALE = "", STOCK_TOTAL = "",TOTAL_RTN="", BATCH = "", PRD_TYPE_ID = "", PRD_DESCRIP = "", STATUS = "", QTY = "", QTYALLOC = "", FROM_DATE = "", TO_DATE = "", fdate = "", tdate = "", xlaction = "";
	String html = "";
	int Total = 0, STOCK_EXPIRE_INT = 0, STOCK_TOTAL_INT = 0, STOCK_BALANCE_INT = 0;
	String SumColor = "", PRD_CLS_ID = "", PRD_CLS_ID1 = "";
	boolean flag = false;

	String PGaction = strUtils
			.fString(request.getParameter("PGaction")).trim();
	PLANT = session.getAttribute("PLANT").toString();
	USERID = session.getAttribute("LOGIN_USER").toString();
	LOC = strUtils.fString(request.getParameter("LOC"));
	ITEM = strUtils.fString(request.getParameter("ITEM"));

	PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
	FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
	if (FROM_DATE == null)
		FROM_DATE = "";
	else
		FROM_DATE = FROM_DATE.trim();

	ItemMstUtil itemMstUtil = new ItemMstUtil();

	xlaction = strUtils.fString(request.getParameter("xlAction"));

	boolean cntRec = false;

	if (xlaction.equalsIgnoreCase("GenerateXLSheetForHQReport")) {

		try {

			ITEM = itemMstUtil.isValidAlternateItemInItemmst(PLANT,
					ITEM);

			Hashtable ht = new Hashtable();
			ht.put("a.PLANT", PLANT);
			if (strUtils.fString(PLANT).length() > 0)
				if (strUtils.fString(ITEM).length() > 0) {
					ht.put("a.ITEM", ITEM);
				}
			if (strUtils.fString(PRD_DESCRIP).length() > 0) {
				ht.put("b.ITEMDESC", PRD_DESCRIP);
			}
			invQryList = invUtil.getHQList(ht, PLANT, FROM_DATE,
					TO_DATE);

			//do_list = masterDetailDAO.MovementHistoryDetails(doNumber,customerNo,status,cfromdate,ctodate,userId,PLANT);
			if (invQryList.size() > 0) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment; filename=HQReport.xls");
%>

<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	<TR BGCOLOR="#000066">
		  <TH><font color="#ffffff" align="left"><b>Product ID</TH>
          <TH><font color="#ffffff" align="left"><b>Description</TH>
          <TH><font color="#ffffff" align="left"><b>Total Goods Receipt</TH>
           <TH><font color="#ffffff" align="left"><b>Total Goods Issue</TH>
             <TH><font color="#ffffff" align="left"><b>Total Goods Return</TH>
            <TH><font color="#ffffff" align="left"><b>Balance stock</TH>     

	</tr>

	<%
		int j = 0;
					String rowColor = "";

					Hashtable htship = new Hashtable();
					htship.put("PLANT", PLANT);
					Hashtable htexp = new Hashtable();

					for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {

						j = j + 1;
						Map lineArr = (Map) invQryList.get(iCnt);
						String trDate = "";
						int iIndex = iCnt + 1;
						// String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
						rowColor = ((j == 0) || (j % 2 == 0)) ? "#FFFFFF"
								: "#dddddd";
						htship.put("ITEM", (String) lineArr.get("ITEM"));
						//Get Total Stock in 

						RecvDetDAO recvdao = new RecvDetDAO();
						stockin = recvdao.getTotalStockIn(htship, "", "",
								"");

						for (int p = 0; p < stockin.size(); p++) {
							Map stockarr = (Map) stockin.get(p);
							STOCK_TOTAL = (String) stockarr.get("RECQTY");
							if (STOCK_TOTAL == null) {
								STOCK_TOTAL = "0";
							}
						}
						//Get stock sale by item
						saleList = shipdao.getStockSale(
								"sum(pickqty) as pickqty", htship, "");

						if (saleList.size() == 0)
							STOCK_SALE = "0";
						else {
							for (int k = 0; k < saleList.size(); k++) {
								Map salArr = (Map) saleList.get(k);
								STOCK_SALE = (String) salArr.get("pickqty");
							}
						}
                                                
                                       ArrayList rtnal=cusrtnDao.selectCustomerReturn("isnull(sum(qty),0) as returnqty",htship,"");
                                       if(rtnal.size()==0)
                                           TOTAL_RTN="0";
                                       else{
                                       for(int k=0;k<rtnal.size();k++)
                                       {
                                           Map salArr = (Map) rtnal.get(k);
                                           TOTAL_RTN = (String) salArr.get("returnqty"); 
                                       }
                                       }
                                      int STOCK_TOTAL_RTN = Integer.parseInt(TOTAL_RTN);
                                      STOCK_TOTAL_INT = Integer.parseInt(STOCK_TOTAL);
                                   // STOCK_EXPIRE_INT = invUtil.getCountofExpdt(htexp, PLANT, (String) lineArr.get("ITEM"), "","");
                                    STOCK_BALANCE_INT = ((STOCK_TOTAL_INT+STOCK_TOTAL_RTN)-Integer.parseInt(STOCK_SALE));//-STOCK_EXPIRE_INT;
	%>

	<TR bgcolor="<%=rowColor%>">

		<TD align="center"><%=(String) lineArr.get("ITEM")%></TD>
		<TD align="center"><%=(String) lineArr.get("ITEMDESC")%></TD>
		<TD align="center"><%= StrUtils.formatNum(STOCK_TOTAL)%></TD>
		<TD align="center"><%=StrUtils.formatNum(STOCK_SALE)%></TD>
                <TD align="center"><%=StrUtils.formatNum(TOTAL_RTN)%></TD>
		<TD align="center"><%=StrUtils.formatNum(String.valueOf(STOCK_BALANCE_INT))%></TD>

		<%
			}
		%>
	</TR>
</TABLE>

<%
	} else if (invQryList.size() < 1) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment; filename=HQReport.xls");

				out.println("No Records Found To List");
			}

		} catch (Exception e) {
		}
	}
%>