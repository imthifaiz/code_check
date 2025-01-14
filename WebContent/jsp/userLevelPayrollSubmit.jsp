
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.TransactionConstants"%>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="sb" class="com.track.gates.sqlBean" />
<jsp:useBean id="tl" class="com.track.gates.TableList" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="df" class="com.track.gates.defaultsBean" />

<br>
<br>
<center>
<h3><%!String okBtn, nextPage;%> <%
 	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
 	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
 	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
 	MLogger mLogger = new MLogger();
 	mLogger.setLoggerConstans(loggerDetailsHasMap);
 	
 	
 	DateUtils _dateutils = new DateUtils();
 	session = request.getSession();
 	String plant = ((String) session.getAttribute("PLANT")).trim();
 	String company = ((String) session.getAttribute("PLANT")).trim();
 	String user = ((String) session.getAttribute("LOGIN_USER")).trim();
 	String urls[] = request.getParameterValues("URL");
 	
 	
 	String UserDasnboard[] = request.getParameterValues("UserDasnboard");
 	String UserAdmin[] = request.getParameterValues("UserAdmin");
 	String pPrint_Config[] = request.getParameterValues("pPrint_Config");
 	String UserEmployee[] = request.getParameterValues("UserEmployee");
 	String userEmployeeType[] = request.getParameterValues("userEmployeeType");
 	String pdepartment[] = request.getParameterValues("pdepartment");
 	String psalarytype[] = request.getParameterValues("psalarytype");
 	String pLeavetype[] = request.getParameterValues("pLeavetype");
 	String pholidaytype[] = request.getParameterValues("pholidaytype");
 	String Banking[] = request.getParameterValues("Banking");
 	String shift[] = request.getParameterValues("Shift");
 	String payaddition[] = request.getParameterValues("payaddition");
 	String paydeduction[] = request.getParameterValues("paydeduction");
 	String UserPayroll[] = request.getParameterValues("UserPayroll");
 	String payrollreport[] = request.getParameterValues("payrollreport");
 	String PayrollPayment[] = request.getParameterValues("PayrollPayment");
 	String pClaim[] = request.getParameterValues("pClaim");
 	String pClaimPayment[] = request.getParameterValues("pClaimPayment");
 	String pbulkpayslip[] = request.getParameterValues("pbulkpayslip");
 	
 	String userReports[] = request.getParameterValues("payreports");
 	
 	
	
 	String level_name = request.getParameter("LEVEL_NAME");
 	String remarks = request.getParameter("REMARKS");
 	String action = request.getParameter("Action");
 	String result = "";
 	MovHisDAO mdao = new MovHisDAO();

 	
 	if (plant.equalsIgnoreCase("track")) {
 		plant = "";
 	} else {
 		plant = plant + "_";
 	}
 	okBtn = "<br><br><center><input type=\"button\" value=\"  OK  \" onClick=\"window.location.href='../home'\">";
 	nextPage = "<br><br><center><input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> "
 			+ "<input type=\"button\" value=\"Cancel\" name=\"cancelBtn\" onClick=\"window.location.href='../home'\">";

 	if (action.equalsIgnoreCase("delete")) {
 		int n = ub.deletePreviousRecordspayroll(level_name, "1", company);
 		if (n == -2) //  User available for selected level
 		{
 			result = "<font  class=mainred>One or more users are using the level - '"
 					+ level_name
 					+ "'<br><font class=mainred> Could not delete level..</font> "
 					+ "<br><br><center><input type=\"button\" value=\"Cancel\" name=\"nextBtn\" onClick=\"window.location.href='../home'\"> ";
 		} else if (n > 0) {

 			Hashtable htm = new Hashtable();
 			htm.put("PLANT", company);
 			htm.put("DIRTYPE", TransactionConstants.DELETE_GROUP);
 			htm.put("RECID", "");
 			htm.put("REMARKS", level_name + "," + remarks);
 			htm.put("CRBY", user);
 			htm.put("UPBY", user);
 			htm.put("CRAT", _dateutils.getDateTime());
 			htm.put("UPAT", _dateutils.getDateTime());
 			htm.put(IDBConstants.TRAN_DATE, _dateutils
 					.getDateinyyyy_mm_dd(_dateutils.getDate()));
 			boolean inserted = mdao.insertIntoMovHis(htm, plant);

 			/* result = "<font  class=maingreen>The '"
 					+ level_name
 					+ "' User Level has been successfully deleted from database</font>"
 					+ okBtn; */
 					
 			 response.sendRedirect("../useraccess/paysummary?RESULT=The '"+ level_name+ "' User Level has been successfully deleted from database");
 		}

 		df.insertToLog(session, "Deleting User Level", result); //  Inserting into the user log

 	}/*  else if (urls == null || urls.length == 1) {
 		result = " <font class=mainred> No levels selected to add/update the database..."
 				+ nextPage;
 	} */ else if ((ub.isAlreadyAvailpayroll(level_name, 1, company))
 			&& (!action.equalsIgnoreCase("update"))) //  Checking if the level name exists already
 	{
 		result = " <font  class=mainred> The Level Name '"
 				+ level_name
 				+ "'already exists in database ...<br>Please choose a different name"
 				+ nextPage;
 		df.insertToLog(session, "Adding User Level", result); //  Inserting into the user log
 	} else {

 		//  Building  USER_LEVEL field string

 		int success = 0;
 		int failure = 0;
 		ArrayList userField = tl.getTableArray("USER_LEVEL");

 		//  Declaring  arraylists for value Strings

 		String createdBy = (String) session.getAttribute("LOGIN_USER");
 		String updatedBy = createdBy;
 		String createdOn;
 		String updatedOn;
 		String url_name = "";
 		ArrayList userValue = new ArrayList(); //      Arraylist   for building value string for USER_LEVEL
 		ArrayList sqllist = new ArrayList(); //      ArrayList  for holding all the insert statements
 		if(urls!=null)
 		{
 		for (int i = 0; i < urls.length; i++) {
 			url_name = urls[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}//    End of for
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		if(UserDasnboard!=null)
 		{
 		 for (int i = 0; i < UserDasnboard.length; i++) {
 			url_name = UserDasnboard[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		if(UserAdmin!=null)
 		{
 		 for (int i = 0; i < UserAdmin.length; i++) {
 			url_name = UserAdmin[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		if(pPrint_Config!=null)
 		{
 		 for (int i = 0; i < pPrint_Config.length; i++) {
 			url_name = pPrint_Config[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(UserEmployee!=null)
 		{
 		 for (int i = 0; i < UserEmployee.length; i++) {
 			url_name = UserEmployee[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(userEmployeeType!=null)
 		{
 		 for (int i = 0; i < userEmployeeType.length; i++) {
 			url_name = userEmployeeType[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		if(pdepartment!=null)
 		{
 		 for (int i = 0; i < pdepartment.length; i++) {
 			url_name = pdepartment[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		
 		if(psalarytype!=null)
 		{
 		 for (int i = 0; i < psalarytype.length; i++) {
 			url_name = psalarytype[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		
 		if(pLeavetype!=null)
 		{
 		 for (int i = 0; i < pLeavetype.length; i++) {
 			url_name = pLeavetype[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(pholidaytype!=null)
 		{
 		 for (int i = 0; i < pholidaytype.length; i++) {
 			url_name = pholidaytype[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		if(Banking!=null)
 		{
 		 for (int i = 0; i < Banking.length; i++) {
 			url_name = Banking[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(shift!=null)
 		{
 		 for (int i = 0; i < shift.length; i++) {
 			url_name = shift[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}


 		
 		if(payaddition!=null)
 		{
 		 for (int i = 0; i < payaddition.length; i++) {
 			url_name = payaddition[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(paydeduction!=null)
 		{
 		 for (int i = 0; i < paydeduction.length; i++) {
 			url_name = paydeduction[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(UserPayroll!=null)
 		{
 		 for (int i = 0; i < UserPayroll.length; i++) {
 			url_name = UserPayroll[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		if(PayrollPayment!=null)
 		{
 		 for (int i = 0; i < PayrollPayment.length; i++) {
 			url_name = PayrollPayment[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		 }
 		 }
 		 
  		if(pClaim!=null)
  		{
  		 for (int i = 0; i < pClaim.length; i++) {
  			url_name = pClaim[i];
  			createdOn = gn.getDateTime();
  			updatedOn = createdOn;

  			//      Building Insert Strings

  			if (!userValue.isEmpty())
  				userValue.clear();
  			userValue.add((String) session.getAttribute("PLANT"));
  			userValue.add(level_name);
  			userValue.add(url_name);
  			userValue.add(remarks);
  			userValue.add(createdBy);
  			userValue.add(createdOn);
  			userValue.add(updatedBy);
  			userValue.add(updatedOn);
  			userValue.add(createdBy); //      Authorise_by
  			userValue.add(createdOn); //      Authorise_on
  			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_PAYROLL" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		}
  		 
  		if(pClaimPayment!=null)
  		{
  		 for (int i = 0; i < pClaimPayment.length; i++) {
  			url_name = pClaimPayment[i];
  			createdOn = gn.getDateTime();
  			updatedOn = createdOn;

  			//      Building Insert Strings

  			if (!userValue.isEmpty())
  				userValue.clear();
  			userValue.add((String) session.getAttribute("PLANT"));
  			userValue.add(level_name);
  			userValue.add(url_name);
  			userValue.add(remarks);
  			userValue.add(createdBy);
  			userValue.add(createdOn);
  			userValue.add(updatedBy);
  			userValue.add(updatedOn);
  			userValue.add(createdBy); //      Authorise_by
  			userValue.add(createdOn); //      Authorise_on
  			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_PAYROLL" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		}
  		if(pbulkpayslip!=null)
  		{
  		 for (int i = 0; i < pbulkpayslip.length; i++) {
  			url_name = pbulkpayslip[i];
  			createdOn = gn.getDateTime();
  			updatedOn = createdOn;

  			//      Building Insert Strings

  			if (!userValue.isEmpty())
  				userValue.clear();
  			userValue.add((String) session.getAttribute("PLANT"));
  			userValue.add(level_name);
  			userValue.add(url_name);
  			userValue.add(remarks);
  			userValue.add(createdBy);
  			userValue.add(createdOn);
  			userValue.add(updatedBy);
  			userValue.add(updatedOn);
  			userValue.add(createdBy); //      Authorise_by
  			userValue.add(createdOn); //      Authorise_on
  			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_PAYROLL" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		}
 		
 		if(payrollreport!=null)
 		{
 		 for (int i = 0; i < payrollreport.length; i++) {
 			url_name = payrollreport[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		}
 		 
 		 if(payrollreport.length > 0){
 			url_name = "PayrollReport";
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserti
 		 }
 		}
 		
 		
 		if(userReports!=null)
 		{
 		 for (int i = 0; i < userReports.length; i++) {
 			url_name = userReports[i];
 			createdOn = gn.getDateTime();
 			updatedOn = createdOn;

 			//      Building Insert Strings

 			if (!userValue.isEmpty())
 				userValue.clear();
 			userValue.add((String) session.getAttribute("PLANT"));
 			userValue.add(level_name);
 			userValue.add(url_name);
 			userValue.add(remarks);
 			userValue.add(createdBy);
 			userValue.add(createdOn);
 			userValue.add(updatedBy);
 			userValue.add(updatedOn);
 			userValue.add(createdBy); //      Authorise_by
 			userValue.add(createdOn); //      Authorise_on
 			userValue.add(Integer.toString(ub.getUserMenuPKpayroll(url_name,(String) session.getAttribute("PLANT"))));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_PAYROLL" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
		
 		boolean b = false;
 		if (action.equalsIgnoreCase("update")) {
 			//            int n = ub.deletePreviousRecords(level_name,"0");
 			int n = ub.deletePreviousRecordspayroll(level_name, "0", company);
 			if (n > 0) {
 				b = sb.insertBatchRecords(sqllist);

 				Hashtable htm = new Hashtable();
 				htm.put("PLANT", company);
 				htm.put("DIRTYPE", TransactionConstants.UPDATE_GROUP);
 				htm.put("RECID", "");
 				htm.put("REMARKS", level_name + "," + remarks);
 				htm.put("CRBY", user);
 				htm.put("UPBY", user);
 				htm.put("CRAT", _dateutils.getDateTime());
 				htm.put("UPAT", _dateutils.getDateTime());
 				htm.put(IDBConstants.TRAN_DATE, _dateutils.getDateinyyyy_mm_dd(_dateutils.getDate()));
 				boolean inserted = mdao.insertIntoMovHis(htm, plant);

 			}
 		} else {
 		
 			System.out.print(sqllist);
 			b = sb.insertBatchRecords(sqllist);
 			Hashtable htm = new Hashtable();
 			htm.put("PLANT", company);
 			htm.put("DIRTYPE", TransactionConstants.ADD_GROUP);
 			htm.put("RECID", "");
 			htm.put("REMARKS", level_name + "," + remarks);
 			htm.put("CRBY", user);
 			htm.put("UPBY", user);
 			htm.put("CRAT", _dateutils.getDateTime());
 			htm.put("UPAT", _dateutils.getDateTime());
 			htm.put(IDBConstants.TRAN_DATE, _dateutils.getDateinyyyy_mm_dd(_dateutils.getDate()));
 			boolean inserted = mdao.insertIntoMovHis(htm, plant);
 			
 		}

 		if (b) {
 			 response.sendRedirect("../useraccess/paysummary?RESULT=The User Level "+level_name+" has been Updated Sucessfully");
 		/* 	result = "<font <font  class=maingreen>The User Level '"
 					+ level_name
 					+ "' has been added/updated to the <br>database.."
 					+ "</font>" + okBtn; */
 		} else {
 			result = "<font <font  class=mainred>Error occured in performing database operation.. <br> Please try again</font>"
 					+ nextPage;
 		}

 		df.insertToLog(session, "Adding/Updating User Level", result); //  Inserting into the user log

 	} //      End of else

 	result = "<h3>" + result;

 	session.setAttribute("RESULT", result);
 //	response.sendRedirect("displayResult2User.jsp");
 %> <%@ include file="footer.jsp"%>