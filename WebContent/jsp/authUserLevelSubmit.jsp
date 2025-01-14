<%@ include file="header.jsp" %>

<%@ include file="body.jsp" %>

<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sb"  class="com.track.gates.sqlBean" />
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<jsp:useBean id="tl"  class="com.track.gates.TableList" />
<jsp:useBean id="df"  class="com.track.gates.defaultsBean" />

<%! String result;  %>
<%
    int m=0;
    String s[] = request.getParameterValues("LEVEL_NAME");
    String action     = request.getParameter("Action");

    if(s==null || s.length==0)
     {
            result = " <tr><td align=\"center\"><font class=mainred><h3>No User Levels selected to authorise/reject...</h3></font></td></tr>";
     }
    else if(action.equalsIgnoreCase("reject"))
     {
            result = "";
            ArrayList al = new ArrayList();  String logStr;
            for(int i=0; i< s.length; i++)
            {
                int n = ub.deletePreviousRecords(s[i],"1");
                logStr="";
                if(n==-2) //  User available for selected level
                {
                    logStr ="<tr><td><font  class=mainred><h3>"+(i+1)+". Could not delete level - '"+s[i]+"'.. One or more users are using it</h3></font></td></tr> ";
                    result+= logStr;
                }
                else if(n>0)
                {
                    logStr ="<tr><td><font  class=maingreen><h3>"+(i+1)+". The '"+s[i]+"' User Level has been successfully deleted from database</h3></font></td></tr>";
                    result+= logStr;
		}
                else
                {
                    logStr = "<tr><td><font  class=mainred><h3>"+(i+1)+". Error in deleting '"+s[i]+"' user level. Refresh the previous page and try again</h3></font></td></tr>";
                    result+= logStr;
                }
//                df.insertToLog(session, "Rejecting User Levels in Authorisation", logStr);   //  Inserting into the user log
            }

     }
    else if(action.equalsIgnoreCase("authorise"))
     {
        ArrayList al      = new ArrayList();    //      For building the fields
        ArrayList ral     = new ArrayList();    //      For building the values

        al.add("AUTHORISE_BY");
        al.add("AUTHORISE_ON");

      for( int i=0; i< s.length; i++)

      {
// Updating the table by authorising the same
        String level_name = s[i];

        String user = session.getAttribute("LOGIN_USER").toString();
        String time = gn.getDateTime();

        if(!ral.isEmpty()) ral.clear();
        ral.add(user);
        ral.add(time);

        String sqlUpdate = "update USER_LEVEL set "+tl.buildUpdateString(al,ral)+" where LEVEL_NAME='"+level_name+"'";
        System.out.print(sqlUpdate);

        int j = sb.insertRecords(sqlUpdate);
//        System.out.println(j);

        int k = ub.getRecordCount(level_name);
//        System.out.println(k);

             if((j==k)&&(j!=0))
		 {
                        m++;
			result = "<tr><td align=\"center\"><font  class=maingreen>"+m+" User Level(s) Authorised Successfully...</h3></font></td></tr>";
                        //df.insertToLog(session, "Authorising User Level",(i+1)+". User Level - "+level_name+" Authorised Successfully");   //  Inserting into the user log
                  }
		else
		 {
			result = "<tr><td align=\"center\"><font  class=mainred><h3> Error in authorising ..<br>"+m+" record(s) Authorised out of "+s.length+" totally</h3></font></td></tr>"+
                                "<tr><td align=\"center\"><font  class=mainred>Please refresh the previous page and try again</font></td></tr>";
                       // df.insertToLog(session, "Authorising User Level",(i+1)+". Error in authorising .."+level_name+" User Level Not Authorised");   //  Inserting into the user log
                        break;
		 }
        }   //  End of for
    }   //      End of else


                 result="<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" width=\"70%\" align=\"center\">"+result+"</table><br><center>"+
                        "<input type=\"button\" value=\" OK \"  onClick=\"window.location.href='../home'\">";


                session.setAttribute("RESULT",result);
                response.sendRedirect("displayResult2User.jsp");
%>

<%@ include file="footer.jsp" %>

