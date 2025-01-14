package com.track.constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* * ************Modification History*********************************
	Nov 13 2014 Bruhan, Description: To include CNT_TO_ASSIGNEE_UPLOAD,CNT_LOAN_ASSIGNEE_UPLOAD and CNT_EMPLOYEE_UPLOAD
	Dec 05 2014 Bruhan, Description: To include PUT_AWAY_IN,PUT_AWAY_OUT
	Jan 02 2015 Bruhan, Description: To include ASSIGN_LOC_IN,ASSIGN_LOC_OUT
*/
public class TransactionConstants
{

  private static Map<String,String> mp=new HashMap<String, String>();
 
  //user admin
  public static final String  CRAT_COMPANY  = "CREATE_COMPANY";
  public static final String ADD_USER  = "CREATE_USER";
  public static final String UPDATE_USER    = "EDIT_USER";
  public static final String ADD_GROUP  = "CREATE_USER_ACCESS_RIGHTS_GROUP"; 
  public static final String UPDATE_GROUP    = "EDIT_USER_ACCESS_RIGHTS_GROUP";
  public static final String DELETE_GROUP    = "DELETE_USER_ACCESS_RIGHTS_GROUP";
  public static final String AUTHORISE_CMPY="AUTHORISE_COMPANY";
  public static final String MAINT_CUST_COMPANY="MAINT_CUSTOMER_COMPANY";
  //System master
  public static final String ADD_ITEM    = "CREATE_PRODUCT";
  public static final String UPD_ITEM    = "EDIT_PRODUCT";
  public static final String DEL_ITEM    = "DELETE_PRODUCT";
  
  public static final String ADD_LOC    = "CRATE_LOCATION";
  public static final String UPD_LOC    = "EDIT_LOCATION";
  public static final String DEL_LOC    = "DELETE_LOCATION";
  
  public static final String ADD_UOM    = "CREATE_UOM";
  public static final String UPD_UOM    = "EDIT_UOM";
  public static final String DEL_UOM    = "DELETE_UOM";
  
  public static final String ADD_TRANSPORT_MODE    = "CREATE_TRANSPORT_MODE";
  public static final String UPD_TRANSPORT_MODE    = "EDIT_TRANSPORT_MODE";
  public static final String DEL_TRANSPORT_MODE    = "DELETE_TRANSPORT_MODE";
  
  public static final String ADD_PACKING    = "CREATE_PACKING";
  public static final String UPD_PACKING    = "EDIT_PACKING";
  public static final String DEL_PACKING    = "DELETE_PACKING";
  
  public static final String ADD_CURNCY    = "CREATE_CURRENCY";
  public static final String UPD_CURNCY    = "EDIT_CURRENCY";
  
  public static final String ADD_RSN    = "CREATE_REASON_CODE";
  public static final String UPD_RSN    = "EDIT_REASON_CODE";
  public static final String DEL_RSN    = "DELETE_REASON_CODE";
  
  public static final String ADD_PRDTYPE    = "CREATE_PRODUCT_TYPE";
  public static final String UPDATE_PRDTYPE    = "EDIT_PRODUCT_TYPE";
  public static final String DEL_PRDTYPE    = "DELETE_PRODUCT_TYPE";
  
  public static final String ADD_PRDCLS    = "CREATE_PRODUCT_CLASS";
  public static final String UPDATE_PRDCLS    = "EDIT_PRODUCT_CLASS";
  public static final String DEL_PRDCLS    = "DELETE_PRODUCT_CLASS";
  
  //Resvi Added For PRODUCT DEPARTMENT
  public static final String ADD_PRDDEP   = "CREATE_PRODUCT_DEPARTMENT";
  public static final String UPDATE_PRDDEP    = "EDIT_PRODUCT_DEPARTMENT";
  public static final String DEL_PRDDEP    = "DELETE_PRODUCT_DEPARTMENT";
  //Ends
  
  public static final String ADD_PRDBRAND    = "CREATE_PRODUCT_BRAND";
  public static final String UPDATE_PRDBRAND    = "EDIT_PRODUCT_BRAND";
  public static final String DEL_PRDBRAND    = "DELETE_PRODUCT_BRAND";

  public static final String ADD_LOCTYPE    = "CREATE_LOCATION_TYPE";
  public static final String UPDATE_LOCTYPE    = "EDIT_LOCATION_TYPE";
  public static final String DEL_LOCTYPE    = "DELETE_LOCATION_TYPE";
  
  //imti added for location type two
  public static final String ADD_LOCTYPETWO    = "CREATE_LOCATION_TYPE_TWO";
  public static final String UPDATE_LOCTYPETWO    = "EDIT_LOCATION_TYPE_TWO";
  public static final String DEL_LOCTYPETWO    = "DELETE_LOCATION_TYPE_TWO";
  
  //imti added for location type three
  public static final String ADD_LOCTYPETHREE    = "CREATE_LOCATION_TYPE_THREE";
  public static final String UPDATE_LOCTYPETHREE    = "EDIT_LOCATION_TYPE_THREE";
  public static final String DEL_LOCTYPETHREE    = "DELETE_LOCATION_TYPE_THREE";

 
  public static final String ADD_PRODBOM    = "ADD_PROD_BOM";
  public static final String DEL_PRODBOM    = "DEL_PROD_BOM";


  public static final String ADD_EMPLOYEE    = "ADD_EMPLOYEE";
  public static final String UPDATE_EMPLOYEE    = "UPDATE_EMPLOYEE";
  public static final String DEL_EMPLOYEE    = "DEL_EMPLOYEE";
  
  public static final String ADD_CONTACT    = "ADD_CONTACT";
  public static final String UPDATE_CONTACT    = "UPDATE_CONTACT";
  public static final String DEL_CONTACT   = "DEL_CONTACT";
  /*public static final String DELETE_PAYMENT    = "DELETE_PAYMENT";*/

  /*public static final String ADD_PAYMENT    = "ADD_PAYMENT";
  public static final String UPDATE_PAYMENT    = "UPDATE_PAYMENT";
  
  
  public static final String ADD_PAYMENT_ID    = "ADD_PAYMENT_ID";
  public static final String UPDATE_PAYMENT_ID    = "UPDATE_PAYMENT_ID";
  public static final String DELETE_PAYMENT_ID    = "DELETE_PAYMENT_ID";*/
  
  //System Admin
  public static final String ADD_OUTLETTERMINAL   = "CREATE_OUTLETTERMINAL";
  public static final String UPD_OUTLETTERMINAL   = "EDIT_OUTLETTERMINAL";
  public static final String DEL_OUTLETTERMINAL    = "DELETE_OUTLETTERMINAL";
  public static final String ADD_OUTLET   = "CREATE_OUTLET";
  public static final String UPD_OUTLET    = "EDIT_OUTLET";
  public static final String DEL_OUTLET    = "DELETE_OUTLET";
  public static final String ADD_SUP    = "CREATE_SUPPLIER";
  public static final String UPD_SUP    = "EDIT_SUPPLIER";
  public static final String DEL_SUP    = "DELETE_SUPPLIER";
  public static final String ADD_FREIGHTFORWARDER    = "CREATE_FREIGHT_FORWARDER"; //thanz
  public static final String UPD_FREIGHTFORWARDER    = "EDIT_FREIGHT_FORWARDER";//thanz
  public static final String DEL_FREIGHTFORWARDER    = "DELETE_FREIGHT_FORWARDER";//thanz
  public static final String ADD_CUST    = "CREATE_CUSTOMER";
  public static final String UPD_CUST    = "EDIT_CUSTOMER";
  public static final String DEL_CUST    = "DELETE_CUSTOMER";
  public static final String ADD_CONSIGNEE    = "CREATE_CONSIGNEE";
  public static final String  UPD_LOAN_ASSIGNEE    = "EDIT_RENTAL_SERVEICE_ORDER_CUSTOMER";
  public static final String  DEL_LOAN_ASSIGNEE    = "DELETE_RENTAL_SERVEICE_ORDER_CUSTOMER";
  public static final String ADD_TO_ASSIGNEE_MST    = "CREATE_TO_ASSIGNEE_MST";
  public static final String UPD_TO_ASSIGNEE    = "EDIT_TRANSFER_ORDER_CUSTOMER";
  public static final String DEL_TO_ASSIGNEE    = "DELETE_TRANSFER_ORDER_CUSTOMER";
  public static final String CNT_ITEM_UPLOAD    = "CNT_PRODUCT_IMPORT_DATA";
  public static final String  CNT_LOC_UPLOAD    = " CNT_LOCATION_IMPORT_DATA";
  public static final String  CNT_INV_UPLOAD    = " CNT_INVENTORY_IMPORT_DATA";
  public static final String  CNT_SUP_UPLOAD    = " CNT_SUPPLIER_IMPORT_DATA";
  public static final String  CNT_CUST_UPLOAD    = " CNT_CUSTOMER_IMPORT_DATA";
  public static final String  CNT_STOCK_TAKE_UPLOAD    = "CNT_STOCK_TAKE_IMPORT_DATA";
  public static final String  CNT_ORD_DISPLAY_UPLOAD    = " CNT_ORDER_DISPALY_IMPORT_DATA";
  
