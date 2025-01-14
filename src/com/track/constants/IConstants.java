package com.track.constants;

 /* * ************Modification History*********************************
Sep 25 2014 Bruhan, Description: To include RECVDATE,ISSUEDATE
*/
public interface IConstants extends IDBConstants {
	public final static String PLANT_VAL = "SIS"; // should be changed for every
	// product
	public final static String ISACTIVE = "ISACTIVE"; // master rec to check
	public final static String ISKITTING = "USERFLD1";
	// active or inactive
	public final static String SUCCESS_COLOR = "maingreen";
	public final static String FAILED_COLOR = "mainred";
	//
	public static final String ITEM_HOME = "EJBItemSes";
	public static final String ITEM_LOC_HOME = "EJBItemLoc";
	public static final String ITEM_MAP_HOME = "EJBItemMap";
	public static final String INV_HOME = "EJBInvSes";
	public static final String TBLCONTROL_HOME = "EJBTblControl";
	public static final String MOVHIS_HOME = "EJBMovHis";
	public static final String CUSTOMER_HOME = "EJBCustomer";
	public static final String CYCLECOUNT_HOME = "EJBCycleCount";
	public static final String PO_HOME = "EJBPO";
	public static final String SO_HOME = "EJBSO";
	public static final String REPORT_HOME = "EJBREPORT";

	// specify the function names
	public static final String DIR_INBOUND = "IB";
	public static final String DIR_OUTBOUND = "OB";

	public static final String RECVHIS_FUNC = "RECV";
        public static final String RECVBYRANGE_FUNC = "RECV_BY_RANGE";
	public final static String COST = "COST";
	public final static String MIN_S_PRICE="MINSPRICE";
	public static final String PLANT = "PLANT";
	public static final String TABLE_NAME = "TABLE_NAME";
	public static final String ITEM = "ITEM";
	public static final String ITEM_DESC = "ITEMDESC";
        public static final String ALTERNATE_ITEM = "ALTERNATE_ITEM";
	public static final String LOC = "LOC";
	public static final String LOC1 = "LOC1";
	public static final String LOC2 = "LOC2";
	public static final String TRAN_TYPE = "TRAN_TYPE";
	public static final String LOGIN_USER = "LOGIN_USER";
	public static final String JOB_NUM = "JOB_NUM";
	public static final String FROMLOC = "FROMLOC";
	public static final String TOLOC = "TOLOC";
	public static final String TEMP_LOC = "TEMP_TO";
	public static final String INV_BATCH = "INV_BATCH";
	public static final String INV_QTY = "INV_QTY";
	public static final String INV_EXP_DATE = "INV_EXP_DATE";
	public static final String DELETE_FLAG = "DELETE_FLAG";
	public static final String DISCOUNT = "DISCOUNT";
	public static final String PRODGST = "PRODGST";
	public static final String BATCH = "BATCH";
	public static final String BATCH_ID = "BATCH_ID";
	public static final String ORD_QTY = "ORDQTY";
	public static final String RECV_QTY = "RECQTY";
	public static final String NONSTKFLAG = "NONSTKFLAG";
	public static final String CATLOGPATH = "CATLOGPATH";
	public static final String NONSTKTYPEID = "NONSTKTYPEID";
	public static final String NETWEIGHT = "NETWEIGHT";
	public static final String GROSSWEIGHT = "GROSSWEIGHT";
	public static final String TOTALNETWEIGHT = "TOTALNETWEIGHT";
	public static final String TOTALGROSSWEIGHT = "TOTALGROSSWEIGHT";
	public static final String NETDIMENSION = "NETDIMENSION";
	public static final String NETPACKING = "NETPACKING";
	public static final String DNNO = "DNNO";
	public static final String PLNO = "PLNO";
	public static final String HSCODE = "HSCODE";
	public static final String COO = "COO";
	public static final String NAME = "NAME";
	public static final String DESGINATION = "DESGINATION";
	public static final String TELNO = "TELNO";
	public static final String HPNO = "HPNO";
	public static final String EMAIL = "EMAIL";
	public static final String FAX = "FAX";
	public static final String REMARKS = "REMARKS";
	
