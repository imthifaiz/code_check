<%@ include file="header.jsp" %>
<%@ include file="body.jsp" %>

<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sb"  class="com.track.gates.sqlBean" />
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<jsp:useBean id="tl"  class="com.track.gates.TableList" />
<jsp:useBean id="df"  class="com.track.gates.defaultsBean" />

<%! String result,cancelBtn,nextPage; %>
<%
    String user_ids[] = request.getParameterValues("USER_ID");
    String action     = request.getParameter("Action");
    int m=0;
    cancelBtn = "<br><br><center><input type=\"button\" value=\" OK \" onClick=\"window.location.href='../home'\">";
    nextPage = "<br><br><center><input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                    "<input type=\"button\" value=\"Cancel\" name=\"cancelBtn\" onClick=\"window.location.href='../home'\">";

    if(user_ids==null || user_ids.length==0)
     {
            result = " No User selected to authorise/reject..."+nextPage;
     }
    else if(action.equalsIgnoreCase("reject"))
     {
            ArrayList al = new ArrayList();
            for(int i=0; i< user_ids.length; i++)
            {
                al.add(ub.getDeleteUserString(user_ids[i]));
            }

            boolean b = sb.insertBatchRecords(al);
            if(b) result = "<font class=mainred>Successfully rejected "+al.size()+" user(s)"+cancelBtn;
            else  result = "<font class=mainred>Could not reject user(s)"+nextPage;

            df.insertToLog(session, "Rejecting Users in Authorisation", result);   //  Inserting into the user log
            //   Adding individual rejected users in User Log
            if(b)
            {
                for(int i=0; i< user_ids.length; i++)
                df.insertToLog(session, "List of Rejected Users ", (i+1)+". Rejected User - "+user_ids[i]+"in User Authorisation");   //  Inserting into the user log
            }

     }
    else if(action.equalsIgnoreCase("authorise"))
     {

        ArrayList al = new ArrayList();
        ArrayList ral = new ArrayList();

        al.add("AUTHORISE_BY");
        al.add("AUTHORISE_ON");

      for( int i=0; i< user_ids.length; i++)

      {
// Updating the table by authorising the same
        String user_id = user_ids[i];

        String user = session.getAttribute("LOGIN_USER").toString();
        String time = gn.getDateTime();

        if(!ral.isEmpty()) ral.clear();
        ral.add(user);
        ral.add(time);

        String sqlUpdate = "update USER_INFO set "+tl.buildUpdateString(al,ral)+" where USER_ID='"+user_id+"'";

        int n = sb.insertRecords(sqlUpdate);

             if(n==1)
		 {
                        m++;
			result = "<font class=maingreen>"+m+" User(s) Authorised Successfully...</font>"+cancelBtn;
                        df.insertToLog(session, "Authorising Users",(i+1)+". User - "+user_id+" Authorised Successfully");   //  Inserting into the user log
                 }
		else
		 {
			result = "<font class=mainred> Error in authorising .."+m+" user(s) Authorised..</font>"+nextPage;
                        df.insertToLog(session, "Authorising Users",(i+1)+". Error in authorising .."+user_id+" User Not Authorised");   //  Inserting into the user log
                        break;
		 }

        }   //  End of for
    }   //      End of else

                result = "<h3><b>"+result;

                session.setAttribute("RESULT",result);
                response.sendRedirect("displayResult2User.jsp");

%>
<%@ include file="footer.jsp" %>

