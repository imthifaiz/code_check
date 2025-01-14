<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.EstDetDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.util.*"%>
<html>

<head>
<link rel="stylesheet" href="css/style.css">
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
			EstDetDAO _EstDetDAO= new EstDetDAO();
			String plant = StrUtils.fString(
					   (String) request.getSession().getAttribute("PLANT")).trim();

				String remarks1 = StrUtils.replaceStr2Char(_StrUtils.fString(request.getParameter("REMARKS1")));
				String remarks2 = StrUtils.replaceStr2Char(_StrUtils.fString(request.getParameter("REMARKS2")));
				String estno =StrUtils.replaceStr2Char( StrUtils.fString(request.getParameter("ESTNO")).trim());
				String item =StrUtils.replaceStr2Char( StrUtils.fString(request.getParameter("ITEM")).trim());
				String estlno = StrUtils.fString(request.getParameter("ESTLNNO")).trim();
				List al = new ArrayList();

				if (estno.length() > 0) {

					String query = "REMARKS,ESTLNNO";
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.ESTHDR_ESTNUM, estno);
					ht.put(IDBConstants.ESTHDR_ESTLNNUM, estlno);
					ht.put(IDBConstants.ITEM, item);				
					ht.put(IDBConstants.PLANT, plant);
					al = _EstDetDAO.selectRemarks(query, ht);
					%>	
	    			<table>
	    				<tr>
	    					<td>Remarks1:&nbsp;</td>
	    					<td>
	    						<textarea class="textBckgnd" style="resize:none;width: 100%;" rows="1" type="TEXT" size="100" MAXLENGTH=100 readonly><%=remarks1%></textarea>	    					
	    					</td>
    					</tr>
    					<tr>
	    					<td>Remarks2:</td>
	    					<td>
	    						<textarea class="textBckgnd" style="resize:none;width: 100%;" rows="1" type="TEXT" size="100" MAXLENGTH=100 readonly><%=remarks2%></textarea>
	    					</td>
    					</tr>
    					<tr>
	    					<td>Product ID:</td>
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