  public static final String CREATE_PRD_PRM    = "CREATE_PRODUCT_PROMOTION";
  public static final String EDIT_PRD_PRM    = "UPDATE_PRODUCT_PROMOTION";
  //System Order
  public static final String CREATE_IB    = "CREATE_PURCHASE_ORDER";
  public static final String CREATE_PM    = "CREATE_MULTI_PURCHASE_ESTIMATE_ORDER";//Resvi
  public static final String CREATE_PE    = "CREATE_PURCHASE_ESTIMATE_ORDER";//Resvi
  public static final String IB_ADD_ITEM    = "PURCHASER_ORDER_ADD_PRODUCT";
  public static final String PM_ADD_ITEM    = "MULTI_PURCHASER_ESTIMATE_ORDER_ADD_PRODUCT";//Resvi
  public static final String PE_ADD_ITEM    = "PURCHASE_ESTIMATE_ORDER_ADD_PRODUCT";//Resvi
  public static final String PURCHASER_ORDER_DELETE_PRODUCT    = "PURCHASER_ORDER_DELETE_PRODUCT";
  public static final String  UPDATE_IB    = "UPDATE_PURCHASE_ORDER";
  public static final String  UPDATE_PM    = "UPDATE_MULTI_PURCHASE_ESTIMATE_ORDER";//Resvi
  public static final String  UPDATE_PE    = "UPDATE_PURCHASE_ESTIMATE_ORDER";//Resvi
  public static final String  CREATE_OB    = "CREATE_SALES_ORDER";
  public static final String OB_ADD_ITEM    = "SALES_ORDER_ADD_PRODUCT";
  public static final String UPDATE_OB    = "UPDATE_SALES_ORDER";
  //imti added for project
  public static final String  CREATE_PROJECT    = "CREATE_PROJECT";
  public static final String DELETE_PROJECT   = "DELETE_PROJECT";
  public static final String UPDATE_PROJECT    = "UPDATE_PROJECT";
  
  public static final String CREATE_LOANASSIGN    = "CREATE_RENTAL_ORDER";
  public static final String LOAN_ADD_ITEM    = "RENTAL_AND_SERVICE_ADD_PRODUCT";
  public static final String UPDATE_LOANASSIGN    = "UPDATE_RENTAL_AND_SERVICE_ORDER";
  public static final String CREATE_TO    = "CREATE_CONSIGNMENT_ORDER";
  public static final String TO_ADD_ITEM    = "CONSIGNMENT_ORDER_ADD_PRODUCT";
  public static final String UPDATE_TO    = "UPDATE_CONSIGNMENT_ORDER";
  //Outbound transaction
  public static final String LOAN_PICK_IN    = "RENTAL_AND_SERVICE_PICK_IN";
  public static final String LOAN_PIC_OUT    = "RENTAL_AND_SERVICE_PICK_OUT";
 
  public static final String LOAN_RECV_OUT    = "RENTAL_AND_SERVICE_RECV_OUT";
  public static final String LOAN_RECV_IN    = "RENTAL_AND_SERVICE_RECV_IN";
  public static final String LT_TRAN_IN    = "STOCK_MOVE_IN";
  public static final String LT_TRAN_OUT         = "STOCK_MOVE_OUT";
  public static final String GOODSRECEIPT           = "GOODS_RECEIPT";
  public static final String GOODSISSUE          = "GOODS_ISSUE";
  public static final String PIC_TRAN_OUT        = "SALES_ORDER_PICK_TRAN_OUT";
  public static final String PIC_TRAN_IN         = "SALES_ORDER_PICK_TRAN_IN";
  public static final String ORDER_CLS         = "FORCE_CLOSE";
  public static final String ORDER_ITEM_CLS    = "ORDER_PRODUCT_CLASS";
  //OutBound Transfer Picking in
  public static final String OB_TRANSFER_PIC_IN     = "SALES_ORDER_TRAN_PICK_IN";
  public static final String OB_TRANSFER_PIC_OUT    = "SALES_ORDER_TRAN_PICK_OUT";
  public static final String CREATE_REGISTRATION    = "CREATE_REGISTRATION";
  public static final String POS_REFUND    = "POS_REFUND";
  public static final String ADD_PAYMENT    = "ADD_PAYMENT";
  //ORDERTYPE
  public static final String ADD_ORDERTYPE    = "CREATE_ORDERTYPE";
  public static final String UPD_ORDERTYPE    = "EDIT_ORDERTYPE";
  
  public static final String CREATE_CATALOG    = "CREATE_CATALOG";
 
  
  //Inbound transaction 
  public static final String ORD_RECV      = "ORDER_RECV";
  //ship confirm
  public static final String ORD_ISSUE     = "ORDER_ISSUE";
  public static final String STOCK_TAKE    = "STOCK_TAKE";
  public static final String STOCK_TAKE_OUT    = "STOCK_TAKE_OUT";
    public static final String STOCK_TAKE_RESET    = "STOCK_TAKE_RESET";
  public static final String PIC_TO_OUT            = "PICK_CONSIGNMENT_ORDER_OUT";
  public static final String PIC_TO_IN             = "PICK_CONSIGNMENT_ORDER_IN";
  public static final String TO_RECV               = "CONSIGNMENT_ORDER_RECV";
  public static final String IB_REVERSE            = "PURCHASE_ORDER_REVERSE";
    
  public static final String ORD_PICK_ISSUE     = "ORD_PICK_ISSUE";
  public static final String INVOICE_PICK_ISSUE     = "INVOICE_PICK_ISSUE";
  
 // public static final String CYCLE_COUNT    = "CYCLE_COUNT";
  public static final String MISC_GENERATE_BATCH    = "GOODS_RECEIPT_GENERATE_BATCH";
  public static final String GENERATE_BATCH    = "GENERATE_BATCH";
  public static final String UPDATE_BATCH    = "EDIT_BATCH"; 
  public static final String GENERATE_SHIPPING   = "GENERATE_SHIPPING";
  public static final String OB_REVERSE    = "SALES_ORDER_REVERSE";
  //pos
   //public static final String POS_TRANSACTION    = "POS_TRANSACTION";
  // public static final String MOBILE_SHOPPING   = "MOBILE_SHOPPING";
  //UserLoc
    public static final String USER_LOC_ASSIGN    = "USER_LOC_ASSIGN";
    public static final String USER_INV_ASSIGN    = "_ASSIGN_INV_LOC";
    public static final String USER_LOC_UNASSIGN    = "USER_LOC_UNASSIGN";

   // public static final String POS_REFUND    = "POS_REFUND";
    public static final Object PIC_DOTRAN_OUT = null;
    
    public static final String CREATE_POS_PRDCLS   = "CREATE_POS_CATEGORY";    
    
    public static final String CREATE_PRDCLS_PROMOTION   = "CREATE_CATEGORY_PROMOTION";    
    public static final String UPDATE_PRDCLS_PROMOTION   = "UPDATE_CATEGORY_PROMOTION";    
    public static final String CREATE_BRAND_PROMOTION   = "CREATE_BRAND_PROMOTION";    
    public static final String UPDATE_BRAND_PROMOTION   = "UPDATE_BRAND_PROMOTION";    
   /* public static final String CASH    = "CASH";
    public static final String NETS    = "NETS";
    public static final String MEPS    = "MEPS";
    public static final String CREDIT_CARD    = "CREDIT CARD";
    public static final String VOUCHER    = "VOUCHER";
    public static final String OTHERS    = "OTHERS";*/
    

  
   public static final String GSTTYPE_ADD    = "CREATE_VAT";
   public static final String GSTTYPE_UPDATE    = "EDIT_VAT";
   public static final String UPDATE_KITTING    = "EDIT_KITTING";
   public static final String ADD_KITTING    = "CREATE_KITTING ";
   public static final String DEKITTING    = "DEKITTING";
   public static final String DELETE_DEKITTING_BOM    = "DELETE_DEKITTING_BOM";
   public static final String DELETE_DEKITTING_INV    = "DELETE_DEKITTING_INV";
   
   public static final String CUSTOMER_RETURNS    = "CUSTOMER_RETURNS";

   public static final String ADDITIONAL_CHARGE    = "ADDITIONAL_CHARGE";
  // public static final String CREATE_CATALOG    = "CREATE_CATALOG";
   public static final String EDIT_CATALOG    = "EDIT_CATALOG";
 //Start code added by Bruhan for DEL Catalog on 12th july 2012
   public static final String DEL_CATALOG    = "DELETE_CATALOG";
  
 //public static final String ADD_TIMESLOT    = "ADD_TIMESLOT";
   //public static final String UPDATE_TIMESLOT    = "UPDATE_TIMESLOT";
  // public static final String DELETE_TIMESLOT    = "DELETE_TIMESLOT";
  public static final String DEL_REGISTRATION_ORDER= "DEL_REGISTER_ORDER";
  // public static final String ATTENDANCE_CONFIRM    = "ATTENDANCE_CONFIRM";

   
   //---Added by Bruhan on May 14 2014, Description: Include for LabelPrint Goods Issue
 //  public static final String LABEL_PRINT_GI    = "LABEL_PRINT_GI";
  // public static final String UPDATE_LABEL_SETTING_GI    = "UPD_LABEL_SETTING_GI";
   //---Added by Bruhan on March 14 2014, Description: Include for LabelPrint Goods Issue
   