	public static final String SHIP_CONTACTNAME = "SHIPCONTACTNAME";
	public static final String SHIP_DESGINATION = "SHIPDESGINATION";
	public static final String SHIP_WORKPHONE = "SHIPWORKPHONE";
	public static final String SHIP_HPNO = "SHIPHPNO";
	public static final String SHIP_EMAIL = "SHIPEMAIL";
	public static final String SHIP_COUNTRY_CODE = "SHIPCOUNTRY_CODE";
	public static final String SHIP_COUNTRY = "SHIPCOUNTRY";
	public static final String SHIP_ADDR1 = "SHIPADDR1";
	public static final String SHIP_ADDR2 = "SHIPADDR2";
	public static final String SHIP_ADDR3 = "SHIPADDR3";
	public static final String SHIP_ADDR4 = "SHIPADDR4";
	public static final String SHIP_STATE = "SHIPSTATE";
	public static final String SHIP_ZIP = "SHIPZIP";
	public static final String SAME_AS_CONTACT_ADDRESS = "SAMEASCONTACTADDRESS";
	
	public static final String TRANSPORT = "TRANSPORT_MODE";
	public static final String TRANSPORTID = "TRANSPORTID";
	public static final String DNPLREMARKS = "DNPLREMARKS";
	public static final String PAYTERMS = "PAY_TERMS";
	public static final String payment_terms = "PAYMENT_TERMS";
	public static final String PAYINDAYS = "PAY_IN_DAYS";
	public static final String EXPIREDATE = "EXPIREDATE";
	public static final String INVOICENUM = "INVOICENO";
	public static final String RECIEVEDATE = "RECVDATE";
	public static final String ORDERTYPE = "ORDERTYPE";
	public static final String CREDITLIMIT = "CREDITLIMIT";
	public static final String ISSHOWBYPRODUCT = "ISSHOWBYPRODUCT";
	public static final String ISSHOWBYCATEGORY = "ISSHOWAPPCATEGORYIMAGE";
	public static final String ISCREDITLIMIT = "ISCREDITLIMIT";
	public static final String CREDIT_LIMIT_BY = "CREDIT_LIMIT_BY";
	
	public static final String DIRTYPE = "DIRTYPE";
	public static final String LNNO = "LNNO";
	public static final String ORDNUM = "ORDNUM";
	public static final String QTY = "QTY";
	public static final String ISMEMBER = "ISMEMBER";
	public static final String UOM = "UOM";
	
	public static final String GST_TYPE = "GSTTYPE";
	public static final String TAXTREATMENT = "TAXTREATMENT";
        
    public static final String ORDERNO = "ORDERNO";
    public static final String ORDERLNO = "ORDERLNO";
    
    public static final String REASONCODE = "RSNCODE";
    
    // inbound order constants

  //Start added by Bruhan for Email Notification on 11 July 2012.
    /**
     * @deprecated Since 28-Apr-2021. Use PURCHSE_ORDER Instead.
     * @see IConstants#PURCHASE_ORDER
     */
    public static final String INBOUND_ORDER = "Inbound Order";
  //Start added by Bruhan for Email Notification on 11 July 2012.
    public static final String INBOUND = "INBOUND";
    public static final String MULTIPOEST = "MULTIPOEST"; //RESVI
    public static final String POEST = "POEST"; //RESVI
    public static final String IN_PONO = "PONO";
    public static final String IN_ORDERTYPE = "ORDERTYPE";
    public static final String IN_COLLECTION_DATE = "CollectionDate";
    public static final String IN_COLLECTION_TIME = "CollectionTime";
    public static final String IN_REMARK1 = "Remark1";
    public static final String IN_REMARK2 = "Remark2";
    public static final String IN_REMARK3 = "Remark3";
    public static final String IN_INBOUND_GST = "INBOUND_GST";
    public static final String IN_CUST_CODE = "CustCode";
    public static final String IN_OUTLET_CODE = "OutletCode";
    public static final String IN_ITEM = "ITEM";
    public static final String IN_MANUFACTURER = "USERFLD4";
    public static final String IN_SUPPLIER = "USERFLD3";
   
    public static final String IN_QTYOR = "QTYOR";
    public static final String IN_UNITMO = "UNITMO";
    public static final String IN_UNITCOST = "UNITCOST";
    public static final String IN_CURRENCYID = "CURRENCYID";
    public static final String IN_STATUS = "STATUS";
    public static final String IN_CUST_NAME = "CustName";
    public static final String IN_OUTLET_NAME = "OutletName";
    public static final String IN_PERSON_IN_CHARGE = "PersonInCharge";
    public static final String IN_REF_NO = "JobNum";
    
