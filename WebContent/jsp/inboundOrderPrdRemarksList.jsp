<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.PoDetDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.util.*"%>
<html>

<head>
<title>Product Class List</title>
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<style>
	.textBckgnd{
	    BACKGROUND: #eeeeee;
	    BORDER-BOTTOM: #888888 1px solid;
	    BORDER-LEFT: #888888 1px solid;
	    BORDER-RIGHT: #888888 1px solid;
	    BORDER-TOP: #888888 1px solid;
	    COLOR: #000000;
	    FONT-FAMILY: Arial, Arial;
	    FONT-SIZE: 12px;
    }
</style>
</head>
<body bgcolor="#ffffff">
	<%
	StrUtils _StrUtils = null;
	String action="";

		try {

			action = _StrUtils.fString(request.getParameter("Submit")).trim();
			_StrUtils = new StrUtils();
			PoDetDAO _poDetDAO = new PoDetDAO();
			String plant = StrUtils.fString(
					   (String) request.getSession().getAttribute("PLANT")).trim();

				String remarks1 = _StrUtils.fString(request.getParameter("REMARKS1"));
				String remarks2 = _StrUtils.fString(request.getParameter("REMARKS2"));
				String pono = _StrUtils.fString(request.getParameter("PONO"));
				String item = _StrUtils.fString(request.getParameter("ITEM"));
				String polnno = _StrUtils.fString(request.getParameter("POLNNO"));
				List al = new ArrayList();

				if (pono.length() > 0) {

					String query = "REMARKS,POLNNO";
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PODET_PONUM, pono);
					ht.put(IDBConstants.PODET_ITEM, item);
					ht.put(IDBConstants.PODET_POLNNO, polnno);
					ht.put(IDBConstants.PLANT, plant);
					al = _poDetDAO.selectRemarks(query, ht);
					%>	
	    			<table>
	    				<tr>
	    					<td>Remarks1&nbsp;</td>
	    					<td>
	    						<textarea class="textBckgnd" style="resize:none;width: 100%;" rows="1" type="TEXT" size="100" MAXLENGTH=100 readonly><%=remarks1%></textarea>	    					
	    					</td>
    					</tr>
    					<tr>
	    					<td>Remarks2</td>
	    					<td>
	    						<textarea class="textBckgnd" style="resize:none;width: 100%;" rows="1" type="TEXT" size="100" MAXLENGTH=100 readonly><%=remarks2%></textarea>
	    					</td>
    					</tr>
    					<tr>
	    					<td>Product ID</td>
	    					<td><input type="test" size="100" value="<%=item%>" readonly></td>
	    				</tr>
	    					    				
	    				<%
	    				for (int i = 0; i < al.size(); i++) {
	    					Map map = (Map) al.get(i);
	    					%>
	    					<tr>
	    					<%if(i == 0) {%>
	    					<td>Remarks</td>
	    					<% }else{ %>
	    					<td></td>
	    					<% }%>
	    					<td>
	    					<textarea class="textBckgnd" style="resize:none;width: 100%;" rows="1" type="TEXT" size="100" MAXLENGTH=100 readonly><%=map.get("REMARKS")%></textarea>
	    					</td>
	    					</tr>
	    					<%	
	    				}
	    				%>
	    				
	    			</table>
					
					<%
				} 		  
				
		}catch (Exception he) {
			he.printStackTrace();
			System.out.println("Error in reterieving data");
		}
	%>
</body>
</html>