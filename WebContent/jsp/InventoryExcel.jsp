
<%@ page import="java.util.*"%>
<%@ page import="com.track.dao.*"%>
<%
	java.util.List do_list = new java.util.ArrayList();
	com.track.util.StrUtils strUtils = new com.track.util.StrUtils();
	com.track.util.DateUtils dateUtils = new com.track.util.DateUtils();
	com.track.db.util.InvUtil invUtil = new com.track.db.util.InvUtil();
	com.track.db.util.BOMUtil bomUtil = new com.track.db.util.BOMUtil();
	java.util.Hashtable ht = new java.util.Hashtable();

	java.util.Hashtable htsql = new java.util.Hashtable();
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	//Start code added by Deen for base Currency inclusion on Aug 15 2012 
	String baseCurrency = (String) session.getAttribute("BASE_CURRENCY");
	//End code added by Deen for base Currency inclusion on Aug 15 2012 
	String LOC = "", ITEM = "",EXPIREDATE="", STATUS = "", QTY = "", BATCH = "", PRD_TYPE_ID = "",PRD_BRAND_ID="", PRD_CLS_ID = "", QTYALLOC = "", xlaction = "", FROM_DATE = "", TO_DATE = "", fdate = "", tdate = "";
	String PRD_DESCRIP = "",CURRENCYID="",CURRENCYDISPLAY="";
	String PRD_DESCRIP_PARENT = "", PARENTBATCH = "", CHILDITEM = "", CHILDBATCH = "",LOC_TYPE_ID="";
	boolean isWithZero = false;
	ArrayList listqry = new ArrayList();
	LOC = strUtils.fString(request.getParameter("LOC"));
	ITEM = strUtils.fString(request.getParameter("ITEM"));
	xlaction = strUtils.fString(request.getParameter("xlAction"));
	BATCH = strUtils.fString(request.getParameter("BATCH"));
	FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
	PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
	PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	 // Start code added by Deen for product brand on 11/9/12 
	PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	 // End code added by Deen for product brand on 11/9/12 
	EXPIREDATE = strUtils.fString(request.getParameter("EXPIREDATE"));
	String minQty = strUtils.fString(request.getParameter("MINQTY"));
	CURRENCYID= strUtils.fString(request.getParameter("CURRENCYID"));
	CURRENCYDISPLAY= strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
	String withZero = strUtils.fString(request.getParameter("WITHZERO"));
	PARENTBATCH = strUtils.fString(request.getParameter("PARENTBATCH"));
	CHILDITEM = strUtils.fString(request.getParameter("CHILDITEM"));
	CHILDBATCH = strUtils.fString(request.getParameter("CHILDBATCH"));
	PRD_DESCRIP_PARENT = strUtils.fString(request.getParameter("PRD_DESCRIP_PARENT"));
	PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
	STATUS = strUtils.fString(request.getParameter("STATUS"));
	LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));

	if (FROM_DATE == null)
		FROM_DATE = "";
	else
		FROM_DATE = FROM_DATE.trim();
	if (FROM_DATE.length() > 5)

		fdate = FROM_DATE.substring(6) + "-"
				+ FROM_DATE.substring(3, 5) + "-"
				+ FROM_DATE.substring(0, 2);

	if (TO_DATE == null)
		TO_DATE = "";
	else
		TO_DATE = TO_DATE.trim();
	if (TO_DATE.length() > 5)
		tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5)
				+ "-" + TO_DATE.substring(0, 2);
	
	
	
	

	if (minQty.length() == 0) {
		minQty = "N";
	} else {
		minQty = "Y";
	}

	if (withZero.length() > 0) {
		isWithZero = true;
	}

	if (xlaction.equalsIgnoreCase("GenerateXLSheet")) {
		try {
			if (strUtils.fString(ITEM).length() > 0)
				htsql.put("b.ITEM", ITEM);
	
			if (strUtils.fString(PRD_CLS_ID).length() > 0)
				htsql.put("b.PRD_CLS_ID", PRD_CLS_ID);
			if (strUtils.fString(PRD_TYPE_ID).length() > 0)
				htsql.put("b.itemtype", PRD_TYPE_ID);
			 // Start code added by Deen for product brand on 11/9/12 
			if (strUtils.fString(PRD_BRAND_ID).length() > 0)
				htsql.put("b.PRD_BRAND_ID", PRD_BRAND_ID);
			 // End code added by Deen for product brand on 11/9/12 
			
			if (minQty.equalsIgnoreCase("Y")) {
				Hashtable htQuery = new Hashtable();

				if (strUtils.fString(plant).length() > 0)
					htQuery.put("C.PLANT", plant);
				if (strUtils.fString(ITEM).length() > 0) {
					htQuery.put("C.ITEM", ITEM);
				}

				//if (strUtils.fString(LOC).length() > 0)
					//htQuery.put("D.LOC", LOC);
				
				if (strUtils.fString(PRD_CLS_ID).length() > 0)
					htQuery.put("C.PRD_CLS_ID", PRD_CLS_ID);

				if (strUtils.fString(PRD_TYPE_ID).length() > 0)
					htQuery.put("C.ITEMTYPE", PRD_TYPE_ID);
				 // Start code added by Deen for product brand on 11/9/12 
				if (strUtils.fString(PRD_BRAND_ID).length() > 0)
					htQuery.put("C.PRD_BRAND_ID", PRD_BRAND_ID);
				 // End code added by Deen for product brand on 11/9/12 
				do_list = invUtil.getInvListSummaryForItemMinStockQtyForExcel(
								htQuery, plant, PRD_DESCRIP, isWithZero,LOC_TYPE_ID,LOC);
			} else {
				
				/* if(LOC_TYPE_ID.length() > 0){
                     
                     do_list = invUtil.getInvListSummaryWithMinQtyWithLocType(htsql, plant, ITEM,
     						LOC,  PRD_CLS_ID,PRD_DESCRIP, isWithZero);
					
			     }
                 else
                 {*/
            	   do_list = invUtil.getInvListSummaryWithMinQty(htsql, plant, ITEM,
   						LOC,  PRD_CLS_ID,PRD_DESCRIP, isWithZero,LOC_TYPE_ID);
               	
                // }
				
				
			}
			if (do_list.size() > 0) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment; filename=InventoryExcel.xls");
				java.util.Vector v = null;
%>
          
<%@page import="com.track.util.StrUtils"%><TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
       	 <TH><font color="#ffffff" align="left"><b>Product ID</TH>
       	   <TH><font color="#ffffff" align="left"><b>Loc</TH>
          <TH><font color="#ffffff" align="left"><b>Product Class ID</TH>
           <TH><font color="#ffffff" align="left"><b>Product Type ID</TH>
            <TH><font color="#ffffff" align="left"><b>Product Brand ID</TH>
         <TH><font color="#ffffff" align="left"><b>Description</TH>
         <TH><font color="#ffffff" align="left"><b>UOM</TH>
        
           <TH><font color="#ffffff" align="left"><b>MinQty</TH>
          <TH><font color="#ffffff" align="left"><b>Quantity</TH>
          
       <!--  out.println("Product ID\tProduct Class ID\tProduct Type ID\tProduct Description\tLocation\tBatch\tExpire Date\tQty");-->
       </tr>
	
	<%
			for (int i = 0; i < do_list.size(); i++) {
							

							try {
								Map do_list1 = (Map) do_list.get(i);

								int iIndex = i + 1;
								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"
										: "#dddddd";
		%>
           <TR bgcolor = "<%=bgcolor%>">
 
             <TD align= "center"><%=(String) do_list1.get("ITEM")%></TD>
              <TD align= "center"><%=(String) do_list1.get("LOC")%></TD>
             <TD align= "center"><%=(String) do_list1.get("PRDCLSID")%></TD>
             <TD align= "center"><%=(String) do_list1.get("ITEMTYPE")%></TD>
             <TD align= "center"><%=(String) do_list1.get("PRD_BRAND_ID")%></TD>
              <TD align= "center"><%=(String) do_list1.get("ITEMDESC")%></TD>
              <TD align= "center"><%=(String) do_list1.get("STKUOM")%></TD>
             
              <TD align= "center"><%=(String) do_list1.get("STKQTY")%></TD>
              <TD align= "center" ><%=StrUtils.formatNum((String) do_list1.get("QTY"))%></TD>
      
         </TR>
          <%
          	// out.println((String)do_list1.get("ITEM")+"\t"+(String)do_list1.get("PRDCLSID")+"\t"+(String)do_list1.get("ITEMTYPE")+"\t"+ (String)do_list1.get("ITEMDESC") +"\t"+(String)do_list1.get("LOC")+"\t" +(String)do_list1.get("BATCH")+"\t"+(String)do_list1.get("EXPIREDATE")+"\t"+(String)do_list1.get("QTY"));
          					} catch (Exception ee) {
          						System.out
          								.println("######################## MovementExcel ################ :"
          										+ ee);
          					}
          				}
          %></TABLE>
          <%
          	} else if (do_list.size() < 1) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");

          				out.println("No Records Found To List");
          			}

          		} catch (Exception e) {
          		}
          	} else if (xlaction.equalsIgnoreCase("GenerateXLSheet_WithPrice")) { // Not in Use
          		try {

          			if (strUtils.fString(ITEM).length() > 0)
          				htsql.put("b.ITEM", ITEM);
          			if (strUtils.fString(LOC).length() > 0)
          				htsql.put("a.LOC", LOC);
          			if (strUtils.fString(BATCH).length() > 0)
          				htsql.put("a.USERFLD4", BATCH);
          			if (strUtils.fString(PRD_CLS_ID).length() > 0)
          				htsql.put("b.PRD_CLS_ID", PRD_CLS_ID);
          			if (strUtils.fString(PRD_TYPE_ID).length() > 0)
          				htsql.put("b.itemtype", PRD_TYPE_ID);
          			
          			do_list = invUtil.getInvListSummaryWithPrice(htsql, plant,
          					ITEM, LOC, BATCH, PRD_CLS_ID, PRD_DESCRIP);

          			if (do_list.size() > 0) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");
          				java.util.Vector v = null;
          %>
          <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
            <TH><font color="#ffffff" align="left"><b>Product ID</TH>
            <TH><font color="#ffffff" align="left"><b>Loc</TH>
            <TH><font color="#ffffff" align="left"><b>Product Class ID</TH>
            <TH><font color="#ffffff" align="left"><b>Product Type ID</TH>
            <TH><font color="#ffffff" align="left"><b>Description</TH>
            <TH><font color="#ffffff" align="left"><b>UOM</TH>
            <TH><font color="#ffffff" align="left"><b>Batch</TH>
            <TH><font color="#ffffff" align="left"><b>Unit Price</TH>
            <TH><font color="#ffffff" align="left"><b>Qty</TH>
            <TH><font color="#ffffff" align="left"><b>Total Price</TH>
          
       <!--  out.println("Product ID\tProduct Class ID\tProduct Type ID\tProduct Description\tLocation\tBatch\tExpire Date\tQty");-->
       </tr>
	
	<%
			int qty = 0;
						int Total = 0;
						float unitprice = 0;
						float totqty = 0;
						float TotalPrice = 0;
						for (int i = 0; i < do_list.size(); i++) {
						

							try {
								Map do_list1 = (Map) do_list.get(i);
								TotalPrice = 0;
								int iIndex = i + 1;
								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"
										: "#dddddd";
								unitprice = Float.parseFloat(((String) do_list1
										.get("UNITPRICE").toString()));
								totqty = Float.parseFloat(((String) do_list1
										.get("QTY").toString()));
								Total = Total
										+ Integer.parseInt(((String) do_list1
												.get("QTY").toString()));
								TotalPrice = (unitprice * totqty);
		%>
           <TR bgcolor = "<%=bgcolor%>">
 
              <TD align= "center"><%=(String) do_list1.get("ITEM")%></TD>
              <TD align= "center"><%=(String) do_list1.get("LOC")%></TD>
              <TD align= "center"><%=(String) do_list1.get("PRDCLSID")%></TD>
              <TD align= "center"><%=(String) do_list1.get("ITEMTYPE")%></TD>
              <TD align= "center"><%=(String) do_list1.get("ITEMDESC")%></TD>
              <TD align= "center"><%=(String) do_list1.get("STKUOM")%></TD>
              
              <TD align= "center"><%=(String) do_list1.get("BATCH")%></TD>
              <TD align= "center"><%=StrUtils.currencyWtoutSymbol((String) do_list1.get("UNITPRICE"))%></TD>
              <TD align= "center" ><%=StrUtils.formatNum((String) do_list1.get("QTY"))%></TD>
              <TD align= "center" ><%=StrUtils.currencyWtoutSymbol(String.valueOf(TotalPrice))%></TD>
         </TR>
          <%
          	// out.println((String)do_list1.get("ITEM")+"\t"+(String)do_list1.get("PRDCLSID")+"\t"+(String)do_list1.get("ITEMTYPE")+"\t"+ (String)do_list1.get("ITEMDESC") +"\t"+(String)do_list1.get("LOC")+"\t" +(String)do_list1.get("BATCH")+"\t"+(String)do_list1.get("EXPIREDATE")+"\t"+(String)do_list1.get("QTY"));
          					} catch (Exception ee) {
          						System.out
          								.println("######################## MovementExcel ################ :"
          										+ ee);
          					}
          				}
          %></TABLE>
          <%
          	} else if (do_list.size() < 1) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");

          				out.println("No Records Found To List");
          			}

          		} catch (Exception e) {
          		}
          	} 
          	 else if (xlaction.equalsIgnoreCase("GenerateInvXLOCBal")) { 
           		try {
           			
           		 
			        if(strUtils.fString(PRD_CLS_ID).length() > 0)         htsql.put("PRD_CLS_ID",PRD_CLS_ID);
					if(strUtils.fString(PRD_TYPE_ID).length() > 0)        htsql.put("itemtype",PRD_TYPE_ID); 
					if(strUtils.fString(PRD_BRAND_ID).length() > 0)        htsql.put("PRD_BRAND_ID",PRD_BRAND_ID);  
                    
					 fdate ="";tdate="";
	                if (FROM_DATE.length()>5)
	                       fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
	                       
	                if (TO_DATE.length()>5){
	                        tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
	                }else{
	                	tdate = dateUtils.getDateFormatyyyyMMdd();
	                }
           			
           			do_list = invUtil.getInvWithOpenCloseBal( plant,ITEM,  PRD_DESCRIP,htsql,fdate,tdate);
           			
           			if (do_list.size() > 0) {
           				response.setContentType("application/vnd.ms-excel");
           				response.setHeader("Content-disposition",
           						"attachment; filename=InventoryExcelOCBal.xls");
           				java.util.Vector v = null;
           %>
           <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    
                    <TR BGCOLOR="#000066">
                    <TH><font color="#ffffff" align="left"><b>Product ID</TH>
                    <TH><font color="#ffffff" align="left"><b>Description</TH>
                    <TH><font color="#ffffff" align="left"><b>Product Class ID</TH>
                    <TH><font color="#ffffff" align="left"><b>Product Type ID</TH>
                    <TH><font color="#ffffff" align="left"><b>Product Brand ID</TH>
                    <TH><font color="#ffffff" align="left"><b>Opening Qty</TH>
                    <TH><font color="#ffffff" align="left"><b>Total Recved</TH>
                    <TH><font color="#ffffff" align="left"><b>Total Issued</TH>
                    <TH><font color="#ffffff" align="left"><b>Closing Qty</TH>
                    <TH><font color="#ffffff" align="left"><b>Later Recv</TH>
                    <TH><font color="#ffffff" align="left"><b>Issue</TH>
                    <TH><font color="#ffffff" align="Right"><b>Stock On Hand</TH>
                    
   
        </tr>
 	
 	<%
 		
 						for (int i = 0; i < do_list.size(); i++) {
 						

 							try {
 								Map do_list1 = (Map) do_list.get(i);
 								
 								int iIndex = i + 1;
 								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"
 										: "#dddddd";
 								
 		%>
            <TR bgcolor = "<%=bgcolor%>">
  
				<TD align= "center"><%=(String) do_list1.get("ITEM")%></TD>
				<TD align= "center"><%=(String) do_list1.get("ITEMDESC")%></TD>
				<TD align= "center"><%=(String) do_list1.get("PRD_CLS_ID")%></TD>
				<TD align= "center"><%=(String) do_list1.get("ITEMTYPE")%></TD>
				<TD align= "center"><%=(String) do_list1.get("PRD_BRAND_ID")%></TD>
				<TD align= "center"><%=(String) do_list1.get("OPENING")%></TD>
				<TD align= "center"><%=(String) do_list1.get("TOTRECV")%></TD>
				<TD align= "center"><%=(String) do_list1.get("TOTAL_ISS")%></TD>
				<TD align= "center"><%=(String) do_list1.get("CLOSING")%></TD>
				<TD align= "center"><%=(String) do_list1.get("RECVED_AFTER")%></TD>
				<TD align= "center"><%=(String) do_list1.get("ISSUED_AFTER")%></TD>
				<TD align= "center"><%=(String) do_list1.get("STOCKONHAND")%></TD>
               
          </TR>
           <%
          
           					} catch (Exception ee) {
           						System.out
           								.println("######################## MovementExcel ################ :"
           										+ ee);
           					}
           				}
           %></TABLE>
           <%
           	} else if (do_list.size() < 1) {
           				response.setContentType("application/vnd.ms-excel");
           				response.setHeader("Content-disposition",
           						"attachment; filename=InventoryExcelOCBal.xls");

           				out.println("No Records Found To List");
           			}

           		} catch (Exception e) {
           		}
           	} 
          	else if (xlaction.equalsIgnoreCase("GenerateXLSheet_WithCost")) {
          		try {
             			     			
          			if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
          			if (FROM_DATE.length()>5)
          				fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
          			if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
          			if (TO_DATE.length()>5)
          				tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
          			
          			RecvDetDAO _RecvDetDAO     = new RecvDetDAO();
          			
          			if (strUtils.fString(ITEM).length() > 0)
          				htsql.put("b.ITEM", ITEM);
          			//if (strUtils.fString(LOC).length() > 0)
          				//htsql.put("a.LOC", LOC);
          			if (strUtils.fString(BATCH).length() > 0)
          				htsql.put("a.USERFLD4", BATCH);
          			if (strUtils.fString(PRD_CLS_ID).length() > 0)
          				htsql.put("b.PRD_CLS_ID", PRD_CLS_ID);
          			if (strUtils.fString(PRD_TYPE_ID).length() > 0)
          				htsql.put("b.itemtype", PRD_TYPE_ID);
          			 // Start code added by Deen for product brand on 11/9/12 
          			if (strUtils.fString(PRD_BRAND_ID).length() > 0)
          				htsql.put("b.PRD_BRAND_ID", PRD_BRAND_ID);
          			 // End code added by Deen for product brand on 11/9/12 
          			 
          			CurrencyDAO _CurrencyDAO=new CurrencyDAO();
          		    String strCurrencyCode= _CurrencyDAO.getCurrencyCode(plant,CURRENCYDISPLAY);
          	  		
          			do_list = invUtil.getInvListSummaryWithAverageCost(htsql,fdate,tdate,plant,
          					ITEM,  PRD_DESCRIP,strCurrencyCode,baseCurrency,LOC_TYPE_ID,LOC);
          					
          			

          			if (do_list.size() > 0) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition","attachment; filename=InventoryExcel.xls");
          				java.util.Vector v = null;
             %>
				          <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
				          <TR BGCOLOR="#000066">
					       	  <TH><font color="#ffffff" align="left"><b>Product ID</TH>
					       	  <TH><font color="#ffffff" align="left"><b>Loc</TH>
					          <TH><font color="#ffffff" align="left"><b>Product Class ID</TH>
					          <TH><font color="#ffffff" align="left"><b>Product Type ID</TH>
					           <TH><font color="#ffffff" align="left"><b>Product Brand ID</TH>
					          <TH><font color="#ffffff" align="left"><b>Description</TH>
					          <TH><font color="#ffffff" align="left"><b>UOM</TH>
					          <TH><font color="#ffffff" align="left"><b>Batch</TH>
					          <TH><font color="#ffffff" align="left"><b>Average Unit Cost</TH>
					          <TH><font color="#ffffff" align="left"><b>List Price</TH>
					          <TH><font color="#ffffff" align="left"><b>Qty</TH>
					          <TH><font color="#ffffff" align="left"><b>Total Cost</TH>
					          <TH><font color="#ffffff" align="left"><b>Total Price</TH>
				          </tr>
	        	<%    	
          			
						double Total = 0;
						double TotalCost = 0;
						double TotalPrice = 0;
						
						for (int i = 0; i < do_list.size(); i++) {
							try {
								Map do_list1 = (Map) do_list.get(i);
								TotalPrice = 0;
								int iIndex = i + 1;
								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"	: "#dddddd";
								   
				                 TotalCost=Double.parseDouble(((String)do_list1.get("AVERAGE_COST").toString()))* Double.parseDouble(((String)do_list1.get("QTY").toString())) ;
				                 TotalPrice=Double.parseDouble(((String)do_list1.get("LIST_PRICE").toString()))* Double.parseDouble(((String)do_list1.get("QTY").toString())) ;
					               
		  %>
				              <TR bgcolor = "<%=bgcolor%>">
				            
				 	              <TD align= "center"><%=(String) do_list1.get("ITEM")%></TD>
					              <TD align= "center"><%=(String) do_list1.get("LOC")%></TD>
					              <TD align= "center"><%=(String) do_list1.get("PRDCLSID")%></TD>
					              <TD align= "center"><%=(String) do_list1.get("ITEMTYPE")%></TD>
					                <TD align= "center"><%=(String) do_list1.get("PRD_BRAND_ID")%></TD>
					              <TD align= "center"><%=(String) do_list1.get("ITEMDESC")%></TD>
					              <TD align= "center"><%=(String) do_list1.get("STKUOM")%></TD>
					         
     				               <TD align= "center"><%=(String) do_list1.get("BATCH")%></TD>
					              <TD align= "center" ><%=StrUtils.currencyWtoutSymbol((String)do_list1.get("AVERAGE_COST")) %></TD>
					               <TD align= "center" ><%=StrUtils.currencyWtoutSymbol((String)do_list1.get("LIST_PRICE")) %></TD>
					              <TD align= "center" ><%=StrUtils.formatNum((String) do_list1.get("QTY"))%></TD>
					              <TD align= "center" ><%=StrUtils.currencyWtoutSymbol(String.valueOf(TotalCost))%></TD>
					               <TD align= "center" ><%=StrUtils.currencyWtoutSymbol(String.valueOf(TotalPrice))%></TD>
					              
				             </TR>
          <%
          	
          					} catch (Exception ee) {
          						System.out.println("######################## MovementExcel ################ :"
          										+ ee);
          					}
          				}
						
					
          %></TABLE>
          <%
          	} else if (do_list.size() < 1) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");
          				out.println("No Records Found To List");
          			}
                 
             //    } 
               } catch (Exception e) {
          		
          		
                 }}else if(xlaction.equalsIgnoreCase("GenerateXLSheet_WithExpireDate")) {
          		try {

          			if (strUtils.fString(ITEM).length() > 0)
          				htsql.put("b.ITEM", ITEM);
          			//if (strUtils.fString(LOC).length() > 0)
          				//htsql.put("a.LOC", LOC);
          			if (strUtils.fString(BATCH).length() > 0)
          				htsql.put("a.USERFLD4", BATCH);
          			
          			if (strUtils.fString(PRD_CLS_ID).length() > 0)
          				htsql.put("b.PRD_CLS_ID", PRD_CLS_ID);
          			if (strUtils.fString(PRD_TYPE_ID).length() > 0)
          				htsql.put("b.itemtype", PRD_TYPE_ID);
          			 // Start code added by Deen for product brand on 11/9/12 
          			if (strUtils.fString(PRD_BRAND_ID).length() > 0)
          				htsql.put("b.PRD_BRAND_ID", PRD_BRAND_ID);
          			 // End code added by Deen for product brand on 11/9/12 
          		//Commented by Samatha to use same method instead of another method for calling to generate to excel	
          		/*	do_list = invUtil.getInvListSummaryWithExpireDateCondition(htsql,
          					plant, ITEM, LOC, BATCH, PRD_CLS_ID, PRD_DESCRIP,EXPIREDATE,LOC_TYPE_ID);*/
          			  do_list=invUtil.getInvListSummaryWithExpireDate(htsql,plant,ITEM,PRD_DESCRIP,"",EXPIREDATE,LOC_TYPE_ID,"",LOC,"","");    


          			if (do_list.size() > 0) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");
          				java.util.Vector v = null;
          %>
          <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
       	  <TH><font color="#ffffff" align="left"><b>Product ID</TH>
       	  <TH><font color="#ffffff" align="left"><b>Loc</TH>
          <TH><font color="#ffffff" align="left"><b>Product Class ID</TH>
          <TH><font color="#ffffff" align="left"><b>Product Type ID</TH>
          <TH><font color="#ffffff" align="left"><b>Product Brand ID</TH>
          <TH><font color="#ffffff" align="left"><b>Description</TH>
          <TH><font color="#ffffff" align="left"><b>UOM</TH>
          
          <TH><font color="#ffffff" align="left"><b>Batch</TH>
           <TH><font color="#ffffff" align="left"><b>Expiry Date</TH>
          <TH><font color="#ffffff" align="left"><b>Quantity</TH>
          
       <!--  out.println("Product ID\tProduct Class ID\tProduct Type ID\tProduct Description\tLocation\tBatch\tExpire Date\tQty");-->
       </tr>
	
	<%
			for (int i = 0; i < do_list.size(); i++) {
							

							try {
								Map do_list1 = (Map) do_list.get(i);

								int iIndex = i + 1;
								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"
										: "#dddddd";
		%>
           <TR bgcolor = "<%=bgcolor%>">
 
              <TD align= "center"><%=(String) do_list1.get("ITEM")%></TD>
               <TD align= "center"><%=(String) do_list1.get("LOC")%></TD>
              <TD align= "center"><%=(String) do_list1.get("PRDCLSID")%></TD>
              <TD align= "center"><%=(String) do_list1.get("ITEMTYPE")%></TD>
              <TD align= "center"><%=(String) do_list1.get("PRD_BRAND_ID")%></TD>
              <TD align= "center"><%=(String) do_list1.get("ITEMDESC")%></TD>
              <TD align= "center"><%=(String) do_list1.get("STKUOM")%></TD>
             
              <TD align= "center"><%=(String) do_list1.get("BATCH")%></TD>
              <TD align= "center"><%=(String) do_list1.get("EXPIREDATE")%></TD>
              <TD align= "center"><%=StrUtils.formatNum((String) do_list1.get("QTY"))%></TD>
      
         </TR>
          <%
          	// out.println((String)do_list1.get("ITEM")+"\t"+(String)do_list1.get("PRDCLSID")+"\t"+(String)do_list1.get("ITEMTYPE")+"\t"+ (String)do_list1.get("ITEMDESC") +"\t"+(String)do_list1.get("LOC")+"\t" +(String)do_list1.get("BATCH")+"\t"+(String)do_list1.get("EXPIREDATE")+"\t"+(String)do_list1.get("QTY"));
          					} catch (Exception ee) {
          						System.out
          								.println("######################## MovementExcel ################ :"
          										+ ee);
          					}
          				}
          %></TABLE>
          <%
          	} else if (do_list.size() < 1) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");

          				out.println("No Records Found To List");
          			}

          		} catch (Exception e) {
          		}
          	} else if (xlaction.equalsIgnoreCase("GenerateXLSheet_WithOutPriceQty")) {
          		try {

          			if (strUtils.fString(ITEM).length() > 0)
          				htsql.put("b.ITEM", ITEM);
          			//if (strUtils.fString(LOC).length() > 0)
          				//htsql.put("a.LOC", LOC);
          			if (strUtils.fString(BATCH).length() > 0)
          				htsql.put("a.USERFLD4", BATCH);
          			if (strUtils.fString(PRD_CLS_ID).length() > 0)
          				htsql.put("b.PRD_CLS_ID", PRD_CLS_ID);
          			if (strUtils.fString(PRD_TYPE_ID).length() > 0)
          				htsql.put("b.itemtype", PRD_TYPE_ID);
          			 // Start code added by Deen for product brand on 11/9/12 
          			if (strUtils.fString(PRD_BRAND_ID).length() > 0)
          				htsql.put("b.PRD_BRAND_ID", PRD_BRAND_ID);
          			 // End code added by Deen for product brand on 11/9/12 

          			do_list = invUtil.getInvListSummaryWithOutPrice(htsql, plant,ITEM,  PRD_DESCRIP,LOC_TYPE_ID,"","",LOC);

          			if (do_list.size() > 0) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");
          				java.util.Vector v = null;
          %>
          <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
       	 <TH><font color="#ffffff" align="left"><b>Product ID</TH>
       	  <TH><font color="#ffffff" align="left"><b>Loc</TH>
          <TH><font color="#ffffff" align="left"><b>Product Class ID</TH>
          <TH><font color="#ffffff" align="left"><b>Product Type ID</TH>
           <TH><font color="#ffffff" align="left"><b>Product BRAND ID</TH>
          <TH><font color="#ffffff" align="left"><b>Description</TH>
          <TH><font color="#ffffff" align="left"><b>UOM</TH>
         
          <TH><font color="#ffffff" align="left"><b>Batch</TH>
          <TH><font color="#ffffff" align="left"><b>Quantity</TH>
      
          
       </tr>
	
	<%
			for (int i = 0; i < do_list.size(); i++) {
							

							try {
								Map do_list1 = (Map) do_list.get(i);

								int iIndex = i + 1;
								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"
										: "#dddddd";
		%>
           <TR bgcolor = "<%=bgcolor%>">
 
              <TD align= "center"><%=(String) do_list1.get("ITEM")%></TD>
              <TD align= "center"><%=(String) do_list1.get("LOC")%></TD>
              <TD align= "center"><%=(String) do_list1.get("PRDCLSID")%></TD>
              <TD align= "center"><%=(String) do_list1.get("ITEMTYPE")%></TD>
               <TD align= "center"><%=(String) do_list1.get("PRD_BRAND_ID")%></TD>
              <TD align= "center"><%=(String) do_list1.get("ITEMDESC")%></TD>
              <TD align= "center"><%=(String) do_list1.get("STKUOM")%></TD>
              
              <TD align= "center"><%=(String) do_list1.get("BATCH")%></TD>
              <TD align= "center"><%=StrUtils.formatNum((String) do_list1.get("QTY"))%></TD>
      
         </TR>
          <%
          	} catch (Exception ee) {
          						System.out
          								.println("######################## MovementExcel ################ :"
          										+ ee);
          					}
          				}
          %></TABLE>
          <%
          	} else if (do_list.size() < 1) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");

          				out.println("No Records Found To List");
          			}

          		} catch (Exception e) {
          		}
          	}

          	else if (xlaction.equalsIgnoreCase("GenerateInvXLGroupByProd")) {
          		try {
          			if (strUtils.fString(PRD_CLS_ID).length() > 0)
          				htsql.put("b.PRD_CLS_ID", PRD_CLS_ID);
          			if (strUtils.fString(PRD_TYPE_ID).length() > 0)
          				htsql.put("b.itemtype", PRD_TYPE_ID);
          			 // Start code added by Deen for product brand on 11/9/12 
          			if (strUtils.fString(PRD_BRAND_ID).length() > 0)
          				htsql.put("b.PRD_BRAND_ID", PRD_BRAND_ID);
          			 // End code added by Deen for product brand on 11/9/12 
          			do_list = invUtil.getInvListSummaryGroupByProd(plant, ITEM,
          					PRD_DESCRIP, htsql,LOC,LOC_TYPE_ID,"","");

          			if (do_list.size() > 0) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");
          				java.util.Vector v = null;
          %>
          <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
        <TR BGCOLOR="#000066">
        <TH><font color="#ffffff" align="left"><b>Location</TH>
		<TH><font color="#ffffff" align="left"><b>Product ID</TH>
		<TH><font color="#ffffff" align="left"><b>Product Class ID</TH>
        <TH><font color="#ffffff" align="left"><b>Product Type ID</TH>
        <TH><font color="#ffffff" align="left"><b>Product BRAND ID</TH>
        <TH><font color="#ffffff" align="left"><b>Description</TH>
		<TH><font color="#ffffff" align="left"><b>Quantity</TH>
		<TH><font color="#ffffff" align="left"><b>UOM</TH>


	</tr>
	
	<%
			for (int i = 0; i < do_list.size(); i++) {

							try {
								Map do_list1 = (Map) do_list.get(i);

								int iIndex = i + 1;
								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"
										: "#dddddd";
		%>
           <TR bgcolor = "<%=bgcolor%>">
 			   <TD align= "center"><%=(String) do_list1.get("LOC")%></TD>
              <TD align= "center">&nbsp;<%=(String) do_list1.get("ITEM")%></TD>
              <TD align= "center"><%=(String) do_list1.get("PRDCLSID")%></TD>
              <TD align= "center"><%=(String) do_list1.get("ITEMTYPE")%></TD>
               <TD align= "center"><%=(String) do_list1.get("PRD_BRAND_ID")%></TD>
              <TD align= "center"><%=(String) do_list1.get("ITEMDESC")%></TD>
              <TD align= "center"><%=StrUtils.formatNum((String) do_list1.get("QTY"))%></TD>
              <TD align= "center"><%=(String) do_list1.get("STKUOM")%></TD>
              
         </TR>
          <%
          	} catch (Exception ee) {
          						System.out
          								.println("######################## MovementExcel ################ :"
          										+ ee);
          					}
          				}
          %></TABLE>
          <%
          	} else if (do_list.size() < 1) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");

          				out.println("No Records Found To List");
          			}

          		} catch (Exception e) {
          		}
          	}

          	//view kitting
          	else if (xlaction.equalsIgnoreCase("GenerateXLSheet_Kitting")) {
          		try {

          			if (strUtils.fString(ITEM).length() > 0)
          				htsql.put("A.PARENT_PRODUCT", ITEM);
          			if (strUtils.fString(PARENTBATCH).length() > 0)
          				htsql.put("A.PARENT_PRODUCT_BATCH", PARENTBATCH);
          			if (strUtils.fString(CHILDITEM).length() > 0)
          				htsql.put("A.CHILD_PRODUCT", CHILDITEM);
          			if (strUtils.fString(CHILDBATCH).length() > 0)
          				htsql.put("A.CHILD_PRODUCT_BATCH", CHILDBATCH);
          			if (strUtils.fString(STATUS).length() > 0)
          				htsql.put("A.STATUS", STATUS);
          			
          			/*if (strUtils.fString(LOC).length() > 0)
          				htsql.put("A.PARENT_PRODUCT_LOC", LOC);*/
                                        
                                        

          			
          			String fdates="";
          			String tdates="";
          			if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
          			if (FROM_DATE.length()>5)
          				 fdates    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
          			if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
          			if (TO_DATE.length()>5)
          				 tdates    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);

          			

          			do_list = bomUtil.getKittingSummary(htsql, plant, fdates,
          					tdates,"");

          			if (do_list.size() > 0) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=KittingExcel.xls");
          				java.util.Vector v = null;
          %>
         <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   	 <TR BGCOLOR="#000066">

		<TH><font color="#ffffff" align="left"><b>SNO</TH>
		<TH><font color="#ffffff" align="left"><b>Parent
		Product ID</TH>
		<TH><font color="#ffffff" align="left"><b>Parent Desc</TH>
		<TH><font color="#ffffff" align="left"><b>Parent Batch</TH>
		<TH><font color="#ffffff" align="left"><b>Child
		Product Id</TH>
		<TH><font color="#ffffff" align="left"><b>Child Desc</TH>
		<TH><font color="#ffffff" align="left"><b>Child Batch</TH>
		<TH><font color="#ffffff" align="left"><b>Loc</TH>
		<TH><font color="#ffffff" align="left"><b>Qty</TH>
		<TH><font color="#ffffff" align="left"><b>Expiry Date</TH>
		<TH><font color="#ffffff" align="left"><b>Remarks</TH>
		<TH><font color="#ffffff" align="left"><b>Status</TH>


	</tr>
	
	  <%
		  	ItemMstDAO itemMstDAO = new ItemMstDAO();
		  				InvMstDAO _InvMstDAO = new InvMstDAO();

		  				for (int i = 0; i < do_list.size(); i++) {

		  					try {
		  						Map do_list1 = (Map) do_list.get(i);

		  						String sItem = (String) do_list1
		  								.get("CHILD_PRODUCT");
		  						String sDesc = strUtils.fString(itemMstDAO
		  								.getItemDesc(plant, sItem));

		  						int iIndex = i + 1;
		  						String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"
		  								: "#dddddd";

		  						//get expirydate from inventory
		  						String expiredate = _InvMstDAO
		  								.getInvExpiryDate(
		  										plant,
		  										(String) do_list1
		  												.get("CHILD_PRODUCT"),
		  										(String) do_list1
		  												.get("CHILD_PRODUCT_LOC"),
		  										(String) do_list1
		  												.get("CHILD_PRODUCT_BATCH"));
		  						//get expirydat from inventory end

		  						//get parent desc
		  						String parentdesc = itemMstDAO
		  								.getKittingParentItem(plant,
		  										(String) do_list1
		  												.get("PARENT_PRODUCT"));
		  %>
          <TR bgcolor = "<%=bgcolor%>">
                <TD align="center" width="3%"><%=iIndex%></TD>
                <TD align= "center" ><%=(String) do_list1
												.get("PARENT_PRODUCT")%></TD>
			    <TD align= "center" ><%=parentdesc%></TD>
				<TD align= "center" ><%=(String) do_list1
												.get("PARENT_PRODUCT_BATCH")%></TD>
				<TD align= "center" ><%=(String) do_list1
												.get("CHILD_PRODUCT")%></TD>
				<TD align= "center" ><%=sDesc%></TD> 
		        <TD align="center"><%=(String) do_list1
												.get("CHILD_PRODUCT_BATCH")%></TD>
				<TD align="center"><%=(String) do_list1
												.get("CHILD_PRODUCT_LOC")%></TD>
				<TD align="center"><%=StrUtils.formatNum((String) do_list1.get("QTY"))%></TD>
				<TD align="center"><%=expiredate%></TD>
		        <TD align="center"><%=(String) do_list1.get("REMARKS")%></TD>
		        <TD align="center"><%=(String) do_list1.get("STATUS")%></TD>
														
	    
        </TR>
         <%
         	} catch (Exception ee) {
         						System.out
         								.println("######################## KittingExcel ################ :"
         										+ ee);
         					}
         				}
         %></TABLE>
          <%
          	} else if (do_list.size() < 1) {
          				response.setContentType("application/vnd.ms-excel");
          				response.setHeader("Content-disposition",
          						"attachment; filename=InventoryExcel.xls");

          				out.println("No Records Found To List");
          			}

          		} catch (Exception e) {
          		}
          	}
          %>
          
         