    public static final String IN_POLNNO = "POLNNO";
    public static final String IN_LNSTAT = "LNSTAT";
    public static final String IN_ITEM_DES = "ItemDesc";
    public static final String IN_ITEM_DET_DES = "USERFLD1";
    public static final String IN_USERFLD2 = "USERFLD2";
    public static final String IN_TRANDATE = "TRANDATE";
	public static final String IN_LOCALEXPENSES = "LOCALEXPENSES";
	public static final String POHDR_EMPNO = "EMPNO";
    
    // outbound order constants

  //Start added by Bruhan for Email Notification on 11 July 2012.
	/**
     * @deprecated Since 28-Apr-2021. Use SALES_ORDER Instead.
     * @see IConstants#SALES_ORDER
     */
    public static final String OUTBOUND_ORDER = "Outbound Order";
  //End added by Bruhan for Email Notification on 11 July 2012.
    public static final String OUTBOUND = "OUTBOUND";
    public static final String OUT_DONO = "DONO";
    public static final String OUT_STATUS = "STATUS";
    public static final String OUT_ORDERTYPE = "ORDERTYPE";
    public static final String OUT_COLLECTION_DATE = "CollectionDate";
    public static final String OUT_COLLECTION_TIME = "CollectionTime";
    public static final String OUT_REMARK1 = "Remark1";
    public static final String OUT_REMARK2 = "Remark2";
	public static final String OUT_REMARK3 = "Remark3";
    public static final String OUT_CUST_CODE = "CustCode";
    public static final String OUT_ITEM = "ITEM";
    public static final String OUT_CUSTOMER = "USERFLD3";
    public static final String OUT_USERFLD2 = "USERFLD2";
    public static final String OUT_QTYOR = "QTYOR";
    public static final String OUT_UNITMO = "UNITMO";
    public static final String OUT_UNITCOST = "UNITPRICE";
    public static final String OUT_CUST_NAME = "CustName";
    public static final String OUT_PERSON_IN_CHARGE = "PersonInCharge";
    public static final String OUT_REF_NO = "JobNum";
    
    public static final String OUT_ITEM_DES = "ItemDesc";
    public static final String OUT_ITEM_DET_DES = "USERFLD1";
    public static final String OUT_NONSTKTYPEID = "NONSTKTYPEID";
    public static final String OUT_DOLNNO = "DOLNNO";
    public static final String OUT_LNSTAT = "LNSTAT";
    public static final String OUT_PICK_STAT = "PickStatus";
    public static final String OUT_TRANDATE = "TRANDATE";
    public static final String OUT_CURRENCYID = "CURRENCYID";
    public static final String PCKLISTNUM = "PCKLISTNUM";
    public static final String SHIPMENTNO = "SHIPMENTNO";
    public static final String OUT_OUTBOUND_GST = "OUTBOUND_GST";
	public static final String ACCOUNTING_APPLICATION_URL = "/trackaccounting/";
	public static final String INVENTORY_APPLICATION_URL = "/track/";
 
    
    // Transfer order constants

  //Start added by Bruhan for Email Notification on 11 July 2012.
	/**
     * @deprecated Since 28-Apr-2021. Use CONSIGNMENT_ORDER Instead.
     * @see IConstants#CONSIGNMENT_ORDER
     */
    public static final String TRANSFER_ORDER = "Transfer Order";
  //End added by Bruhan for Email Notification on 11 July 2012.
    public static final String TR_TONO = "TONO";
    public static final String TR_TOLNNO = "TOLNNO";
    public static final String TR_ASSIGNEE = "CustCode";
    public static final String TR_FROM_LOC = "FROMWAREHOUSE";
    public static final String TR_TO_LOC = "TOWAREHOUSE";
    public static final String TR_ORDER_DATE = "CollectionDate";
    public static final String TR_ORDER_TIME = "CollectionTime";
    public static final String TR_REMARK1 = "Remark1";
    public static final String TR_REMARK2 = "Remark2";
    public static final String TR_REMARK3 = "Remark3";
    public static final String TR_ITEM = "ITEM";
    public static final String TR_QTY = "QTYOR";
    public static final String TR_RECVQTY = "QTYRC";