 //---Added by Bruhan on oct 8 2014, Description: for TO Reversal
   public static final String TO_REVERSE    = "CONSIGNMENT_REVERSE";
   public static final String TO_REVERSE_IN    = "CONSIGNMENT_REVERSE_IN";
   public static final String TO_REVERSE_OUT    = "CONSIGNMENT_REVERSE_OUT";
   //---Added by Bruhan on oct 8 2014, Description: for TO Reversal
   
  public static final String  CNT_TO_ASSIGNEE_UPLOAD    = "CNT_CONSIGNMENT_ORDER_CUSTOMER_IMPORT_DATA";
   public static final String  CNT_LOAN_ASSINGEE_UPLOAD    = "CNT_LOAN_CUSTOMER_IMPORT_DATA";
   public static final String  CNT_EMPLOYEE_UPLOAD    = "CNT_EMPLOYEE_UPLOAD";
 
  // public static final String PUT_AWAY_OUT         = "PUT_AWAY_OUT";
  // public static final String PUT_AWAY_IN    = "PUT_AWAY_IN";

   public static final String ASSIGN_LOC_OUT   = "ASSIGN_LOC_OUT";
   public static final String ASSIGN_LOC_IN    = "ASSIGN_LOC_IN";
 

 /* * ************Modification History*********************************
   Nov 13 2014 Bruhan, Description: To include CNT_TO_ASSIGNEE_UPLOAD,CNT_LOAN_ASSIGNEE_UPLOAD and CNT_EMPLOYEE_UPLOAD
   */
     
   //  added by Bruhan for production bom,routing import
     public static final String  CNT_PRODBOM_UPLOAD    = "CNT_PRODBOM_UPLOAD";

     
 
     public static final String  CREATE_EST    = "CREATE_SALES_ESTIMATE";
     public static final String EST_ADD_ITEM    = "SALES_ESTIMATE_ADD_PRODUCT";
     public static final String UPDATE_EST    = "UPDATE_SALES_ESTIMATE";
     public static final String EST_DEL_ITEM    = "SALES_ESTIMATE_DELETE_PRODUCT";
     public static final String DEL_ESTIMATE_ORDER   = "DELETE_SALES_ESTIMATE_ORDER";
     public static final String CONVERT_OUTBOUND   = "CONVERT_TO_SALES";
     public static final String CONVERT_INBOUND   = "CONVERT_TO_PURCHASE";
     
     public static final String DEL_OUTBOUND_ORDER    = "DELETE_SALES_ORDER";
     public static final String  DEL_INBOUND_ORDER    = "DELETE_PURCHASE_ORDER";
     public static final String  DEL_PE_ORDER    = "DELETE_PURCHASE_ESTIMATE_ORDER";//RESVI
     public static final String  DEL_PM_ORDER    = "DELETE_MULTI_PURCHASE_ESTIMATE_ORDER";//RESVI
     public static final String DEL_LOAN_ORDER    = "DELETE_RENTAL_AND_SERVICE_ORDER";
     public static final String DEL_CONSIGNMENT_ORDER    = "DELETE_CONSIGNMENT_ORDER";
    
     public static final String ADD_CUSTOMER_TYPE    = "CREATE_CUSTOMER_TYPE";
     public static final String DEL_CUSTOMER_TYPE    = "DELETE_CUSTOMER_TYPE";
     public static final String UPDATE_CUSTOMER_TYPE    = "UPDATE_CUSTOMER_TYPE";
     
     public static final String ADD_CLEARANCE_TYPE    = "CREATE_CLEARANCE_TYPE";
     public static final String DEL_CLEARANCE_TYPE    = "DELETE_CLEARANCE_TYPE";
     public static final String UPDATE_CLEARANCE_TYPE    = "UPDATE_CLEARANCE_TYPE";
	 
	  public static final String ADD_SUPPLIER_TYPE    = "CREATE_SUPPLIER_TYPE";
     public static final String DEL_SUPPLIER_TYPE    = "DELETE_SUPPLIER_TYPE";
     public static final String UPDATE_SUPPLIER_TYPE    = "EDIT_SUPPLIER_TYPE";
    
     
     public static final String IB_UPD_ITEM    = "PURCHASE_ORDER_UPDATE_PRODUCT";
     public static final String PM_UPD_ITEM    = "MULTI_PURCHASE_ORDER_UPDATE_PRODUCT";//Resvi
     public static final String PE_UPD_ITEM    = "PURCHASE_ESTIMATE_ORDER_UPDATE_PRODUCT";//Resvi
     public static final String EST_UPD_ITEM    = "SALES_ESTIMATE_ORDER_UPDATE_PRODUCT";
     public static final String OB_UPD_ITEM    = "SALES_ORDER_UPDATE_PRODUCT";
     public static final String TO_UPD_ITEM    = "CONSIGNMENT_ORDER_UPDATE_PRODUCT";
    //public static final String ADD_CUSTOMER_STATUS    = "ADD_CUSTOMER_STATUS";
     //public static final String DEL_CUSTOMER_STATUS    = "DEL_CUSTOMER_STATUS";
     //public static final String UPDATE_CUSTOMER_STATUS    = "UPD_CUSTOMER_STATUS";
     public static final String KIT_PARENT_UPLOAD    = "KIT_PARENT_UPLOAD";
     public static final String ADD_KITTING_UPLOAD    = "CREATE_KITTING_UPLOAD";
     
     public static final String  CNT_SUPPLIERDISCOUNT_UPLOAD    = "CNT_SUPPLIER_DISCOUNT_IMPORT_DATA";
     public static final String  CNT_CUSTOMERDISCOUNT_UPLOAD    = "CNT_CUSTOMER_DISCOUNT_IMPORT_DATA";
 	 public static final String OBISSUE_REVERSE    = "SALES_ORDER_ISSUE_REVERSE";
	
	 public static final String  KIT_PARENT    = "KIT_PARENT";
     public static final String  REJECTED_OB_ITEM    = "REJECTED_SALES_ORDER_PRODUCT";
     
     public static final String ADD_KITBOM    = "CREATE_KIT_BOM";
     public static final String DEL_KITBOM    = "DELETE_KIT_BOM";
     public static final String UPDATE_KITBOM    = "UPDATE_KIT_BOM";
     public static final String  SIGNATURE_CAPTURE="SIGNATURE_CAPTURE"; 
     public static final String  UPDATE_EXPIREDATE="EDIT_EXPIREDATE"; 
     public static final String DEL_FOOTER    = "DELETE_FOOTER";
     public static final String DEL_REMARKS    = "DELETE_REMARKS";
     public static final String DEL_INCO_TERMS    = "DELETE_INCOTERMS";
     public static final String DEL_SHIPPING_DETAILS    = "DELETE_SHIPPING_DETAILS";
     public static final String  CNT_OUTBOUNDPRODREMARKS_UPLOAD    = "CNT_SALES_ORDER_PRODUCT_REMARKS_IMPORT_DATA";
     public static final String  CNT_INBOUNDPRODREMARKS_UPLOAD    = "CNT_PURCHASE_ORDER_PRODUCT_REMARKS_IMPORT_DATA";
     public static final String ADD_BANK_BRANCH = "CREATE_BANK_BRNCH";
     public static final String UPD_BANK_BRANCH = "UPDATE_BANK_BRNCH";
     public static final String ADD_ACCOUNT = "CREATE_ACCOUNT";
     public static final String UPD_ACCOUNT = "EDIT_ACCOUNT";
     public static final String ADD_DAYBOOK  = "CREATE_DAYBOOK";
     public static final String UPD_DAYBOOK  = "EDIT_DAYBOOK";
     public static final String ADD_FIN_TRAN = "CREATE_FIN_TRAN";
     public static final String UPD_FIN_TRAN = "EDIT_FIN_TRAN";
     public static final String REC_FIN_TRAN = "REC_FIN_TRAN";
     public static final String  ADD_ALTERNATE_BRAND_PRODUCT    = "CREATE_ALTERNTE_BRAND_PRODUCT";
     public static final String  DELETE_ALTERNATE_BRAND_PRODUCT    = "DELETE_ALTERNTE_BRAND_PRODUCT";
     public static final String  UPDATE_ALTERNATE_BRAND_PRODUCT    = "EDIT_ALTERNTE_BRAND_PRODUCT";
     public static final String  DEL_ALTERNATE_BRAND_PRODUCT    = "DELETE_ALTERNTE_BRAND_PRODUCT";
     public static final String CREATEDNPL     = "CREATE_DNPL";
     public static final String EDITDNPL     = "EDIT_DNPL";
     
     public static final String ADD_PAYMENTTYPE    = "CREATE_PAYMENT_TYPE";
     public static final String ADD_HSCODE    = "CREATE_HSCODE";
     public static final String ADD_COO    = "CREATE_COO";
          
     public static final String ADD_PAYMENTMODE    = "CREATE_PAYMENT_MODE";
     public static final String DEL_PAYMENTMODE    = "DELETE_PAYMENT_MODE";
     
     public static final String ADD_PAYMENTTERMS   = "CREATE_PAYMENT_TERM";
     public static final String DEL_PAYMENTTERMS   = "DELETE_PAYMENT_TERM";
     
