<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %> 
<%@ page import="java.sql.PreparedStatement" %> 
<%@ page import="java.sql.ResultSet" %> 
<%@ page import="java.sql.Connection" %> 
<%@ page import="java.sql.DriverManager" %> 
<%! 
public int nullIntconv(String str) 
{ 
int conv=0; 
if(str==null) 
{ 
str="0"; 
} 
else if((str.trim()).equals("null")) 
{ 
str="0"; 
} 
else if(str.equals("")) 
{ 
str="0"; 
} 
try{ 
conv=Integer.parseInt(str); 
} 
catch(Exception e) 
{ 
} 
return conv; 
} 
%> 
<% 

Connection conn = null; 
Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance(); 
conn = DriverManager.getConnection("jdbc:sqlserver://T002\\SQLEXPRESS:2161;DatabaseName=track1.0-Live;SelectMethod=cursor","sa", "track"); 

ResultSet rsPagination = null; 
ResultSet rsRowCnt = null; 

PreparedStatement psPagination=null; 
PreparedStatement psRowCnt=null; 

int iShowRows=3; // Number of records show on per page 
int iTotalSearchRecords=10; // Number of pages index shown 

int iTotalRows=nullIntconv(request.getParameter("iTotalRows")); 
int iTotalPages=nullIntconv(request.getParameter("iTotalPages")); 
int iPageNo=nullIntconv(request.getParameter("iPageNo")); 
int cPageNo=nullIntconv(request.getParameter("cPageNo")); 

int iStartResultNo=0; 
int iEndResultNo=0; 

if(iPageNo==0) 
{ 
iPageNo=1; 
} 
 

int start = ((new Integer(iPageNo)-1)*iShowRows)+1;
int end = (new Integer(iPageNo)*iShowRows);


String sqlPagination="SELECT * FROM(SELECT (ROW_NUMBER() OVER ( ORDER BY ITEM)) AS ID,ITEM,ITEMDESC,ITEMTYPE FROM demo1_itemmst) A WHERE ID>= "+start+" AND ID<="+end+""; 

psPagination=conn.prepareStatement(sqlPagination); 
rsPagination=psPagination.executeQuery(); 

//// this will count total number of rows 
String sqlRowCnt="SELECT count(*) as cnt FROM demo1_itemmst "; 
psRowCnt=conn.prepareStatement(sqlRowCnt); 
rsRowCnt=psRowCnt.executeQuery(); 

if(rsRowCnt.next()) 
{ 
iTotalRows=rsRowCnt.getInt("cnt"); 
} 
if(end>iTotalRows){
end = iTotalRows;
}
%> 
<html> 
<head> 
<title>Pagination of JSP page</title> 

</head> 
<body> 
<form name="frm"> 
<input type="hidden" name="iPageNo" value="<%=iPageNo%>"> 
<input type="hidden" name="cPageNo" value="<%=cPageNo%>"> 
<input type="hidden" name="iShowRows" value="<%=iShowRows%>"> 
<table width="100%" cellpadding="0" cellspacing="0" border="0" > 
<tr> 
<td>Name</td> 
<td>Batch</td> 
<td>Address</td> 
</tr> 
<% 
while(rsPagination.next()) 
{ 
%> 
<tr> 
<td><%=rsPagination.getString("ID")%></td> 
<td><%=rsPagination.getString("ITEM")%></td> 
<td><%=rsPagination.getString("ITEMDESC")%></td> 
</tr> 
<% 
} 
%> 
<% 
//// calculate next record start record and end record 
try{ 
if(iTotalRows<(iPageNo+iShowRows)) 
{ 
iEndResultNo=iTotalRows; 
} 
else 
{ 
iEndResultNo=(iPageNo+iShowRows); 
} 

iStartResultNo=(iPageNo); 
iTotalPages=((int)(Math.ceil((double)iTotalRows/iShowRows))); 

} 
catch(Exception e) 
{ 
e.printStackTrace(); 
} 

%> 
<tr> 
<td colspan="3"> 
<div> 
<% 
//// index of pages 

int i=0; 
int cPage=0; 
if(iTotalRows!=0) 
{ 
cPage=((int)(Math.ceil((double)iEndResultNo/(iTotalSearchRecords*iShowRows)))); 

int prePageNo=(cPage*iTotalSearchRecords)-((iTotalSearchRecords-1)+iTotalSearchRecords); 
if((cPage*iTotalSearchRecords)-(iTotalSearchRecords)>0) 
{ 
%> 
<a href="tblControl_list.jsp?iPageNo=<%=prePageNo%>&cPageNo=<%=prePageNo%>"> << Previous</a> 
<% 
} 

for(i=((cPage*iTotalSearchRecords)-(iTotalSearchRecords-1));i<=(cPage*iTotalSearchRecords);i++) 
{ 
if(i==((iPageNo/iShowRows)+1)) 
{ 
%> 
<a href="tblControl_list.jsp?iPageNo=<%=i%>" style="cursor:pointer;color: red"><b><%=i%></b></a> 
<% 
} 
else if(i<=iTotalPages) 
{ 
%> 
<a href="tblControl_list.jsp?iPageNo=<%=i%>"><%=i%></a> 
<% 
} 
} 
if(iTotalPages>iTotalSearchRecords && i<iTotalPages) 
{ 
%> 
<a href="tblControl_list.jsp?iPageNo=<%=i%>&cPageNo=<%=i%>"> >> Next</a> 
<% 
} 
} 
%> 
<b>Rows <%=start%> - <%=end%> Total Result <%=iTotalRows%> </b> 
</div> 
</td> 
</tr> 
</table> 
</form> 
</body> 
</html> 