    public static final String TR_STATUS = "STATUS";
    public static final String TR_ASSIGNEE_NAME = "CustName";
    public static final String TR_ASSIGNEE_PERSONINCHARGE = "PersonInCharge";
    public static final String TR_ASSIGNEE_ADD1 = "Address";
    public static final String TR_ASSIGNEE_ADD2 = "Address2";
    public static final String TR_ASSIGNEE_ADD3 = "Address3";
    public static final String TR_ITEM_DESC = "ItemDesc";
    public static final String TR_ITEM_DET_DESC = "USERFLD1";
    public static final String TR_ASSIGNEE_DESC = "USERFLD3";
    public static final String TR_ITEM_UOM = "UNITMO";
    public static final String TR_LNSTAT = "LNSTAT";
    public static final String TR_PICK_STAT = "PickStatus";
    public static final String TR_TRANDATE = "TRANDATE";
    public static final String TR_REF_NO = "JobNum";
    public static final String TR_CURRENCYID = "CURRENCYID";
    public static final String TR_CONSIGNMENT_GST = "CONSIGNMENT_GST";
    public static final String TR_UNITCOST = "UNITPRICE";
    public static final String TR_ORDERTYPE = "ORDERTYPE";
    public static final String TR_NONSTKTYPEID = "NONSTKTYPEID";
	//Start added by Bruhan for bulk receiving on 3 Aug 2012.
    public static final String TR_RECV_TYPE = "recvType";
    //End added by Bruhan for for bulk receiving on 3 Aug 2012.
    public static final String FROMTIME = "FROMTIME";
    public static final String TOTIME = "TOTIME";
    public static final String MERIDIEN = "MERIDIEN";
    
    public static final String  OUT_CONTAINER = "CONTAINER";
    
    public static final String PCKLIST = "PCKLIST";
    
    public static final String FREIGHTFORWARDERNO = "FREIGHT_FORWARDERNO";
    public static final String VENDNO = "VENDNO";
    public static final String OUTLET = "OUTLET";
    public static final String OUTLETNO = "OUTLET";
    public static final int DECIMALPTS =3;
    
    public static final String TRANSFER ="TRANSFER";
    public static final String LOAN ="LOAN";
    
	 public final static String LOANHDR_ORDNO = "ORDNO";
	public final static String LOANHDR_CUST_NAME = "custName";
	public final static String LOANHDR_CUST_CODE = "custCode";
	public final static String LOANHDR_DELDATE = "DELDATE";
	public final static String LOANHDR_REMARK1 = "remark1";
	public final static String LOANHDR_REMARK2 = "remark2";
	public final static String LOANHDR_JOB_NUM = "jobNum";
	public final static String LOANHDR_PERSON_INCHARGE = "personInCharge";
	public final static String LOANHDR_CONTACT_NUM = "contactNum";
	public final static String LOANHDR_ADDRESS = "address";
	public final static String LOANHDR_ADDRESS2 = "address2";
	public final static String LOANHDR_ADDRESS3 = "address3";
	public final static String LOANHDR_COL_DATE = "collectionDate";
	public final static String LOANHDR_COL_TIME = "collectionTime";
	public final static String LOANHDR_GST  = "RENTAL_GST";
	public final static String LOANHDR_EMPNO  = "EMPNO";
	// LOANDET
	public final static String LOANDET_ORDNO = "ORDNO";
	public final static String LOANDET_ORDLNNO = "ORDLNNO";
	public final static String MOVHIS_ORDLNO = "LNNO";
	public final static String LOANDET_ITEM = "ITEM";
	public final static String LOANDET_ITEMDESC = "ITEMDESC";
	public final static String LOANDET_LNSTATUS = "LNSTAT";
	public final static String LOANDET_PICKSTATUS = "PICKSTATUS";
	public final static String LOANDET_RECVSTATUS = "RECVSTATUS";
	public final static String LOANDET_ITEM_DESC = "USERFLD1";
	public final static String LOANDET_JOB_NUM = "USERFLD2";
	public final static String LOANDET_CUST_NAME = "USERFLD3";
	public final static String LOANDET_QTYOR = "QTYOR";
	public final static String LOANDET_QTYIS = "QTYIS";
	public final static String LOANDET_QTYRC = "QTYRC";
	public final static String LOANDET_RENTALPRICE = "RENTALPRICE";
	public final static String LOANDET_PRODGST = "PRODGST";
	public final static String LOANDET_UNITMO = "UNITMO";
    
