
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
 	String home[] = request.getParameterValues("Home");
 	String userAdmin[] = request.getParameterValues("UserAdmin");
 	String systemMaster[] = request.getParameterValues("SystemMaster");
 	String UomLoc[] = request.getParameterValues("UomLoc");
 	String accountingAdmin[] = request.getParameterValues("AccountingAdmin");
 	String systemAdmin[] = request.getParameterValues("SystemAdmin");
 	String orderAdmin[] = request.getParameterValues("OrderAdmin");
 	String distribution[] = request.getParameterValues("Distribution");
 	
 	String Supplier[] = request.getParameterValues("Supplier");
 	String Customer[] = request.getParameterValues("Customer");
 	String UserEmployee[] = request.getParameterValues("UserEmployee");
 	String OrderAdminHS[] = request.getParameterValues("OrderAdminHS");
 	
 	String orderConfiguration[] = request.getParameterValues("OrderConfiguration");
 	String emailConfiguration[] = request.getParameterValues("EmailConfiguration");
 	String orderManagement[] = request.getParameterValues("OrderManagement");
 	String orderManagement1[] = request.getParameterValues("OrderManagement1");
 	
 	String project[] = request.getParameterValues("Project");
 	String Purchaseestimate[] = request.getParameterValues("Purchaseestimate");
 	
 	String POPurchaseTransaction[] = request.getParameterValues("PurchaseTransaction");
 	String expenses[] = request.getParameterValues("expenses");
 	String GoodsReceipt[] = request.getParameterValues("GoodsReceipt");
 	String bill[] = request.getParameterValues("bill");
 	String PurchaseReturn[] = request.getParameterValues("PurchaseReturn");
 	String ProductReturn[] = request.getParameterValues("ProductReturn");
 	String ProductReturnReceive[] = request.getParameterValues("ProductReturnReceive");
 	String SupplierCreditNotes[] = request.getParameterValues("SupplierCreditNotes");
 	String PurchaseReports[] = request.getParameterValues("PurchaseReports");

 	String SalesEstimateReports[] = request.getParameterValues("SalesEstimateReports");
 	String purchaseTransaction[] = request.getParameterValues("SalesEstimate");
 	String rentalTransaction[] = request.getParameterValues("RentalTransaction");
 	String inHouse[] = request.getParameterValues("InHouse");
 	String salesTransaction[] = request.getParameterValues("SalesTransaction");
 	
 	String SalesOrder[] = request.getParameterValues("SalesOrder");
 	String Consignment[] = request.getParameterValues("Consignment");
 	