     public static final String DEL_PAYMENTTYPE    = "DELETE_PAYMENT_TYPE";
     public static final String DEL_HSCODE    = "DELETE_HSCODE";
     public static final String DEL_COO    = "DELETE_COO";
     
     public static final String CREATE_DIRECT_TAX_INVOICE    = "CREATE_DIRECT_TAX_INVOICE";
     public static final String EDIT_DIRECT_TAX_INVOICE    = "EDIT_DIRECT_TAX_INVOICE";
     public static final String REVERSE_DIRECT_TAX_INVOICE   = "REVERSE_DIRECT_TAX_INVOICE";
     public static final String DELETE_DIRECT_TAX_INVOICE   = "DELETE_DIRECT_TAX_INVOICE";
     
     public static final String PURCHASE_RETURN    ="PURCHASE_RETURN";
     public static final String SALES_RETURN    ="SALES_RETURN";
     
     public static final String CREATE_EXPENSES    = "CREATE_EXPENSES";
     public static final String CREATE_EXPENSES_ADD    = "CREATE_EXPENSES_ADD_ACCOUNT";
     public static final String CREATE_EXPENSES_UPD    = "UPDATE_EXPENSES_ADD_ACCOUNT";
     public static final String EDIT_EXPENSES    = "EDIT_EXPENSES";
     public static final String DELETE_EXPENSES    = "DELETE_EXPENSES";

     public static final String CREATE_GRN    = "CREATE_GRN";
     public static final String CONVERT_TO_BILL    = "CONVERT_TO_BILL";
     
     public static final String CREATE_BILL    = "CREATE_BILL";
     public static final String EDIT_BILL    = "EDIT_BILL";
     public static final String DELETE_BILL    = "DELETE_BILL";
     public static final String CANCEL_BILL    = "CANCEL_BILL";
     public static final String REVERSE_BILL    = "REVERSE_BILL";
     
     public static final String CREATE_CREDIT_NOTE    = "CREATE_CREDIT_NOTE";
     public static final String EDIT_CREDIT_NOTE    = "EDIT_CREDIT_NOTE";
     public static final String DELETE_CREDIT_NOTE    = "DELETE_CREDIT_NOTE";
     public static final String CANCEL_CREDIT_NOTE    = "CANCEL_CREDIT_NOTE";
     
     public static final String EXPENSES_TO_INVOICE    = "EXPENSES_TO_INVOICE";
     public static final String CONVERT_TO_INVOICE    = "CONVERT_TO_INVOICE";
     public static final String CREATE_GINO    = "CREATE_GINO";
     
     public static final String CREATE_INVOICE    = "CREATE_INVOICE";
     public static final String EDIT_INVOICE    = "EDIT_INVOICE";
     public static final String DELETE_INVOICE    = "DELETE_INVOICE";
     public static final String CANCEL_INVOICE    = "CANCEL_INVOICE";
     
     public static final String CREATE_PAYMENT_RECEIVED    = "CREATE_PAYMENT_RECEIVED";
     public static final String EDIT_PAYMENT_RECEIVED    = "EDIT_PAYMENT_RECEIVED";
     public static final String DELETE_PAYMENT_RECEIVED    = "DELETE_PAYMENT_RECEIVED";
     public static final String UNAPPLY_CREDIT_PAYMENT_RECEIVED    = "UNAPPLY_CREDIT_PAYMENT_RECEIVED";
     public static final String APPLY_CREDIT_PAYMENT_RECEIVED    = "APPLY_CREDIT_PAYMENT_RECEIVED";
     public static final String CHEQUE_DETAILS_RECEIVED     = "CHEQUE_DETAILS_RECEIVED ";
     
     public static final String CREATE_PAYMENT    = "CREATE_PAYMENT";
     public static final String EDIT_PAYMENT    = "EDIT_PAYMENT";
     public static final String DELETE_PAYMENT    = "DELETE_PAYMENT";
     public static final String CHEQUE_DETAILS    = "CHEQUE_DETAILS";
     public static final String UNAPPLY_CREDIT_PAYMENT    = "UNAPPLY_CREDIT_PAYMENT";
     public static final String APPLY_CREDIT_PAYMENT    = "APPLY_CREDIT_PAYMENT";
     
     public static final String CREATE_DEBIT_NOTE    = "CREATE_DEBIT_NOTE";
     public static final String EDIT_DEBIT_NOTE    = "EDIT_DEBIT_NOTE";
     public static final String DELETE_DEBIT_NOTE    = "DELETE_DEBIT_NOTE";
     public static final String CANCEL_DEBIT_NOTE    = "CANCEL_DEBIT_NOTE";
     
     public static final String TAX_SETTINGS_UPDATE    = "TAX_SETTINGS_UPDATE";
     public static final String TAX_FILE    = "TAX_FILE";
     public static final String CREATE_TAXRETURN_PAYMENT    = "CREATE_TAXRETURN_PAYMENT";
     public static final String DELETE_TAXRETURN_PAYMENT    = "DELETE_TAXRETURN_PAYMENT";
     public static final String CREATE_TAXFILE_ADJUSTMENT    = "CREATE_TAXFILE_ADJUSTMENT";
     public static final String UPDATE_TAXFILE_ADJUSTMENT    = "UPDATE_TAXFILE_ADJUSTMENT";
     
     public static final String PDC_PROCESS_PAYMENT    = "PDC_PROCESS_PAYMENT";
     public static final String PDC_EDIT_PAYMENT    = "PDC_EDIT_PAYMENT";
     //public static final String CANCEL_PDC_PAYMENT    = "CANCEL_PDC_PAYMENT";
     
     public static final String PDC_PROCESS_PAYMENT_RECEIVED    = "PDC_PROCESS_PAYMENT_RECEIVED";
     public static final String PDC_EDIT_PAYMENT_RECEIVED    = "PDC_EDIT_PAYMENT_RECEIVED";
     
     public static final String CREATE_EMPLOYEE_TYPE    = "CREATE_EMPLOYEE_TYPE";
     public static final String UPDATE_EMPLOYEE_TYPE    = "UPDATE_EMPLOYEE_TYPE";
     
     public static final String CREATE_LEAVE_TYPE    = "CREATE_LEAVE_TYPE";
     public static final String UPDATE_LEAVE_TYPE    = "UPDATE_LEAVE_TYPE";
     
     public static final String CREATE_SHIFT    = "CREATE_SHIFT";
     public static final String UPDATE_SHIFT    = "UPDATE_SHIFT";
     
     public static final String CREATE_HOLIDAY    = "CREATE_HOLIDAY";
     public static final String UPDATE_HOLIDAY    = "UPDATE_HOLIDAY";
     
     public static final String CREATE_SALARY_TYPE    = "CREATE_SALARY_TYPE";
     public static final String UPDATE_SALARY_TYPE    = "UPDATE_SALARY_TYPE";
     public static final String  IMPORT_SALARY_TYPE    = "IMPORT_SALARY_TYPE";
     
     public static final String CREATE_DEPARTMENT    = "CREATE_DEPARTMENT";
     public static final String UPDATE_DEPARTMENT    = "UPDATE_DEPARTMENT";
     public static final String  IMPORT_DEPARTMENT    = "IMPORT_DEPARTMENT";
     
     public static final String APPLY_LEAVE    = "APPLY_LEAVE";
     
     public static final String CREATE_PAYROLL_ADDITION_MASTER    = "CREATE_PAYROLL_ADDITION_MASTER";
     public static final String UPDATE_PAYROLL_ADDITION_MASTER    = "UPDATE_PAYROLL_ADDITION_MASTER";
     public static final String DELETE_PAYROLL_ADDITION_MASTER    = "DELETE_PAYROLL_ADDITION_MASTER";
     
     public static final String CREATE_PAYROLL_DEDUCTION_MASTER    = "CREATE_PAYROLL_DEDUCTION_MASTER";
     public static final String UPDATE_PAYROLL_DEDUCTION_MASTER    = "UPDATE_PAYROLL_DEDUCTION_MASTER";
     public static final String DELETE_PAYROLL_DEDUCTION_MASTER    = "DELETE_PAYROLL_DEDUCTION_MASTER";
     
     public static final String CREATE_PAYROLL    = "CREATE_PAYROLL";
     public static final String UPDATE_PAYROLL    = "UPDATE_PAYROLL";
     
     public static final String CREATE_PAYROLL_ADDITION    = "CREATE_PAYROLL_ADDITION";
     public static final String CREATE_PAYROLL_DEDUCTION    = "CREATE_PAYROLL_DEDUCTION";
     
     
     
     public static final String APPLY_CLAIM   = "APPLY_CLAIM";
     public static final String APPROVED_CLAIM   = "APPROVE_CLAIM";
     public static final String REJECTED_CLAIM   = "REJECTED_CLAIM";
     
     public static final String CREATE_CLAIM_PAYMENT    = "CREATE_CLAIM_PAYMENT";
     public static final String UPDATE_CLAIM_PAYMENT    = "UPDATE_CLAIM_PAYMENT";
     
     public static final String CREATE_PAYROLL_PAYMENT    = "CREATE_PAYROLL_PAYMENT";
     public static final String UPDATE_PAYROLL_PAYMENT    = "UPDATE_PAYROLL_PAYMENT";
    