    public static final String PARENT_LOC ="PARENT_PRODUCT_LOC";
    public static final String PARENT_BATCH ="PARENT_PRODUCT_BATCH";
    public static final String CHILD_PRODUCT ="CHILD_PRODUCT";
    public static final String CHILD_LOC ="CHILD_PRODUCT_LOC";
    public static final String CHILD_BATCH ="CHILD_PRODUCT_BATCH";
    
    

          
       
       
    //---Added by Bruhan on March 10 2014, Description: Include for LabelPrint
 	  public final static String LABEL_PRINT_TYPE = "TYPE";
 	  public final static String REFNO = "REFNO";
 	  
   //---End Added by Bruhan on March 10 2014, Descript publicion: Include for LabelPrint
 	  static final String RECVDATE ="RECVDATE";
 	 static final String ISSUEDATE ="ISSUEDATE";
 	 
 	 
 	public static final String ESTIMATE = "ESTIMATE";  
 	public static final String PROJECT = "PROJECT"; // imti project 
 	public static final String ESTIMATE_ORDER = "Sales Estimate Order";
 	public static final String ESTIMATE_ORDER_UCSO = "Sales Estimate Order UCSO";
 	
 	public static final String EST_ESTNO = "ESTNO";
 	public static final String EST_ESTLNNO = "ESTLNNO";
 	public static final String EST_REF_NO = "JobNum";
 	public static final String EST_COLLECTION_DATE = "CollectionDate";
    public static final String EST_COLLECTION_TIME = "CollectionTime";
    public static final String EST_REMARK1 = "Remark1";
    public static final String EST_EMPNO = "EMPNO";
    public static final String EST_EXPIRE_DATE = "EXPIREDATE";
    public static final String EST_CUST_CODE = "CustCode";
    public static final String EST_ITEM = "ITEM";
    public static final String EST_QTYOR = "QTYOR";
    public static final String EST_UNITMO = "UNITMO";
	 public static final String EST_ORDERTYPE = "ORDERTYPE";
    public static final String EST_UNITCOST = "UNITPRICE";
    public static final String EST_CURRENCYID = "CURRENCYID";
    
    public static final String BYCOST = "BYCOST";
    public static final String BYPERCENTAGE = "BYPERCENTAGE";
    public static final String CatalogImagePath = "CatalogImagePath";
    public static final String CNAME = "CNAME";
    public static final String INVID = "ID";
    public static final String INVOICENO = "INVOICENO";
    public static final String GRNO= "GRNO";
    public static final String DIMENSION= "DIMENSION";
    public static final String PACKING= "PACKING";
	public static final String ISPDA= "ISPDA";
	public final static String VINNO = "VINNO";
	public final static String MODEL = "MODEL";
	
	/* Main Menu's*/
	public final static String SETTINGS = "Settings";
	public final static String PURCHASE = "Purchase";
	/**
     * @deprecated Since 28-Apr-2021. Use ESTIMATE_ORDER Instead.
     * @see IConstants#ESTIMATE_ORDER
     */
	public final static String SALES_ESTIMATE = "Sales Estimate";
	public final static String SALES = "Sales";
	public final static String CONSIGNMENT = "Consignment";
	public final static String MULTIPURCHASEESTIMATE = "Multi Purchase Estimate"; //Resvi
	public final static String PURCHASEESTIMATE = " Purchase Estimate"; //Resvi
	public final static String ACCOUNTING = "Accounting";
	public final static String TAX = "Tax";
	public final static String PAYROLL = "Payroll";
	public final static String RENTAL_CONSIGNMENT = "Rental / Consignment";
	public final static String IN_HOUSE = "In House";
	public final static String REPORTS = "Reports";
	public final static String INTEGRATIONS = "Integrations";
	public final static String BARCODE = "BARCODE";
	
	/* Sub Menu's*/
	public final static String USER_ADMIN = "User Admin";
	public final static String SYSTEM_MASTER = "System Master";
	public final static String PRODUCT_PROMOTION = "Product Promotion";
	public final static String CATEGORY_PROMOTION = "Category Promotion";
	public final static String BRAND_PROMOTION = "Brand Promotion";
	public final static String SYSTEM_ADMIN = "System Admin";
	public final static String ORDER_ADMIN = "Order Admin";
	public final static String PRINTOUT_CONFIGURATION = "Print Configuration";
	
