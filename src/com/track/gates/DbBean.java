package com.track.gates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import com.track.constants.MLoggerConstant;
import com.track.util.MLogger;

/********************************************************************************************************
 * PURPOSE : The main class for establishing Database Connection using paramters
 * from the C:/props/track/config/track.properties file from the pool of
 * Connections from the class ConnectionPool
 *******************************************************************************************************/

/* ************Modification History*********************************
Oct 23 2014 Bruhan, Description: To include IB_MODIFICATION and OB_MODIFICATION
Nov 10 2014 Bruhan, Description: To include TO_MODIFICATION,LO_MODIFICATION and WO_MODIFICATION
Nov 11 2014,Bruhan,Description: To include DownloadWorkOrderTemplate
Nov 13 2014,Bruhan,Description: To include DownloadTransferAssigneeTemplate,DownloadLoanAssigneeTemplate and DownloadEmployeeTemplate
*/
@SuppressWarnings({"rawtypes"})
public class DbBean {

		public static String DATA_SOURCE, MY_CONTEXT_FACTORY, MY_CONTEXT_URL,
				ODBC_DRIVER, ODBC_URL, ODBC_URL_WMS, ODBC_URL_WMS1;
		public static String BEAN_ERROR_LOG_FILE, BEAN_ERR_LOG_DESC_FILE,
				URL_PKG_PREFIXES, APP_SERVER;
		public static String JASPER_INPUT, JASPER_OUTPUT, JASPER_XML,	
				JASPER_DOWNLOAD;
		
		public static String THREAD_SLEEPING;
		public static String USERNAME, PASSWORD, USERNAME_WMS, PASSWORD_WMS,
				USERNAME_WMS1, PASSWORD_WMS1;
		public static String USERTRANSACTION;
		public static String CURRENCYSYMBOL="";
		public static Connection connection;
		public static String DB_PROPS_FILE = MLoggerConstant.PROPS_FOLDER + "/track/config/track.properties";
		
		private final static boolean DEBUG = true;
//		private MLogger mLogger = new MLogger();
		private static String BroadCastMessage = "";
		public static String CountSheetUploadPath;
		public static String DownloadItemTemplate;
		public static String DownloadItemSupplierTemplate;
		public static String DownloadItemDescTemplate;
		public static String DownloadItemImgTemplate;
		public static String DownloadAddItemTemplate;
		
		public static String DownloadLocTemplate;
		public static String DownloadInvTemplate;
		public static String DownloadInvDisplay;
		public static String DownloadStockTakeTemplate;
		public static String DownloadRentalTemplate;
		public static String DownloadInBoundTemplate;
		public static String DownloadOutBoundTemplate;
		public static String DownloadTransferTemplate;
	    public static String DownloadSupTemplate;
	    public static String DownloadCustTemplate;
	    public static String DownloadAltrPrdTemplate;
        public static String DownloadWorkOrderTemplate;
        public static String DownloadEstimateTemplate;
		public static String ImportDataDateFormat;
		public static String NOOFDECIMALPTSFORCURRENCY;
		public static String NOOFDECIMALPTSFORTAX;
		public static String NOOFDECIMALPTSFORWEIGHT;
	    public static String LOCAL_CURRENCY;
	    public static String COMPANY_LOGO_PATH;
	    public static String COMPANY_SEAL_PATH;
	    public static String COMPANY_SIGNATURE_PATH;
	    public static String NO_LOGO_FILE; 
	    public static String LOGO_FILE; 
	    public static String COMPANY_ORDER_APP_BACKGROUND_PATH;  //imthi
        public static String UTILITY_DOWNLOAD_PATH; 
        public static String COMPANY_CATAGERY_PATH;
        public static String COMPANY_CATALOG_PATH;
        public static String COMPANY_BANNER_PATH;
        public static String COMPANY_EMPLOYEE_PATH;
        public static String COMPANY_USER_PATH;
 		public static String IB_MODIFICATION;
        public static String OB_MODIFICATION;
 		public static String TO_MODIFICATION;
        public static String LO_MODIFICATION;
        public static String WO_MODIFICATION;
        public static String EST_MODIFICATION;
        public static String DownloadTransferAssigneeTemplate;
        public static String DownloadLoanAssigneeTemplate;
        public static String DownloadEmployeeWithPayollTemplate;
        public static String DownloadEmployeeWithoutPayollTemplate;
        public static String DownloadHolidayMstTemplate;
        public static String DownloadSalaryMstTemplate;
        public static String DownloadEmployeeLeaveDetTemplate;
        public static String DownloadEmployeeSalaryDetTemplate;
        public static String DownloadProdBOMTemplate;
        public static String DownloadRoutingTemplate;
        public static String DownloadKittingTemplate;
        public static String DownloadSupplierDiscountTemplate;
        public static String DownloadCustomerDiscountTemplate;
        public static String DownloadKitBOMTemplate;
        public static String DownloadApplyLeaveForEmployeeTemplate;
    	private static int conCount = 0;
    	public static String COMPANY_SIGN_PATH; 
    	public static String CHANGEPASSWORDLOGO_IMAGE_PATH;
    	public static String COMPANY_MAIL_ATTACHMENT_PATH;
    	public static String DownloadOutboundProdRemarksTemplate;
    	public static String DownloadInboundProdRemarksTemplate;
     	public static String[] imageFormatsArray = { ".jpeg", ".png", ".jpg",".gif",".webp",".tiff",".tif",".psd",".raw",".bmp", ".dib",".jp2",".ind",".svg",".ai"};
     	public static String Locations;
     	public static String DownloadBillTemplate;
     	public static String DownloadInvoiceTemplate;
     	public static String DownloadMinMaxTemplate;
     	public static String DownloadOutletMinMaxTemplate;
     	public static String DownloadContactTemplate;