//  	String RentalTransaction[] = request.getParameterValues("RentalTransaction");
 	String ConsignmentReports[] = request.getParameterValues("ConsignmentReports");
 	
 	String GoodsIssued[] = request.getParameterValues("GoodsIssued");
 	String Invoice[] = request.getParameterValues("Invoice");
 	String SalesReturn[] = request.getParameterValues("SalesReturn");
 	String CreditNotes[] = request.getParameterValues("CreditNotes");
 	String SalesReports[] = request.getParameterValues("SalesReports");
 	
 	String reports[] = request.getParameterValues("Reports");
 	String posreports[] = request.getParameterValues("PosReports");
 	String activityLogs[] = request.getParameterValues("ActivityLogs");
 	String peppolIntegration[] = request.getParameterValues("PeppolIntegration");
 	String accountingHome[] = request.getParameterValues("AccountingHome");
 	
 	String Banking[] = request.getParameterValues("Banking");
 	String chartOfAccounts[] = request.getParameterValues("chartOfAccounts");
 	String apSummary[] = request.getParameterValues("apSummary");
 	String PaymentMade[] = request.getParameterValues("PaymentMade");
 	String PDCPaymentMade[] = request.getParameterValues("PDCPaymentMade");
 	String PaymentReceived[] = request.getParameterValues("PaymentReceived");
 	String PDCPaymentReceived[] = request.getParameterValues("PDCPaymentReceived");
 	String JournalEntry[] = request.getParameterValues("JournalEntry");
 	String ContraEntry[] = request.getParameterValues("ContraEntry");
 	
 	String accountingMaster[] = request.getParameterValues("AccountingMaster");
 	String accountingTransactions[] = request.getParameterValues("AccountingTransactions");
	String accountingReports[] = request.getParameterValues("AccountingReports");
	String pdaPurchaseTransaction[] = request.getParameterValues("PdaPurchaseTransaction");
	String pdaSalesTransaction[] = request.getParameterValues("PdaSalesTransaction");
	String pdaInHouse[] = request.getParameterValues("PdaInHouse");
 	String level_name = request.getParameter("LEVEL_NAME");
 	String remarks = request.getParameter("REMARKS");
 	String action = request.getParameter("Action");
 	String result = "";
 	MovHisDAO mdao = new MovHisDAO();
 	
 	System.out.print(userAdmin);
 	mdao.setmLogger(mLogger);
 	ub.setmLogger(mLogger);
 	df.setmLogger(mLogger);
 	sb.setmLogger(mLogger);
 	
 	if (plant.equalsIgnoreCase("track")) {
 		plant = "";
 	} else {
 		plant = plant + "_";
 	}
 	okBtn = "<br><br><center><input type=\"button\" value=\"  OK  \" onClick=\"window.location.href='../home'\">";
 	nextPage = "<br><br><center><input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> "
 			+ "<input type=\"button\" value=\"Cancel\" name=\"cancelBtn\" onClick=\"window.location.href='../home'\">";

 	if (action.equalsIgnoreCase("delete")) {
 		int n = ub.deletePreviousRecordsaccounting(level_name, "1", company);
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
 					
 			response.sendRedirect("../useraccess/summary?RESULT=The '"+ level_name+ "' User Level has been successfully deleted from database");
 		}

 		df.insertToLog(session, "Deleting User Level", result); //  Inserting into the user log

 	}/*  else if (urls == null || urls.length == 1) {
 		result = " <font class=mainred> No levels selected to add/update the database..."
 				+ nextPage;
 	} */ else if ((ub.isAlreadyAvailaccounting(level_name, 1, company))
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}//    End of for
 		if(home!=null)
 		{
 		 for (int i = 0; i < home.length; i++) {
 			url_name = home[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		if(userAdmin!=null)
 		{
 		 for (int i = 0; i < userAdmin.length; i++) {
 			url_name = userAdmin[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(systemMaster!=null)
 		{
 		 for (int i = 0; i < systemMaster.length; i++) {
 			url_name = systemMaster[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(UomLoc!=null)
 		{
 		 for (int i = 0; i < UomLoc.length; i++) {
 			url_name = UomLoc[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(systemAdmin!=null)
 		{
 		 for (int i = 0; i < systemAdmin.length; i++) {
 			url_name = systemAdmin[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		if(accountingAdmin!=null)
 		{
 		 for (int i = 0; i < accountingAdmin.length; i++) {
 			url_name = accountingAdmin[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		
 		if(orderAdmin!=null)
 		{
 		 for (int i = 0; i < orderAdmin.length; i++) {
 			url_name = orderAdmin[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		if(distribution!=null)
 		{
 		 for (int i = 0; i < distribution.length; i++) {
 			url_name = distribution[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		if(Supplier!=null)
 		{
 		 for (int i = 0; i < Supplier.length; i++) {
 			url_name = Supplier[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		if(Customer!=null)
 		{
 		 for (int i = 0; i < Customer.length; i++) {
 			url_name = Customer[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(OrderAdminHS!=null)
 		{
 		 for (int i = 0; i < OrderAdminHS.length; i++) {
 			url_name = OrderAdminHS[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		
 		if(orderConfiguration!=null)
 		{
 		 for (int i = 0; i < orderConfiguration.length; i++) {
 			url_name = orderConfiguration[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(emailConfiguration!=null)
 		{
 		 for (int i = 0; i < emailConfiguration.length; i++) {
 			url_name = emailConfiguration[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(orderManagement!=null)
 		{
 		 for (int i = 0; i < orderManagement.length; i++) {
 			url_name = orderManagement[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(orderManagement1!=null)
 		{
 		 for (int i = 0; i < orderManagement1.length; i++) {
 			url_name = orderManagement1[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}

 		if(project!=null)
 		{
 		 for (int i = 0; i < project.length; i++) {
 			url_name = project[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		}
 		}
 		
 		if(Purchaseestimate!=null)
 		{
 		 for (int i = 0; i < Purchaseestimate.length; i++) {
 			url_name = Purchaseestimate[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		}
 		}
 		
 		if(POPurchaseTransaction!=null)
 		{
 		 for (int i = 0; i < POPurchaseTransaction.length; i++) {
 			url_name = POPurchaseTransaction[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		}
 		}

  		if(expenses!=null)
  		{
  		 for (int i = 0; i < expenses.length; i++) {
  			url_name = expenses[i];
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
  			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		}
  		 
  		if(GoodsReceipt!=null)
  		{
  		 for (int i = 0; i < GoodsReceipt.length; i++) {
  			url_name = GoodsReceipt[i];
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
  			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		} 
  		 
  		if(bill!=null)
  		{
  		 for (int i = 0; i < bill.length; i++) {
  			url_name = bill[i];
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
  			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		}
  		 
  		if(PurchaseReturn!=null)
  		{
  		 for (int i = 0; i < PurchaseReturn.length; i++) {
  			url_name = PurchaseReturn[i];
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
  			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		}
  		   		 
  		if(ProductReturn!=null)
  		{
  		 for (int i = 0; i < ProductReturn.length; i++) {
  			url_name = ProductReturn[i];
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
  			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		}
  		   		 
  		if(ProductReturnReceive!=null)
  		{
  		 for (int i = 0; i < ProductReturnReceive.length; i++) {
  			url_name = ProductReturnReceive[i];
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
  			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		}
  		 
  		if(SupplierCreditNotes!=null)
  		{
  		 for (int i = 0; i < SupplierCreditNotes.length; i++) {
  			url_name = SupplierCreditNotes[i];
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
  			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		}  		 
  		if(PurchaseReports!=null)
  		{
  		 for (int i = 0; i < PurchaseReports.length; i++) {
  			url_name = PurchaseReports[i];
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
  			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

  			String sqlInsert = "insert into " + "[" + plant
  					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
  					+ tl.getFieldString(userField) + ") values ("
  					+ tl.getValueString(userValue) + ")";
  			
  			if (!url_name.equals("on"))
  			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                         

  		}
  		}
 		
 		if(purchaseTransaction!=null)
 		{
 		 for (int i = 0; i < purchaseTransaction.length; i++) {
 			url_name = purchaseTransaction[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		}
 		}
 		
 		if(SalesEstimateReports!=null)
 		{
 		 for (int i = 0; i < SalesEstimateReports.length; i++) {
 			url_name = SalesEstimateReports[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		}
 		}

 		if(rentalTransaction!=null)
 		{
 		 for (int i = 0; i < rentalTransaction.length; i++) {
 			url_name = rentalTransaction[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		}
 		}
 		
 		//imthi
 		if(ConsignmentReports!=null)
 		{
 		 for (int i = 0; i < ConsignmentReports.length; i++) {
 			url_name = ConsignmentReports[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		}
 		}

 		
 		if(inHouse!=null)
 		{
 		 for (int i = 0; i < inHouse.length; i++) {
 			url_name = inHouse[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        

 		} 
 		}
 		
 		if(salesTransaction!=null)
 		{
 		 for (int i = 0; i < salesTransaction.length; i++) {
 			url_name = salesTransaction[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
 		
 		if(SalesOrder!=null)
 		{
 		 for (int i = 0; i < SalesOrder.length; i++) {
 			url_name = SalesOrder[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
 		
 		//navas
 		if(Consignment!=null)
 		{
 		 for (int i = 0; i < Consignment.length; i++) {
 			url_name = Consignment[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
 		
 		
 		if(GoodsIssued!=null)
 		{
 		 for (int i = 0; i < GoodsIssued.length; i++) {
 			url_name = GoodsIssued[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
 		if(Invoice!=null)
 		{
 		 for (int i = 0; i < Invoice.length; i++) {
 			url_name = Invoice[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
 		if(SalesReturn!=null)
 		{
 		 for (int i = 0; i < SalesReturn.length; i++) {
 			url_name = SalesReturn[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
 		if(CreditNotes!=null)
 		{
 		 for (int i = 0; i < CreditNotes.length; i++) {
 			url_name = CreditNotes[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
 		if(SalesReports!=null)
 		{
 		 for (int i = 0; i < SalesReports.length; i++) {
 			url_name = SalesReports[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
 		
		if(reports!=null)
 		{
 		 for (int i = 0; i < reports.length; i++) {
 			url_name = reports[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
		if(posreports!=null)
 		{
 		 for (int i = 0; i < posreports.length; i++) {
 			url_name = posreports[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
		
		if(activityLogs!=null)
 		{
 		 for (int i = 0; i < activityLogs.length; i++) {
 			url_name = activityLogs[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
		
		if(peppolIntegration!=null)
 		{
 		 for (int i = 0; i < peppolIntegration.length; i++) {
 			url_name = peppolIntegration[i];
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
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}

		
		if(accountingHome!=null)
 		{
 		 for (int i = 0; i < accountingHome.length; i++) {
 			url_name = accountingHome[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
		
		if(accountingMaster!=null)
 		{
 		 for (int i = 0; i < accountingMaster.length; i++) {
 			url_name = accountingMaster[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
		
		if(accountingTransactions!=null)
 		{
 		 for (int i = 0; i < accountingTransactions.length; i++) {
 			url_name = accountingTransactions[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}

		if(chartOfAccounts!=null)
 		{
 		 for (int i = 0; i < chartOfAccounts.length; i++) {
 			url_name = chartOfAccounts[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}

		if(apSummary!=null)
 		{
 		 for (int i = 0; i < apSummary.length; i++) {
 			url_name = apSummary[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}

		if(PaymentMade!=null)
 		{
 		 for (int i = 0; i < PaymentMade.length; i++) {
 			url_name = PaymentMade[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}

		if(PDCPaymentMade!=null)
 		{
 		 for (int i = 0; i < PDCPaymentMade.length; i++) {
 			url_name = PDCPaymentMade[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}

		if(PaymentReceived!=null)
 		{
 		 for (int i = 0; i < PaymentReceived.length; i++) {
 			url_name = PaymentReceived[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}

		if(PDCPaymentReceived!=null)
 		{
 		 for (int i = 0; i < PDCPaymentReceived.length; i++) {
 			url_name = PDCPaymentReceived[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}

		if(JournalEntry!=null)
 		{
 		 for (int i = 0; i < JournalEntry.length; i++) {
 			url_name = JournalEntry[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}

		if(ContraEntry!=null)
 		{
 		 for (int i = 0; i < ContraEntry.length; i++) {
 			url_name = ContraEntry[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}

		if(accountingReports!=null)
 		{
 		 for (int i = 0; i < accountingReports.length; i++) {
 			url_name = accountingReports[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
		
		
		if(pdaPurchaseTransaction!=null)
 		{
 		 for (int i = 0; i < pdaPurchaseTransaction.length; i++) {
 			url_name = pdaPurchaseTransaction[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
		
		if(pdaSalesTransaction!=null)
 		{
 		 for (int i = 0; i < pdaSalesTransaction.length; i++) {
 			url_name = pdaSalesTransaction[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}
		
		if(pdaInHouse!=null)
 		{
 		 for (int i = 0; i < pdaInHouse.length; i++) {
 			url_name = pdaInHouse[i];
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
 			userValue.add(Integer.toString(ub.getUserMenuACCPK(url_name)));

 			String sqlInsert = "insert into " + "[" + plant
 					+ "USER_LEVEL_ACCOUNTING" + "]" + "("
 					+ tl.getFieldString(userField) + ") values ("
 					+ tl.getValueString(userValue) + ")";
 			
 			if (!url_name.equals("on"))
 			sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
                        
 		} 
 		}		
		
 		boolean b = false;
 		if (action.equalsIgnoreCase("update")) {
 			//            int n = ub.deletePreviousRecords(level_name,"0");
 			int n = ub.deletePreviousRecordsaccounting(level_name, "0", company);
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
 			 response.sendRedirect("../useraccess/summary?RESULT=The User Level "+level_name+" has been Updated Sucessfully");
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
 	response.sendRedirect("displayResult2User.jsp");
 %> <%@ include file="footer.jsp"%>