   //Shopify_Config
     public static final String ADD_SHOPIFYCONFIG    = "CREATE_SHOPIFY_CONFIG";
     public static final String UPD_SHOPIFYCONFIG    = "EDIT_SHOPIFY_CONFIG";
     
   //Shopee_Config
     public static final String ADD_SHOPEECONFIG    = "CREATE_SHOPEE_CONFIG";
     public static final String UPD_SHOPEECONFIG    = "EDIT_SHOPEE_CONFIG";
     
     public static final String CREATE_INVOICE_QUOTES    = "CREATE_INVOICE_QUOTES ";
     public static final String EDIT_INVOICE_QUOTES     = "EDIT_INVOICE_QUOTES ";
     
     public static final String CREATE_JOURNAL   = "CREATE_JOURNAL";
     public static final String EDIT_JOURNAL    = "EDIT_JOURNAL";
     public static final String DELETE_JOURNAL    = "DELETE_JOURNAL";
     
     public static final String BULK_PAYSLIP_PROCESSING_SEND_MAIL    = "BULK_PAYSLIP_PROCESSING_SEND_MAIL";
     
     public static final String PRD_RETURN    = "CREATE_PRODUCT_RETURN";
     public static final String PRD_RECEIVE    = "CREATE_PRODUCT_RECEIVE";
     