    /*************Modification History*********************************
	   Oct 23 2014 Bruhan, Description: To include IB_MODIFICATION and OB_MODIFICATION
	   Nov 10 2014 Bruhan, Description: To include TO_MODIFICATION,LO_MODIFICATION and WO_MODIFICATION
	   Nov 11 2014,Bruhan,Description: To include DownloadWorkOrderTemplate
	   Nov 13 2014,Bruhan,Description: To include DownloadTransferAssigneeTemplate,DownloadLoanAssigneeTemplate and DownloadEmployeeTemplate
	*/
	static {
		System.out.println(" ###### DbBean:loading propery file #####");
		Properties dbpr;
		InputStream dbip;

		try {
			dbip = new FileInputStream(new File(DB_PROPS_FILE));
			dbpr = new Properties();
			dbpr.load(dbip);
                        DATA_SOURCE = dbpr.getProperty("DATA_SOURCE"); // Database Driver
                        MY_CONTEXT_FACTORY = dbpr.getProperty("CONTEXT_FACTORY");
                        MY_CONTEXT_URL = dbpr.getProperty("CONTEXT_URL");
                        ODBC_URL = dbpr.getProperty("ODBC_URL");
                        ODBC_DRIVER = dbpr.getProperty("ODBC_DRIVER");
                        URL_PKG_PREFIXES = dbpr.getProperty("URL_PKG_PREFIXES");
                        APP_SERVER = dbpr.getProperty("APP_SERVER");
                        JASPER_OUTPUT = dbpr.getProperty("JASPER_OUTPUT");
                        JASPER_INPUT = MLoggerConstant.PROPS_FOLDER +  dbpr.getProperty("JASPER_INPUT");
                        BEAN_ERROR_LOG_FILE = MLoggerConstant.PROPS_FOLDER +  dbpr.getProperty("BEAN_ERROR_LOG_FILE");
                        BEAN_ERR_LOG_DESC_FILE = MLoggerConstant.PROPS_FOLDER +  dbpr.getProperty("BEAN_ERR_LOG_DESC_FILE");
                        JASPER_XML = dbpr.getProperty("JASPER_XML");
                        USERTRANSACTION = dbpr.getProperty("USERTRANACTION");
                        USERNAME = dbpr.getProperty("USERNAME");
                        PASSWORD = dbpr.getProperty("PASSWORD");
                        ODBC_URL_WMS = dbpr.getProperty("ODBC_URL_WMS");
                        ODBC_URL_WMS1 = dbpr.getProperty("ODBC_URL_WMS1");
                        THREAD_SLEEPING = dbpr.getProperty("THREAD_SLEEPING");
                        USERNAME_WMS = dbpr.getProperty("USERNAME_WMS");
                        PASSWORD_WMS = dbpr.getProperty("PASSWORD_WMS");
                        USERNAME_WMS1 = dbpr.getProperty("USERNAME_WMS1");
                        PASSWORD_WMS1 = dbpr.getProperty("PASSWORD_WMS1");
                        JASPER_DOWNLOAD = dbpr.getProperty("JASPER_DOWNLOAD");
                        CURRENCYSYMBOL = dbpr.getProperty("CURRENCYSYMBOL");
                        CountSheetUploadPath = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("CountSheetUploadPath");
                        DownloadItemTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadProductTemplate");
                        DownloadItemSupplierTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadItemSupplierTemplate");
                        DownloadItemDescTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadProductDescTemplate");
                        DownloadItemImgTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadProductImgTemplate");
                        DownloadAddItemTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadAddProductTemplate");
                        
		        		DownloadAltrPrdTemplate = MLoggerConstant.PROPS_FOLDER +dbpr.getProperty("DownloadAlternateProductTemplate");
                        DownloadLocTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadLocationTemplate");
                        DownloadInvTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadInventoryTemplate");
                        DownloadInvDisplay = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadInventoryDisplay");
                        DownloadStockTakeTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadStockTakeTemplate");
		       		    DownloadSupTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadSuplierTemplate");
		                DownloadCustTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadCustomerTemplate");
                        DownloadInBoundTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadPurchaseTemplate");
                        DownloadOutBoundTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadSalesTemplate");
                        DownloadRentalTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadRentalTemplate");
                        DownloadTransferTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadConsignmentTemplate");
                        ImportDataDateFormat =   dbpr.getProperty("ImportDataDateFormat");
                        NOOFDECIMALPTSFORCURRENCY = dbpr.getProperty("NOOFDECIMALPTSFORCURRENCY");
                        NOOFDECIMALPTSFORTAX = dbpr.getProperty("NOOFDECIMALPTSFORTAX");
                        NOOFDECIMALPTSFORWEIGHT = dbpr.getProperty("NOOFDECIMALPTSFORWEIGHT");
                        LOCAL_CURRENCY = dbpr.getProperty("LOCAL_CURRENCY");
                        COMPANY_LOGO_PATH = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("COMPANY_LOGO_PATH");
                        COMPANY_SEAL_PATH = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("COMPANY_SEAL_PATH");
                        COMPANY_SIGNATURE_PATH = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("COMPANY_SIGNATURE_PATH");
                        NO_LOGO_FILE = dbpr.getProperty("NO_LOGO_FILE");
                        LOGO_FILE = dbpr.getProperty("LOGO_FILE");
                        COMPANY_ORDER_APP_BACKGROUND_PATH = dbpr.getProperty("COMPANY_ORDER_APP_BACKGROUND_PATH");
                        COMPANY_CATAGERY_PATH = dbpr.getProperty("COMPANY_CATAGERY_PATH");
                        COMPANY_CATALOG_PATH = dbpr.getProperty("COMPANY_CATALOG_PATH");
                        COMPANY_BANNER_PATH = dbpr.getProperty("COMPANY_BANNER_PATH");
                        COMPANY_EMPLOYEE_PATH = dbpr.getProperty("COMPANY_EMPLOYEE_PATH");
                        COMPANY_USER_PATH = dbpr.getProperty("COMPANY_USER_PATH");
                        UTILITY_DOWNLOAD_PATH =  dbpr.getProperty("UTILITY_DOWNLOAD_PATH");
						IB_MODIFICATION=dbpr.getProperty("PURCHASE_MODIFICATION");
                        OB_MODIFICATION=dbpr.getProperty("SALES_MODIFICATION");
		      			TO_MODIFICATION=dbpr.getProperty("CONSIGNMENT_MODIFICATION");
                        LO_MODIFICATION=dbpr.getProperty("RENTAL_MODIFICATION");
                        WO_MODIFICATION=dbpr.getProperty("WOMODIFICATION");
                        EST_MODIFICATION=dbpr.getProperty("SALESEST_MODIFICATION");
                        DownloadTransferAssigneeTemplate=MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadConsignmentAssigneeTemplate");
                        DownloadLoanAssigneeTemplate=MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadRentalAssigneeTemplate");
                        DownloadEmployeeWithPayollTemplate=MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadEmployeeWithPayollTemplate");
                        DownloadEmployeeWithoutPayollTemplate=MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadEmployeeWithoutPayollTemplate");
                        DownloadHolidayMstTemplate=MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadHolidayMstTemplate");
                        DownloadSalaryMstTemplate=MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadSalaryMstTemplate");
                        DownloadEmployeeLeaveDetTemplate=MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadEmployeeLeaveDetTemplate");
                        DownloadEmployeeSalaryDetTemplate=MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadEmployeeSalaryDetTemplate");
                        DownloadApplyLeaveForEmployeeTemplate=MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadApplyLeaveForEmployeeTemplate");
                        DownloadProdBOMTemplate=MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadProductBOMTemplate");
                        DownloadEstimateTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadEstimateTemplate");
                        DownloadKittingTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadKittingTemplate");
                        DownloadSupplierDiscountTemplate= MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadSupplierDiscountTemplate");
                        DownloadCustomerDiscountTemplate= MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadCustomerDiscountTemplate");
                        DownloadKitBOMTemplate=dbpr.getProperty("DownloadKitBOMTemplate");
						COMPANY_SIGN_PATH = dbpr.getProperty("COMPANY_SIGN_PATH");
						COMPANY_MAIL_ATTACHMENT_PATH = dbpr.getProperty("COMPANY_MAIL_ATTACHMENT_PATH");
						DownloadOutboundProdRemarksTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadSalesProductRemarksTemplate");
						DownloadInboundProdRemarksTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadPurchaseProductRemarksTemplate");
						Locations=MLoggerConstant.PROPS_FOLDER+ dbpr.getProperty("Locations");
						DownloadBillTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadBillTemplate");
						DownloadInvoiceTemplate = MLoggerConstant.PROPS_FOLDER + dbpr.getProperty("DownloadInvoiceTemplate");
						DownloadMinMaxTemplate = MLoggerConstant.PROPS_FOLDER +dbpr.getProperty("DownloadProductMinMaxTemplate");
						DownloadOutletMinMaxTemplate = MLoggerConstant.PROPS_FOLDER +dbpr.getProperty("DownloadProductOutletMinMaxTemplate");
						DownloadContactTemplate = MLoggerConstant.PROPS_FOLDER +dbpr.getProperty("DownloadContactTemplate");
					
                        
			log(" ####################### INITIALISATION ####################### ");
			log("Data Source :" + DATA_SOURCE + "\n\n" + "Factory : "
					+ MY_CONTEXT_FACTORY);
			log(" ####################### INITIALISATION ####################### ");

		} catch (FileNotFoundException fnfe) {

			log("################### Exception  ################ ");
			log("Propery file " + DB_PROPS_FILE + " not found");
			log("################### Exception  ################ ");
			writeError("DbBean", "static()", fnfe);
		} catch (Exception e) {
			log("################### Exception  ################ ");
			log("Propery file " + DB_PROPS_FILE + " not found");
			log("################### Exception  ################ ");
		}
	}