	public final static String PURCHASE_ORDER = "Purchase Order";
    public final static String PURCHASE_ORDER_AR = "Purchase Order AR";
	public final static String PURCHASE_TRANSACTION = "Purchase Transaction";
	public final static String EXPENSES = "Expenses";
	public final static String POSEXPENSES = "POS Expenses";
	public final static String APEXPENSES = "AP Expenses";
	public final static String RECEIPT = "Receipt";
	public final static String BILL = "Bill";
	public final static String PAYMENT = "Payment";
	public final static String PDC_PAYMENT = "PDC Payment";
	public final static String PRODUCT_RETURN = "Product Return";
	public final static String PRODUCT_RECEIVE = "Product Receive";
	public final static String PURCHASE_RETURN = "Purchase Return";
	public final static String PURCHASE_CREDIT_NOTES = "Purchase Credit Notes";
	public final static String PURCHASE_REPORTS = "Purchase Reports";
	
	public final static String SALES_ESTIMATE_SUB_MENU = "Sales Estimate";
	public final static String ESTIMATE_REPORTS = "Estimate Reports";
	public final static String MULTI_PURCHASE_ESTIMATE = "Multi Purchase Estimate"; //Resvi
	public final static String PURCHASE_ESTIMATE = "Purchase Estimate"; //Resvi
	
	public final static String SALES_ORDER = "Sales Order";
	public final static String SALES_ORDER_API = "Sales Order API";
	public final static String CONSIGNMENT_ORDER = "Consignment Order";
	public final static String CONSIGNMENT_ORDER_API = "Consignment Order API";
	public final static String SALES_TRANSACTION = "Sales Transaction";
	public final static String GOODS_ISSUED = "Goods Issued";
	public final static String INVOICE = "Invoice";
	public final static String PAYSLIP = "Payslip";
	public final static String DELIVERY = "Delivery";
	public final static String MINMAX = "MINMAX";
	public final static String PAYMENT_RECEIVED = "Payment Received";
	public final static String PDC_PAYMENT_RECEIVED = "PDC Payment Received";
	public final static String SALES_RETURN = "Sales return";
	public final static String CREDIT_NOTES = "Credit Notes";
	public final static String SALES_REPORTS = "Sales Reports";
	
	public final static String CONSIGNMENT_TRANSACTION = "Consignment Transaction";
	public final static String CONSIGNMENT_REPORTS = "Consignment Reports";
	public final static String SHIFT = "Shift";
	public final static String BANKING = "Banking";
	public final static String CHART_OF_ACCOUNTS = "Chart Of Accounts";
	public final static String JOURNAL_ENTRY = "Journal Entry";
	public final static String CONTRA_ENTRY = "Contra Entry";
	public final static String TAX_SETTINGS = "Tax Settings";
	public final static String TAX_RETURN = "Tax Return";
	public final static String TAX_REPORTS = "Tax Reports";
	public final static String TAX_PAYMENTS = "Tax Payments";
	public final static String TAX_ADJUSTMENTS = "Tax Adjustments";
	
	public final static String EMPLOYEE = "Employee";
	public final static String EMPLOYEE_TYPE = "Employee Type";
	public final static String DEPARTMENT = "Department";
	public final static String SALARY_TYPE = "Salary Type";
	public final static String LEAVE_TYPE = "Leave Type";
	public final static String HOLIDAY = "Holiday";
	public final static String PAYROLL_ADDITION = "PAYROLL_ADDITION";
	public final static String PAYROLL_DEDUCTION = "PAYROLL_DEDUCTION";
	public final static String PAYROLL_SUMMARY = "Payroll Summary";
	public final static String PAYROLL_PAYMENT = "Payroll Payment";
	public final static String CLAIM = "Claim";
	public final static String CLAIM_PAYMENT = "Claim Payment";
	public final static String PAYROLL_REPORT = "Payroll Report";

	public final static String RENTAL_CONSIGNMENT_ORDER = "Rental / Consignment Order";
	public final static String RENTAL_CONSIGNMENT_TRANSACTION = "Rental / Consignment Transaction";
	public final static String RENTAL_CONSIGNMENT_REPORTS = "Rental / Consignment Reports";
    		   
	public final static String IN_HOUSE_SUB_MENU = "In House";
    
	public final static String INVENTORY = "Inventory";
	public final static String ACCOUNTING_SUB_MENU = "Accounting";
	public final static String ACTIVITY_LOGS = "Activity Logs";
	public final static String LOC_TYPE_ID = "LOC_TYPE_ID";
	public final static String SHOPPING_CART = "Shopping Cart";
	public final static String E_COMMERCE = "eCommerce";
	
	public static final String KITTING = "KITTING";
	public static final String POS_REPORT = "POS_REPORT";
	public static final String PEPPOL = "PEPPOL";
}