     static
  {
    //user admin
    mp.put(ADDITIONAL_CHARGE,ADDITIONAL_CHARGE);
    mp.put(ADD_USER,ADD_USER);
    mp.put(UPDATE_USER,UPDATE_USER);
    mp.put(ADD_GROUP,ADD_GROUP);
    mp.put(UPDATE_GROUP,UPDATE_GROUP);
    mp.put(DELETE_GROUP,DELETE_GROUP);
    mp.put(AUTHORISE_CMPY, AUTHORISE_CMPY);
    mp.put(MAINT_CUST_COMPANY,MAINT_CUST_COMPANY);
    //System master
    mp.put(ADD_ITEM,ADD_ITEM);
    mp.put(UPD_ITEM,UPD_ITEM);
    mp.put(DEL_ITEM,DEL_ITEM);
    
    mp.put(ADDITIONAL_CHARGE,ADDITIONAL_CHARGE);
    
    mp.put(ADD_LOC,ADD_LOC);
    mp.put(UPD_LOC,UPD_LOC);
    mp.put(DEL_LOC,DEL_LOC);
    
    mp.put(ADD_LOCTYPE,ADD_LOCTYPE);
    mp.put(UPDATE_LOCTYPE,UPDATE_LOCTYPE);
    mp.put(DEL_LOCTYPE,DEL_LOCTYPE);
    
    mp.put(ADD_LOCTYPETWO,ADD_LOCTYPETWO);
    mp.put(UPDATE_LOCTYPETWO,UPDATE_LOCTYPETWO);
    mp.put(DEL_LOCTYPETWO,DEL_LOCTYPETWO);
    
    mp.put(ADD_LOCTYPETHREE,ADD_LOCTYPETHREE);
    mp.put(UPDATE_LOCTYPETHREE,UPDATE_LOCTYPETHREE);
    mp.put(DEL_LOCTYPETHREE,DEL_LOCTYPETHREE);
    
    mp.put(ADD_RSN,ADD_RSN);
    mp.put(UPD_RSN,UPD_RSN);
    mp.put(DEL_RSN,DEL_RSN);
    
    mp.put(ADD_PRDCLS,ADD_PRDCLS);
    mp.put(UPDATE_PRDCLS,UPDATE_PRDCLS);
    mp.put(DEL_PRDCLS,DEL_PRDCLS);
    
    mp.put(ADD_PRDDEP,ADD_PRDDEP);
    mp.put(UPDATE_PRDDEP,UPDATE_PRDDEP);
    mp.put(DEL_PRDDEP,DEL_PRDDEP);

    mp.put(ADD_PRDTYPE,ADD_PRDTYPE);
    mp.put(UPDATE_PRDTYPE,UPDATE_PRDTYPE);
    mp.put(DEL_PRDTYPE,DEL_PRDTYPE);
    
    mp.put(ADD_PRDBRAND,ADD_PRDBRAND);
    mp.put(UPDATE_PRDBRAND,UPDATE_PRDBRAND);
    mp.put(DEL_PRDBRAND,DEL_PRDBRAND);
    
    mp.put(ADD_UOM,ADD_UOM);
    mp.put(UPD_UOM,UPD_UOM);
    mp.put(DEL_UOM,DEL_UOM);
    
    //System Admin
    
    mp.put(ADD_SUP,ADD_SUP);
    mp.put(UPD_SUP,UPD_SUP);
    mp.put(DEL_SUP,DEL_SUP);

    mp.put(ADD_CUST,ADD_CUST);
    mp.put(UPD_CUST,UPD_CUST);
    mp.put(DEL_CUST,DEL_CUST);

    mp.put(ADD_CONSIGNEE,ADD_CONSIGNEE);
    mp.put(UPD_LOAN_ASSIGNEE,UPD_LOAN_ASSIGNEE);
    mp.put(DEL_LOAN_ASSIGNEE,DEL_LOAN_ASSIGNEE);
    
    mp.put(ADD_TO_ASSIGNEE_MST,ADD_TO_ASSIGNEE_MST);
    mp.put(UPD_TO_ASSIGNEE,UPD_TO_ASSIGNEE);
    mp.put(DEL_TO_ASSIGNEE,DEL_TO_ASSIGNEE);
    
    mp.put(ADD_EMPLOYEE,ADD_EMPLOYEE);
    mp.put(UPDATE_EMPLOYEE,UPDATE_EMPLOYEE);
    mp.put(DEL_EMPLOYEE,DEL_EMPLOYEE);
    
    mp.put(CNT_ITEM_UPLOAD, CNT_ITEM_UPLOAD);
    mp.put(CNT_LOC_UPLOAD, CNT_LOC_UPLOAD);
    mp.put(CNT_INV_UPLOAD, CNT_INV_UPLOAD);
    mp.put(CNT_SUP_UPLOAD, CNT_SUP_UPLOAD);
    mp.put(CNT_CUST_UPLOAD, CNT_CUST_UPLOAD);
    mp.put(CNT_ORD_DISPLAY_UPLOAD, CNT_ORD_DISPLAY_UPLOAD);
    
    //System Order
    mp.put(CREATE_IB,CREATE_IB);
    mp.put(IB_ADD_ITEM,IB_ADD_ITEM);
    mp.put(PURCHASER_ORDER_DELETE_PRODUCT, PURCHASER_ORDER_DELETE_PRODUCT);
    mp.put(CREATE_OB,CREATE_OB);
    mp.put(UPDATE_IB,UPDATE_IB);
    
  //RESVI Add This code For MultipurchaseEstimate

    mp.put(CREATE_PM,CREATE_PM);   
    mp.put(PM_ADD_ITEM,PM_ADD_ITEM);
    mp.put(UPDATE_PM,UPDATE_PM);
    mp.put(PM_UPD_ITEM,PM_UPD_ITEM);
    mp.put(DEL_PM_ORDER,DEL_PM_ORDER);
   //Ends
    
    //RESVI Add This code For PurchaseEstimate On 16.09.2021

    mp.put(CREATE_PE,CREATE_PE);   
    mp.put(PE_ADD_ITEM,PE_ADD_ITEM);
    mp.put(UPDATE_PE,UPDATE_PE);
    mp.put(PE_UPD_ITEM,PE_UPD_ITEM);
    mp.put(DEL_PE_ORDER,DEL_PE_ORDER);
    
   //    Ends
   
    mp.put(OB_ADD_ITEM,OB_ADD_ITEM);
    mp.put(UPDATE_OB,UPDATE_OB);
    
    mp.put(CREATE_LOANASSIGN,CREATE_LOANASSIGN);
    mp.put(LOAN_ADD_ITEM,LOAN_ADD_ITEM);
    mp.put(UPDATE_LOANASSIGN,UPDATE_LOANASSIGN);
    mp.put(CREATE_TO,CREATE_TO);
    mp.put(TO_ADD_ITEM,TO_ADD_ITEM);
    mp.put(UPDATE_TO,UPDATE_TO);
    mp.put(LOAN_PICK_IN,LOAN_PICK_IN);
    mp.put(LOAN_PIC_OUT,LOAN_PIC_OUT);
    mp.put(LOAN_RECV_OUT,LOAN_RECV_OUT);
    mp.put(LOAN_RECV_IN,LOAN_RECV_IN);
    mp.put(LT_TRAN_IN,LT_TRAN_IN);
    mp.put(OB_TRANSFER_PIC_IN,OB_TRANSFER_PIC_IN);
    mp.put(OB_TRANSFER_PIC_OUT,OB_TRANSFER_PIC_OUT);
    
    
    mp.put(LT_TRAN_OUT,LT_TRAN_OUT);
    mp.put(GOODSRECEIPT,GOODSRECEIPT);
    mp.put(GOODSISSUE,GOODSISSUE);
    mp.put(PIC_TRAN_OUT,PIC_TRAN_OUT);
    mp.put(PIC_TRAN_IN,PIC_TRAN_IN);
    
    mp.put(ORD_RECV,ORD_RECV);
    mp.put(ORD_ISSUE,ORD_ISSUE);
    mp.put(ORD_PICK_ISSUE,ORD_PICK_ISSUE);
    mp.put(INVOICE_PICK_ISSUE,INVOICE_PICK_ISSUE);
    mp.put(STOCK_TAKE,STOCK_TAKE);
    mp.put(STOCK_TAKE_OUT,STOCK_TAKE_OUT);
    mp.put(STOCK_TAKE_RESET,STOCK_TAKE_RESET);
    mp.put(PIC_TO_OUT,PIC_TO_OUT);
    mp.put(PIC_TO_IN,PIC_TO_IN);
    mp.put(TO_RECV,TO_RECV);
    mp.put(IB_REVERSE,IB_REVERSE);
       
   // mp.put(CYCLE_COUNT,CYCLE_COUNT);
    mp.put(MISC_GENERATE_BATCH,MISC_GENERATE_BATCH);
    mp.put(GENERATE_BATCH,GENERATE_BATCH);
    mp.put(UPDATE_BATCH,UPDATE_BATCH);
    mp.put(GENERATE_SHIPPING,GENERATE_SHIPPING);
    mp.put(OB_REVERSE,OB_REVERSE);
   
   // mp.put(POS_REFUND,POS_REFUND);

    mp.put(GSTTYPE_ADD, GSTTYPE_ADD);
    mp.put(GSTTYPE_UPDATE, GSTTYPE_UPDATE);
    mp.put(ORDER_CLS, ORDER_CLS);
    mp.put(ORDER_ITEM_CLS, ORDER_ITEM_CLS);
    
    mp.put(UPDATE_KITTING, UPDATE_KITTING);
    mp.put(ADD_KITTING, ADD_KITTING);
    mp.put(DEKITTING, DEKITTING);
    mp.put(DELETE_DEKITTING_BOM, DELETE_DEKITTING_BOM);
    mp.put(DELETE_DEKITTING_INV, DELETE_DEKITTING_INV);
    mp.put(CUSTOMER_RETURNS, CUSTOMER_RETURNS); 
    mp.put(ADD_CURNCY,ADD_CURNCY);
    mp.put(UPD_CURNCY,UPD_CURNCY);
   // mp.put(MOBILE_SHOPPING, MOBILE_SHOPPING);
    //mp.put(DEL_CATALOG, DEL_CATALOG);
   // mp.put(EDIT_CATALOG, EDIT_CATALOG);
   // mp.put(CREATE_CATALOG, CREATE_CATALOG);
   //  mp.put(ADD_TIMESLOT, ADD_TIMESLOT);
   // mp.put(UPDATE_TIMESLOT, UPDATE_TIMESLOT);
     mp.put(TO_REVERSE, TO_REVERSE);
 
 	mp.put(CNT_TO_ASSIGNEE_UPLOAD, CNT_TO_ASSIGNEE_UPLOAD);
    mp.put(CNT_LOAN_ASSINGEE_UPLOAD, CNT_LOAN_ASSINGEE_UPLOAD);
    mp.put(CNT_EMPLOYEE_UPLOAD, CNT_EMPLOYEE_UPLOAD);
    mp.put(ASSIGN_LOC_OUT, ASSIGN_LOC_OUT);
    mp.put(ASSIGN_LOC_IN, ASSIGN_LOC_IN);

    mp.put(DEL_INBOUND_ORDER,DEL_INBOUND_ORDER);
    mp.put(DEL_OUTBOUND_ORDER,DEL_OUTBOUND_ORDER);
    mp.put(DEL_LOAN_ORDER,DEL_LOAN_ORDER);
    mp.put(DEL_CONSIGNMENT_ORDER,DEL_CONSIGNMENT_ORDER);
    
    mp.put(CREATE_EST,CREATE_EST);
    mp.put(EST_ADD_ITEM,EST_ADD_ITEM);
    mp.put(EST_DEL_ITEM,EST_DEL_ITEM);
    mp.put(UPDATE_EST,UPDATE_EST);
    mp.put(DEL_ESTIMATE_ORDER,DEL_ESTIMATE_ORDER);
    
    mp.put(ADD_CUSTOMER_TYPE,ADD_CUSTOMER_TYPE);
    mp.put(DEL_CUSTOMER_TYPE,DEL_CUSTOMER_TYPE);
    mp.put(UPDATE_CUSTOMER_TYPE,UPDATE_CUSTOMER_TYPE);
    mp.put(CONVERT_OUTBOUND,CONVERT_OUTBOUND);
    
    mp.put(IB_UPD_ITEM ,IB_UPD_ITEM);
    mp.put(EST_UPD_ITEM,EST_UPD_ITEM);
    mp.put(OB_UPD_ITEM,OB_UPD_ITEM);
    mp.put(TO_UPD_ITEM,TO_UPD_ITEM);

    mp.put(KIT_PARENT_UPLOAD,KIT_PARENT_UPLOAD);
    mp.put(ADD_KITTING_UPLOAD,ADD_KITTING_UPLOAD);
    mp.put(CONVERT_INBOUND,CONVERT_INBOUND); 
    
    mp.put(CNT_SUPPLIERDISCOUNT_UPLOAD,CNT_SUPPLIERDISCOUNT_UPLOAD);
    mp.put(CNT_CUSTOMERDISCOUNT_UPLOAD,CNT_CUSTOMERDISCOUNT_UPLOAD); 
	mp.put(OBISSUE_REVERSE,OBISSUE_REVERSE);
	mp.put(ADD_KITBOM,ADD_KITBOM);
	mp.put(DEL_KITBOM,DEL_KITBOM);
	mp.put(SIGNATURE_CAPTURE,SIGNATURE_CAPTURE);
	mp.put(UPDATE_EXPIREDATE,UPDATE_EXPIREDATE);
	mp.put(CNT_OUTBOUNDPRODREMARKS_UPLOAD,CNT_OUTBOUNDPRODREMARKS_UPLOAD);
	mp.put(CNT_INBOUNDPRODREMARKS_UPLOAD,CNT_INBOUNDPRODREMARKS_UPLOAD);
	mp.put(ADD_ALTERNATE_BRAND_PRODUCT,ADD_ALTERNATE_BRAND_PRODUCT);
	mp.put(DELETE_ALTERNATE_BRAND_PRODUCT,DELETE_ALTERNATE_BRAND_PRODUCT);
	mp.put(CREATEDNPL,CREATEDNPL);
	mp.put(CNT_STOCK_TAKE_UPLOAD,CNT_STOCK_TAKE_UPLOAD);
	mp.put("DE-KITTING_IN","DE-KITTING_IN");
	mp.put("DE-KITTING_OUT","DE-KITTING_OUT");
	mp.put("KITTING_IN","KITTING_IN");
	mp.put("KITTING_OUT","KITTING_OUT");
	mp.put(ADD_PAYMENTTYPE,ADD_PAYMENTTYPE);
	mp.put(ADD_HSCODE,DEL_HSCODE);
	mp.put(ADD_COO,DEL_COO);
	
	mp.put(DEL_PAYMENTTYPE,DEL_PAYMENTTYPE);
	mp.put(DEL_HSCODE,DEL_HSCODE);
	mp.put(DEL_COO,DEL_COO);
//	transport mode
	 mp.put(ADD_TRANSPORT_MODE,ADD_TRANSPORT_MODE);
	 mp.put(DEL_TRANSPORT_MODE,DEL_TRANSPORT_MODE);
	 
	mp.put(ADD_SUPPLIER_TYPE,ADD_SUPPLIER_TYPE);
	mp.put(DEL_SUPPLIER_TYPE,DEL_SUPPLIER_TYPE);
	mp.put(UPDATE_SUPPLIER_TYPE,UPDATE_SUPPLIER_TYPE);
	
	mp.put(PURCHASE_RETURN,PURCHASE_RETURN);
	mp.put(SALES_RETURN,SALES_RETURN);
	
	mp.put(CREATE_EXPENSES,CREATE_EXPENSES);
	mp.put(EDIT_EXPENSES,EDIT_EXPENSES);
	mp.put(EDIT_EXPENSES,EDIT_EXPENSES);
	
	mp.put(CREATE_GRN,CREATE_GRN);
	mp.put(CONVERT_TO_BILL,CONVERT_TO_BILL);
	
	mp.put(CREATE_BILL,CREATE_BILL);
	mp.put(EDIT_BILL,EDIT_BILL);
	mp.put(DELETE_BILL,DELETE_BILL);
	mp.put(CANCEL_BILL,CANCEL_BILL);
	mp.put(REVERSE_BILL,REVERSE_BILL);
	
	mp.put(CREATE_CREDIT_NOTE,CREATE_CREDIT_NOTE);
	mp.put(EDIT_CREDIT_NOTE,EDIT_CREDIT_NOTE);
	mp.put(DELETE_CREDIT_NOTE,DELETE_CREDIT_NOTE);
	mp.put(CANCEL_CREDIT_NOTE,CANCEL_CREDIT_NOTE);
	
	mp.put(CREATE_GINO,CREATE_GINO);
	mp.put(CONVERT_TO_INVOICE,CONVERT_TO_INVOICE);
	mp.put(EXPENSES_TO_INVOICE,EXPENSES_TO_INVOICE);
	
	mp.put(CREATE_INVOICE,CREATE_INVOICE);
	mp.put(EDIT_INVOICE,EDIT_INVOICE);
	mp.put(DELETE_INVOICE,DELETE_INVOICE);
	mp.put(CANCEL_INVOICE,CANCEL_INVOICE);
	
	mp.put(CREATE_PAYMENT_RECEIVED,CREATE_PAYMENT_RECEIVED);
	mp.put(EDIT_PAYMENT_RECEIVED,EDIT_PAYMENT_RECEIVED);
	mp.put(DELETE_PAYMENT_RECEIVED,DELETE_PAYMENT_RECEIVED);
	mp.put(UNAPPLY_CREDIT_PAYMENT_RECEIVED,UNAPPLY_CREDIT_PAYMENT_RECEIVED);
	mp.put(APPLY_CREDIT_PAYMENT_RECEIVED,APPLY_CREDIT_PAYMENT_RECEIVED);
	mp.put(CHEQUE_DETAILS_RECEIVED,CHEQUE_DETAILS_RECEIVED);
	
	
	mp.put(CREATE_PAYMENT,CREATE_PAYMENT);
	mp.put(EDIT_PAYMENT,EDIT_PAYMENT);
	mp.put(DELETE_PAYMENT,DELETE_PAYMENT);
	mp.put(CHEQUE_DETAILS,CHEQUE_DETAILS);
	mp.put(UNAPPLY_CREDIT_PAYMENT,UNAPPLY_CREDIT_PAYMENT);
	mp.put(APPLY_CREDIT_PAYMENT,APPLY_CREDIT_PAYMENT);
	
	mp.put(CREATE_DEBIT_NOTE,CREATE_DEBIT_NOTE);
	mp.put(EDIT_DEBIT_NOTE,EDIT_DEBIT_NOTE);
	mp.put(DELETE_DEBIT_NOTE,DELETE_DEBIT_NOTE);
	mp.put(CANCEL_DEBIT_NOTE,CANCEL_DEBIT_NOTE);
	
	mp.put(TAX_SETTINGS_UPDATE,TAX_SETTINGS_UPDATE);
	mp.put(TAX_FILE,TAX_FILE);
	mp.put(CREATE_TAXRETURN_PAYMENT,CREATE_TAXRETURN_PAYMENT);
	mp.put(DELETE_TAXRETURN_PAYMENT,DELETE_TAXRETURN_PAYMENT);
	mp.put(CREATE_TAXFILE_ADJUSTMENT,CREATE_TAXFILE_ADJUSTMENT);
	mp.put(UPDATE_TAXFILE_ADJUSTMENT,UPDATE_TAXFILE_ADJUSTMENT);
	
	mp.put(PDC_PROCESS_PAYMENT,PDC_PROCESS_PAYMENT);
	mp.put(PDC_EDIT_PAYMENT,PDC_EDIT_PAYMENT);
	//mp.put(CANCEL_PDC_PAYMENT,CANCEL_PDC_PAYMENT);
	
	mp.put(PDC_PROCESS_PAYMENT_RECEIVED,PDC_PROCESS_PAYMENT_RECEIVED);
	mp.put(PDC_EDIT_PAYMENT_RECEIVED,PDC_EDIT_PAYMENT_RECEIVED);
	
	mp.put(CREATE_EMPLOYEE_TYPE,CREATE_EMPLOYEE_TYPE);
	mp.put(UPDATE_EMPLOYEE_TYPE,UPDATE_EMPLOYEE_TYPE);
	
	mp.put(CREATE_LEAVE_TYPE,CREATE_LEAVE_TYPE);
	mp.put(UPDATE_LEAVE_TYPE,UPDATE_LEAVE_TYPE);
	
	mp.put(CREATE_HOLIDAY,CREATE_HOLIDAY);
	mp.put(UPDATE_HOLIDAY,UPDATE_HOLIDAY);
	
	mp.put(CREATE_PROJECT,CREATE_PROJECT);
	mp.put(UPDATE_PROJECT,UPDATE_PROJECT);
	mp.put(DELETE_PROJECT,DELETE_PROJECT);
	
	mp.put(CREATE_SALARY_TYPE,CREATE_SALARY_TYPE);
	mp.put(UPDATE_SALARY_TYPE,UPDATE_SALARY_TYPE);
	mp.put(IMPORT_SALARY_TYPE,IMPORT_SALARY_TYPE);
	
	mp.put(CREATE_DEPARTMENT,CREATE_DEPARTMENT);
	mp.put(UPDATE_DEPARTMENT,UPDATE_DEPARTMENT);
	mp.put(IMPORT_DEPARTMENT,IMPORT_DEPARTMENT);
	
	mp.put(APPLY_LEAVE, APPLY_LEAVE);
	
	mp.put(CREATE_PAYROLL_ADDITION_MASTER, CREATE_PAYROLL_ADDITION_MASTER);
	mp.put(UPDATE_PAYROLL_ADDITION_MASTER, UPDATE_PAYROLL_ADDITION_MASTER);
	mp.put(DELETE_PAYROLL_ADDITION_MASTER, DELETE_PAYROLL_ADDITION_MASTER);
	
	mp.put(CREATE_PAYROLL_DEDUCTION_MASTER, CREATE_PAYROLL_DEDUCTION_MASTER);
	mp.put(UPDATE_PAYROLL_DEDUCTION_MASTER, UPDATE_PAYROLL_DEDUCTION_MASTER);
	mp.put(DELETE_PAYROLL_DEDUCTION_MASTER, DELETE_PAYROLL_DEDUCTION_MASTER);
	
	mp.put(CREATE_PAYROLL, CREATE_PAYROLL);
	mp.put(CREATE_PAYROLL_ADDITION, CREATE_PAYROLL_ADDITION);
	mp.put(CREATE_PAYROLL_DEDUCTION, CREATE_PAYROLL_DEDUCTION);
	mp.put(UPDATE_PAYROLL, UPDATE_PAYROLL);
	
	mp.put(APPLY_CLAIM, APPLY_CLAIM);
	mp.put(APPROVED_CLAIM,APPROVED_CLAIM);
	mp.put(REJECTED_CLAIM, REJECTED_CLAIM);
	
	mp.put(CREATE_CLAIM_PAYMENT, CREATE_CLAIM_PAYMENT);
	mp.put(UPDATE_CLAIM_PAYMENT, UPDATE_CLAIM_PAYMENT);
	
	mp.put(CREATE_PAYROLL_PAYMENT, CREATE_PAYROLL_PAYMENT);
	mp.put(UPDATE_PAYROLL_PAYMENT, UPDATE_PAYROLL_PAYMENT);
	
	mp.put(CREATE_INVOICE_QUOTES,CREATE_INVOICE_QUOTES);
	mp.put(EDIT_INVOICE_QUOTES,EDIT_INVOICE_QUOTES);
	
	mp.put(CREATE_JOURNAL,CREATE_JOURNAL);
	mp.put(EDIT_JOURNAL,EDIT_JOURNAL);
	mp.put(DELETE_JOURNAL,DELETE_JOURNAL);

	mp.put(BULK_PAYSLIP_PROCESSING_SEND_MAIL,BULK_PAYSLIP_PROCESSING_SEND_MAIL);
	
  }
    
