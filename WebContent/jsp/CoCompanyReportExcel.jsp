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

	String fieldDesc = "";
	String USERID = "", PLANT = "", LOC = "", ITEM = "", STOCK_SALE = "", STOCK_CLAIM = "", BATCH = "", PRD_TYPE_ID = "", CUSTOMER="",PRD_DESCRIP = "", STATUS = "", QTY = "", QTYALLOC = "", FROM_DATE = "", TO_DATE = "", fdate = "", tdate = "", xlaction = "",SUPPLIER="",BYSUPPLIER="";
	String html = "",sCustomerTypeId="";
	int Total = 0, STOCK_EXPIRE_INT = 0, STOCK_CLAIM_INT = 0, STOCK_FLOATING_INT = 0, STOCK_SALE_INT = 0;
	String SumColor = "", PRD_CLS_ID = "", PRD_CLS_ID1 = "";
	boolean flag = false;

	String PGaction = strUtils
			.fString(request.getParameter("PGaction")).trim();
	PLANT = session.getAttribute("PLANT").toString();
	USERID = session.getAttribute("LOGIN_USER").toString();
	LOC = strUtils.fString(request.getParameter("LOC"));
	ITEM = strUtils.fString(request.getParameter("ITEM"));
	CUSTOMER    = strUtils.fString(request.getParameter("CUSTOMER"));
	sCustomerTypeId  = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
	
	SUPPLIER    = StrUtils.fString(request.getParameter("SUPPLIER"));
	BYSUPPLIER = StrUtils.fString(request.getParameter("BYSUPPLIER"));

	PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
	FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
	if (FROM_DATE == null)
		FROM_DATE = "";
	else
		FROM_DATE = FROM_DATE.trim();

	ItemMstUtil itemMstUtil = new ItemMstUtil();

	boolean cntRec = false;

	xlaction = strUtils.fString(request.getParameter("xlAction"));

	if (xlaction.equalsIgnoreCase("GenerateXLSheetForCoCompanyReport")) {

		try {
			ITEM = itemMstUtil.isValidAlternateItemInItemmst(PLANT,
					ITEM);

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			if (strUtils.fString(PLANT).length() > 0)
				if (strUtils.fString(ITEM).length() > 0) {
					ht.put("ITEM", ITEM);
				}
			
			 if(strUtils.fString(CUSTOMER).length() > 0)      
                          {
                            ht.put("CNAME",CUSTOMER);
                          }
			 if(strUtils.fString(sCustomerTypeId).length() > 0)      
		      {
		        	ht.put("CUSTTYPE",sCustomerTypeId);
		      }
			
			
			if(BYSUPPLIER.equalsIgnoreCase("1")){
	        	   ht.remove("PLANT");
	        	   ht.remove("ITEM");
	        	   ht.remove("CNAME");
	        	   ht.remove("CUSTTYPE");
		           if(StrUtils.fString(SUPPLIER).length() > 0){ht.put("R.CNAME",StrUtils.InsertQuotes(SUPPLIER));}
		           if(StrUtils.fString(ITEM).length() > 0){ht.put("A.ITEM",ITEM);}
	        	   ht.put("R.PLANT",PLANT);
	           	   invQryList = shipdao.getTotalSupplierStockSalestWithDates(ht,FROM_DATE,TO_DATE,PRD_DESCRIP,"");
			}else{
				invQryList = shipdao.getTotalStockSaleLstWithDates(ht, FROM_DATE,TO_DATE,PRD_DESCRIP,"");
			}

			if (invQryList.size() > 0) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment; filename=SalesOrderSummaryByCustomer.xls");
%>

<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	<TR BGCOLOR="#000066">
			
		 <TH><font color="#ffffff" align="left"><b>Product ID</TH>
          <TH><font color="#ffffff" align="left"><b>Description</TH>
          <TH><font color="#ffffff" align="left"><b>Sale to CO-Company</TH>
          <TH><font color="#ffffff" align="left"><b>Total Stock Sale</TH>
	</tr>

	<%
		int j = 0;
					String rowColor = "";

					Hashtable htship = new Hashtable();
					htship.put("PLANT", PLANT);
					Hashtable htexp = new Hashtable();
					int total_stock_claim = 0;
					if(invQryList.size()>0){
					double totalQty = 0;
					for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {

						j = j + 1;
						Map lineArr = (Map) invQryList.get(iCnt);
						String trDate = "";
						int iIndex = iCnt + 1;
						// String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
						rowColor = ((j == 0) || (j % 2 == 0)) ? "#FFFFFF"
								: "#dddddd";
						htship.put("ITEM", (String) lineArr.get("ITEM"));
						htship.put("USERFLD2", (String) lineArr.get("CUSTCODE"));
						if(strUtils.fString(CUSTOMER).length() > 0)      
		                {
		                	htship.put("CUSTNAME",CUSTOMER);
		                }
						totalQty=totalQty+Double.valueOf((String)lineArr.get("PICKQTY"));
	%>

	<TR bgcolor="<%=rowColor%>">

	  <TD align= "center"><%=(String)lineArr.get("ITEM") %></TD>
              <TD align= "center"><%=(String)lineArr.get("ITEMDESC")%></TD>
              <TD align= "center"><%=(String)lineArr.get("CNAME")%></TD>
              <TD align= "center"><%=StrUtils.formatNum((String)lineArr.get("PICKQTY"))%></TD>
		<%
			}
		%>
	</TR>
	<TR bgcolor="<%=rowColor%>">

	  <TD align= "center"></TD>
      <TD align= "center"></TD>
      <TD align= "center">Total</TD>
      <TD align= "center"><%=totalQty%></TD>
	</TR>
		<%
			}
		%>
	
	
</TABLE>



</FORM>

<%
	} else if (invQryList.size() < 1) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment; filename=SalesOrderSummaryByCustomer.xls");

				out.println("No Records Found To List");
			}

		} catch (Exception e) {
		}
	}
%>