	public static void setBroadCastMessage(String msg) {
		BroadCastMessage = msg;
	}

	public static String getBroadCastMessage() {
		return BroadCastMessage;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for gettting Database Connection from the Connection
	 * Pool PARAMETER 1 : Nil RETURNS : Connection Object
	 *******************************************************************************************************/

	public static synchronized Connection getConnection()
			throws NamingException, SQLException {
		Context ctx = getInitialContext();
		MLogger.info("system", "system", "Opening connection : "
				+ (conCount + 1));
		DataSource dataSource = (DataSource) ctx.lookup(DATA_SOURCE);
		try {
			conCount++;
			connection = dataSource.getConnection();
		} catch (Exception e) {

			MLogger.log(0, "" + e.getMessage());
		}
		MLogger.info("system", "system", "Total connection opened : "
				+ conCount);
		return connection;

	}

	/********************************************************************************************************
	 * PURPOSE : Method for gettting Database Connection from the Connection
	 * Pool PARAMETER 1 : Nil RETURNS : Connection Object
	 *******************************************************************************************************/
	public static synchronized Connection getODBCConnection()
			throws NamingException, SQLException {
		Connection con = null;
		try {
			Class.forName(ODBC_DRIVER);
			con = DriverManager.getConnection(ODBC_URL, USERNAME, PASSWORD);
		} catch (Exception e) {
			log("getODBCConnection :: Connection :" + con + " :" + e.toString());
		}
		return con;
	}

	public static synchronized Connection getODBCConnection4BPCS_MASTERS()
			throws NamingException, SQLException {
		Connection con = null;
		try {
			Class.forName(ODBC_DRIVER);
			con = DriverManager.getConnection(ODBC_URL_WMS, USERNAME_WMS,
					PASSWORD_WMS);
		} catch (Exception e) {
			System.out.println("getODBCConnection4BPCS_MASTERS :: Connection :"
					+ con + " :" + e.toString());
		}
		return con;
	}

	/**
	 * *************************************************************************
	 * **************************** Odbc Connection for Second Database
	 * 
	 * @param connection
	 * @param statement
	 *****************************************************************************************************/

	public static synchronized Connection getODBCConnection4BPCS_MASTERS1()
			throws NamingException, SQLException {
		Connection con = null;
		try {
			Class.forName(ODBC_DRIVER);
			con = DriverManager.getConnection(ODBC_URL_WMS1, USERNAME_WMS1,
					PASSWORD_WMS1);
		} catch (Exception e) {
			System.out
					.println("getODBCConnection4BPCS_MASTERS1 :: Connection :"
							+ con + " :" + e.toString());
		}
		return con;
	}

	/**
	 * *************************************************************************
	 * **************************** PURPOSE : Method for closing a open Database
	 * Connection That is, returning to the pool of connections PARAMETER :
	 * Connection to be freed RETURNS : void
	 *******************************************************************************************************/
	public static synchronized void closeConnection(Connection connection,
			PreparedStatement statement) {
		MLogger.info("", "", "Closing connection : " + conCount);
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			System.out
					.println("DbBean :: closeConnection() :Error closing connection in Statement Pool : "
							+ e);
			log(e.toString());
			writeError("DbBean", "closeConnection()",
					"Error closing connection in Statement Pool : " + e);

		}
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out
					.println("DbBean :: closeConnection() :Error closing connection in Connection Pool : "
							+ e);
			log(e.toString());
			writeError("DbBean", "closeConnection()",
					"Error closing connection in Connection Pool : " + e);

		}
		conCount--;
		MLogger.info("", "", "Total Connection available : " + conCount);
	}

	/**
	 * *************************************************************************
	 * **************************** PURPOSE : Method for closing a open Database
	 * Connection PARAMETER : Connection to be freed RETURNS : void
	 *******************************************************************************************************/
	public static synchronized void closeConnection(Connection connection,
			Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			System.out
					.println("DbBean :: closeConnection() : Error closing connection in Statement Pool : "
							+ e);
			log(e.toString());
			writeError("DbBean", "closeConnection()",
					"Error closing connection in Statement Pool : " + e);
		}
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out
					.println("DbBean :: closeConnection() : Error closing connection in Connection Pool : "
							+ e);
			log(e.toString());
			writeError("DbBean", "closeConnection()",
					"Error closing connection in Connection Pool : " + e);
		}
	}

	public static synchronized void closeConnection(Connection connection,
			CallableStatement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			log(e.toString());
			writeError("DbBean", "closeConnection()",
					"Error closing connection in Statement Pool : " + e);
		}
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			log(e.toString());
			writeError("DbBean", "closeConnection()",
					"Error closing connection in Connection Pool : " + e);
		}
	}

	/**
	 * *************************************************************************
	 * **************************** PURPOSE : Method for closing a open Database
	 * Connection That is, returning to the pool of connections PARAMETER :
	 * Connection to be freed RETURNS : void
	 *******************************************************************************************************/
	public static synchronized void closeODBCConnection(Connection connection,
			PreparedStatement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			log(e.toString());
			writeError("DbBean", "closeConnection()",
					"Error closing connection in Connection Pool : " + e);

		}
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			log(e.toString());
			writeError("DbBean", "closeConnection()",
					"Error closing connection in Connection Pool : " + e);

		}
	}

	public static synchronized void closeConnection(Connection connection) {
		MLogger.info("system", "system", "Closing connection : " + conCount);
		try {
			if (connection != null)
				connection.close();
		} catch (Exception e) {
			log(e.toString());
			writeError("DbBean", "closeConnection()",
					"Error closing connection in Connection Pool : " + e);
		}

		conCount--;
		MLogger.info("system", "system", "Total Connection available : "
				+ conCount);
	}

	public static synchronized void closeODBCConnection(Connection connection) {
		try {
			if (connection != null)
				connection.close();
		} catch (Exception e) {
			log(e.toString());
			writeError("DbBean", "closeConnection()",
					"Error closing connection in Connection Pool : " + e);
		}
	}

	/**
	 * PURPOSE : Method for writing errors to error log - with Exception Object
	 * PARAMETER 1 : Name of the Bean where the error occured PARAMETER 2 : Name
	 * of the Method where the error occured PARAMETER 3 : The Exception object
	 * caught RETURNS : void
	 *******************************************************************************************************/
	public static void writeError(String beanName, String methodName,
			Exception e) {

		Class c = e.getClass();
		String error;
		if (c.getSuperclass().getName() == "java.sql.SQLException") {
			SQLException sq = (SQLException) e;
			error = "SQLSTATE = " + sq.getSQLState() + " ERROR-CODE = "
					+ sq.getErrorCode() + " " + sq.toString();
		} else {
			error = e.toString();
		}
		try {
			e.printStackTrace(new PrintStream(new FileOutputStream(
					BEAN_ERR_LOG_DESC_FILE, true)));
		} catch (Exception es) {
			log("@@@@ Could not write to " + BEAN_ERR_LOG_DESC_FILE);
		}

		writeError(beanName, methodName, error);
	}

	/********************************************************************************************************
	 * PURPOSE : Method for writing errors to error log - all String PARAMETER 1
	 * : Name of the Bean where the error occured PARAMETER 2 : Name of the
	 * Method where the error occured PARAMETER 3 : The description of Exception
	 * caught RETURNS : void
	 *******************************************************************************************************/
	public static void writeError(String beanName, String methodName,
			String error) {

		String time = "";
		Date dt = new java.util.Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dt);
		SimpleDateFormat formatter2 = new SimpleDateFormat(
				"dd/MM/yyyy 'at' HH:mm:ss");
		time = formatter2.format(dt);

		StringReader sr = new StringReader("\n[Time] : " + time
				+ " [Origin] : " + beanName + " [Method] : " + methodName
				+ " [Error] : " + error);
		FileOutputStream fo;
		int n;
		try {
			fo = new FileOutputStream(BEAN_ERROR_LOG_FILE, true);
			fo.write(13); // next line
			while ((n = sr.read()) != -1) {
				fo.write(n);
			}

			System.out.println();
			fo.flush();
			fo.close();
			Runtime.getRuntime().gc(); // Invoking the garbage collecting thread
		} catch (FileNotFoundException fnfe) {
			log("Could not find/open the " + BEAN_ERROR_LOG_FILE + " file..");
		} catch (Exception e) {
			log("Unable to write error to Error file .. ");
			log(e.toString());
		}
	}

	/************************************************************************
	 * PURPOSE : TO GET THE INITIAL CONTEXT PARAMS : RETURNS : CONTEXT
	 ************************************************************************/

	public static Context getInitialContext() throws NamingException {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, MY_CONTEXT_FACTORY);
		props.put(Context.PROVIDER_URL, MY_CONTEXT_URL);
		if (APP_SERVER.equalsIgnoreCase("JBOSS")) {
			props.put(Context.URL_PKG_PREFIXES, URL_PKG_PREFIXES);
		}
		return new InitialContext(props);
	}

	/**
	 * @method : getUserTranaction()
	 * @description : Look up the User Transaction
	 * @return UserTransaction
	 * @throws NamingException
	 */
	public static UserTransaction getUserTranaction() throws NamingException {
		UserTransaction userTrans = null;
		Context ctx = DbBean.getInitialContext();
		userTrans = (UserTransaction) ctx.lookup(USERTRANSACTION);
		return userTrans;
	}

	public static UserTransaction getBPCSUserTranaction()
			throws NamingException {
		UserTransaction userTrans = null;
		Context ctx = DbBean.getInitialContext();
		userTrans = (UserTransaction) ctx.lookup(USERTRANSACTION);
		return userTrans;
	}

	public static boolean CommitTran(UserTransaction ut) {
		boolean flag = false;
		try {

			ut.commit();
			flag = true;

		} catch (Exception e) {
			flag = false;
			System.out.println(" DbBean :: CommitTran() :: ");
			e.printStackTrace();
		}
		return flag;
	}

	public static boolean RollbackTran(UserTransaction ut) {
		boolean flag = false;
		try {
			MLogger.log(0, "DbBean : Transaction rollback : Start");
			ut.rollback();
			flag = true;
			MLogger.log(0, "DbBean : Transaction rollback : Ends");
		} catch (Exception e) {
			flag = false;
			System.out.println(" DbBean :: RollbackTran() :: ");
			e.printStackTrace();
		}
		return flag;
	}

	private static void log(String s) {
		if (DEBUG) {
			System.out.println("DB Bean : " + s);
		}
	}

}