  public static Map getTransactionList()
  {
    return mp;
  }
  
  public static final String[] inbounddirtype = {"CREATE_PURCHASE_ORDER","PURCHASER_ORDER_ADD_PRODUCT","PO_ADD_ITEM","PO_UPD_ITEM","PO_DEL_ITEM","UPDATE_PO","PURCHASE_ORDER_REVERSE","ORDER_RECV","FORCE_CLOSE","DELETE_PURCHASE_ORDER","CONVERT_TO_PURCHASE","PURCHASE_ORDER_UPDATE_PRODUCT","PURCHASER_ORDER_DELETE_PRODUCT","UPDATE_PURCHASE_ORDER","PURCHASE_RETURN","CREATE_PURCHASE_ORDER_APPROVED","CREATE_PURCHASE_ORDER_REJECTED","EDIT_PURCHASE_ORDER_APPROVED","EDIT_PURCHASE_ORDER_REJECTED","DELETE_PURCHASE_ORDER_APPROVED","DELETE_PURCHASE_ORDER_REJECTED"}; 
  public static final String[] outbounddirtype = {"CREATE_SALES_ORDER","SALES_ORDER_ADD_PRODUCT","SALES_ORDER_UPDATE_PRODUCT","OB_DEL_ITEM","UPDATE_SALES_ORDER","SALES_ORDER_REVERSE","ORD_PICK_ISSUE","ORDER_ISSUE","SALES_ORDER_TRAN_PICK_IN","SALES_ORDER_TRAN_PICK_OUT","DELETE_SALES_ORDER","SALES_ORDER_PICK_TRAN_OUT","SALES_ORDER_PICK_TRAN_IN","CONVERT_TO_SALES","SALES_ORDER_ISSUE_REVERSE","REJECTED_SALES_ORDER_PRODUCT","SIGNATURE_CAPTURE","SALES_RETURN","SALES_ORDER_DELETE_PRODUCT","INVOICE_PICK_ISSUE","POS_VOID_SALES_ORDER","POS_SALES_RETURN","POS_EXCHANGE"};
  public static final String[] transferdirtype = {"CREATE_CONSIGNMENT_ORDER","CONSIGNMENT_ORDER_ADD_PRODUCT","UPDATE_CONSIGNMENT_ORDER","DELETE_CONSIGNMENT_ORDER","CONSIGNMENT_REVERSE","CONSIGNMENT_ORDER_UPDATE_PRODUCT","CONSIGNMENT_DEL_ITEM","CONSIGNMENT_ORDER_RECV","PICK_CONSIGNMENT_ORDER_OUT","PICK_CONSIGNMENT_ORDER_IN","DEL_CONSIGNMENT_ORDER","CONSIGNMENT_REVERSE_IN","CONSIGNMENT_REVERSE_OUT"};
  public static final String[] loandirtype = {"CREATE_RENTAL_ORDER","RENTAL_AND_SERVICE_ADD_PRODUCT","LOAN_DEL_ITEM","UPDATE_RENTAL_AND_SERVICE_ORDER","RENTAL_AND_SERVICE_PICK_IN","RENTAL_AND_SERVICE_PICK_OUT","RENTAL_AND_SERVICE_RECV_OUT","RENTAL_AND_SERVICE_RECV_IN","DELETE_RENTAL_AND_SERVICE_ORDER","CONSIGNMENT_ORDER_UPDATE_PRODUCT","CONSIGNMENT_DEL_ITEM","TO_REVERSE","TRANSFER_ORDER_RECV","PICK_TRANSFER_ORDER_OUT","PICK_TRANSFER_ORDER_IN","DEL_CONSIGNMENT_ORDER"};
  public static final String[] workdirtype = {"CREATE_WO","WO_ADD_ITEM","WO_UPD_ITEM","WO_DEL_ITEM","UPDATE_WO","MOVE_TO_WIP_OUT","MOVE_TO_WIP_IN","WIP_REVERSE_OUT","WIP_REVERSE_IN","REPORTING_PARENT_OUT","REPORTING_PARENT_IN","CHILD CONSUMED","PARENT_REVERSAL_OUT","PARENT_REVERSAL_IN","CHILD_REVERSAL","WIP-ADJUSTMENT","LABORCLOCK_IN","LABORCLOCK_OUT","DEL_WORK_ORDER"};
  public static final String[] estimatedirtype = {"CREATE_SALES_ESTIMATE","SALES_ESTIMATE_ADD_PRODUCT","SALES_ESTIMATE_ORDER_UPDATE_PRODUCT","SALES_ESTIMATE_DELETE_PRODUCT","UPDATE_SALES_ESTIMATE","DELETE_SALES_ESTIMATE_ORDER","CONVERT_TO_SALES"};
  //public static final String[] multipurchaseestimatedirtype = {};
  public static final String[] purchaseestimatedirtype = {"CREATE_MULTI_PURCHASE_ESTIMATE_ORDER","DELETE_MULTI_PURCHASE_ESTIMATE_ORDER","MULTI_PURCHASER_ESTIMATE_ORDER_ADD_PRODUCT","UPDATE_MULTI_PURCHASE_ESTIMATE_ORDER","MULTI_PURCHASE_ORDER_UPDATE_PRODUCT","DELETE_PURCHASE_ESTIMATE_ORDER","CREATE_PURCHASE_ESTIMATE_ORDER","PURCHASE_ESTIMATE_ORDER_ADD_PRODUCT","PURCHASE_ESTIMATE_ORDER_UPDATE_PRODUCT","UPDATE_PURCHASE_ESTIMATE_ORDER"};
  public static final String[] goods_receipt_Issue = {"CREATE_GRN","GOODSRECEIPT","GOODSISSUE"};
  public static final String[] stockmove = {"STOCK_MOVE_IN","STOCK_MOVE_OUT"};
  public static final String[] stocktake = {"STOCK_TAKE","STOCK_TAKE_OUT","STOCK_TAKE_RESET"};
  public static final String[] kitting = {"KIT_PARENT","CREATE_KITTING","UPDATE_KITTING","DELETE_DEKITTING_BOM","DELETE_DEKITTING_INV","KIT_PARENT_UPLOAD","ADD_KITTING_UPLOAD","DEKITTING","CREATE_KIT_BOM","DELETE_KIT_BOM","UPDATE_KIT_BOM"};
  public static final String[] ProcessingReceive = {"DE-KITTING_IN","DE-KITTING_OUT"};
  public static final String[] SemiProcessedProduct = {"KITTING_IN","KITTING_OUT"};
  public static final String[] expenses = {"CREATE_EXPENSES","EDIT_EXPENSES","DELETE_EXPENSES","UPDATE_EXPENSES_ADD_ACCOUNT","UPDATE_EXPENSES_ADD_ACCOUNT"};
  public static final String[] bill = {"CONVERT_TO_BILL","CREATE_BILL","EDIT_BILL","DELETE_BILL","CANCEL_BILL","REVERSE_BILL"};
  public static final String[] sales_creditnote = {"CREATE_CREDIT_NOTE","EDIT_CREDIT_NOTE","DELETE_CREDIT_NOTE","CANCEL_CREDIT_NOTE"};
  public static final String[] invoice = {"EXPENSES_TO_INVOICE","CONVERT_TO_INVOICE","CREATE_GINO","CREATE_INVOICE","EDIT_INVOICE","DELETE_INVOICE","CANCEL_INVOICE"};
  public static final String[] payment_received = {"CREATE_PAYMENT_RECEIVED","EDIT_PAYMENT_RECEIVED","DELETE_PAYMENT_RECEIVED","UNAPPLY_CREDIT_PAYMENT_RECEIVED","APPLY_CREDIT_PAYMENT_RECEIVED","CHEQUE_DETAILS_RECEIVED","PDC_PROCESS_PAYMENT_RECEIVED","PDC_EDIT_PAYMENT_RECEIVED"};
  public static final String[] payment = {"CREATE_PAYMENT","EDIT_PAYMENT","DELETE_PAYMENT","CHEQUE_DETAILS","UNAPPLY_CREDIT_PAYMENT","APPLY_CREDIT_PAYMENT","PDC_PROCESS_PAYMENT","PDC_EDIT_PAYMENT"};
  public static final String[] purchase_creditnote = {"CREATE_DEBIT_NOTE","EDIT_DEBIT_NOTE","DELETE_DEBIT_NOTE","CANCEL_DEBIT_NOTE"};
  public static final String[] tax_file = {"TAX_SETTINGS_UPDATE","TAX_FILE","CREATE_TAXRETURN_PAYMENT","DELETE_TAXRETURN_PAYMENT","CREATE_TAXFILE_ADJUSTMENT","UPDATE_TAXFILE_ADJUSTMENT"};
  public static final String[] employeetype = {"CREATE_EMPLOYEE_TYPE","UPDATE_EMPLOYEE_TYPE"};
  public static final String[] leavetype = {"CREATE_LEAVE_TYPE","UPDATE_LEAVE_TYPE"};
  public static final String[] bulkpayslip = {"BULK_PAYSLIP_PROCESSING_SEND_MAIL"};
  public static final String[] holiday = {"CREATE_HOLIDAY","UPDATE_HOLIDAY"};
  public static final String[] project = {"CREATE_PROJECT","UPDATE_PROJECT","DELETE_PROJECT"};
  public static final String[] salarytype = {"CREATE_SALARY_TYPE","UPDATE_SALARY_TYPE","IMPORT_SALARY_TYPE"};
  public static final String[] applyleave = {"APPLY_LEAVE"};
  public static final String[] payroll = {"CREATE_PAYROLL_ADDITION_MASTER","UPDATE_PAYROLL_ADDITION_MASTER","DELETE_PAYROLL_ADDITION_MASTER","CREATE_PAYROLL_DEDUCTION_MASTER","UPDATE_PAYROLL_DEDUCTION_MASTER","DELETE_PAYROLL_DEDUCTION_MASTER","CREATE_PAYROLL","UPDATE_PAYROLL","CREATE_PAYROLL_ADDITION","CREATE_PAYROLL_DEDUCTION","APPLY_CLAIM","APPROVED_CLAIM","REJECTED_CLAIM","CREATE_CLAIM_PAYMENT","UPDATE_CLAIM_PAYMENT","CREATE_PAYROLL_PAYMENT","UPDATE_PAYROLL_PAYMENT"};
  public static final String[] department = {"CREATE_DEPARTMENT","UPDATE_DEPARTMENT","IMPORT_DEPARTMENT"};
  public static final String[] shift = {"CREATE_SHIFT","UPDATE_SHIFT"};
  public static final String[] journal = {"CREATE_JOURNAL","EDIT_JOURNAL","DELETE_JOURNAL"};
  
  //for testing
  public static void main(String[] arg)
  {
    //Get Map in Set interface to get key and value
    Map map=TransactionConstants.getTransactionList();
           // Set s=mp.entrySet();
            Set s=map.entrySet();
            
            //Move next key and value of Map by iterator
            Iterator it=s.iterator();

            while(it.hasNext())
            {
                // key=value separator this by Map.Entry to get key and value
                Map.Entry m =(Map.Entry)it.next();

                // getKey is used to get key of Map
                String key=(String)m.getKey();

                // getValue is used to get value of key in Map
                String value=(String)m.getValue();

                System.out.println("Key :"+key+"  Value :"+value);
            }

  }
  
}
