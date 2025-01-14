package com.track.constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/*********
 * Modification History ****************** Bruhan,June 18 2014, Description: To
 * include MobileSalesServlet Constants Bruhan,June 25 2014, Description: To
 * include MobileSalesHandlerServlet Constants Bruhan,June 25 2014, Description:
 * To include WmsMobileSales Constants Bruhan,Dec 5 2014, Description: To include
 * WmsPutAway_PRINTPLANTMASTERLOG,WmsPutAway_PRINTPLANTMASTERINFO Bruhan,20 jan
 * 2015,Description: To include
 * WmsTOPickReceive_PRINTPLANTMASTERLOG,WmsTOPickReceive_PRINTPLANTMASTERINFO
 * 
 * 
 */
public class MLoggerConstant {
	

	public static Boolean TRANSPORTMODEServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TRANSPORTMODERServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean TRANSPORTMODE_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TRANSPORTMODEDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	//ADDED BY NAVAS
	public static boolean ESTTRANSFERHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static boolean ESTTRANSFERHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static boolean ESTTRANSFERDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static boolean ESTTRANSFERDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static boolean ESTHDR_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static boolean ESTHDR_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static boolean ESTDET_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static boolean ESTDET_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	//END
	// ADDED BY JAYESH
	public static boolean ESTIMATEHDR_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static boolean ESTIMATEHDR_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static boolean CARTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static boolean CARTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	// END

	public static Boolean SETTINGSSERVLET_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean COUNTRYNCURRENCYDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean COUNTRYNCURRENCYDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POSPaymentDetailDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean POSPaymentDetailDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean CUSTMSTDAO_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean CUSTMSTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CUSTMSTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CUSTOMERBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CUSTOMERBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CLEARANCEBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CLEARANCEBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SHIPPERDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SHIPPERDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean OUTLETBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean OUTLETBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CYCLECOUNTBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CYCLECOUNTBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CYCLECOUNTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CYCLECOUNTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean DOHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DOHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean DNPLHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DNPLHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean DNPLDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DNPLDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean DOTRANSFERHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DOTRANSFERHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean DOTRANSFERDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DOTRANSFERDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean DODETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DODETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean INVMSTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean INVMSTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean BOMDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BOMDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean INVSESBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean INVSESBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ITEMLOCBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ITEMLOCBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ITEMMAPBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ITEMMAPBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ITEMMSTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ITEMMSTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ITEMSESBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ITEMSESBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean LBLDET_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LBLDET_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean LOANDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LOANDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean LOANHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LOANHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean LOCMSTBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LOCMSTBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean LOCMSTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean GSTTYPEDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean GSTTYPEDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean LOCMSTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ORDERTYPEBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ORDERTYPEBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ORDERTYPEDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ORDERTYPEDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean MOVHISBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MOVHISBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean MOVHISDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MOVHISDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PLANTMSTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PLANTMSTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean POBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PODETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PODETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean POHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	
	public static Boolean POSITEMPROMOTIONDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POSITEMPROMOTIONDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean POSITEMPROMOTIONHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POSITEMPROMOTIONHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
//	Create By Resvi; Description - MultiPurchaseEstimate
	public static Boolean POMULTIDETDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POMULTIDETDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean POMULTIHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POMULTIHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	
//	Create By Resvi; Description - PurchaseEstimate
	public static Boolean POESTDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POESTDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean POESTHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POESTHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	
	public static Boolean POUtil_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PackingUtil_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean POSHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean POSHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POSDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean POSDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean PRDCLASSDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PRDCLASSDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	
	//Resvi Include For Product Department
	public static Boolean PRDDEPTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PRDDEPTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	//Ends
	public static Boolean CLEARAGENTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CLEARAGENTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	
	public static Boolean CATEGORYPROMOTIONHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CATEGORYPROMOTIONHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CATEGORYPROMOTIONDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CATEGORYPROMOTIONDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	
	public static Boolean BRANDPROMOTIONHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BRANDPROMOTIONHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean BRANDPROMOTIONDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BRANDPROMOTIONDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean PRDTYPEDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PRDTYPEDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean RECVDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean RECVDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean REPORTSESBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean REPORTSESBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean RSNMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean RSNMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SOBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SOBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SHIPHISDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SALESDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SALESDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SHIPHISDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean STOCKTAKEDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean STOCKTAKEDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TBLCONTROLBEANDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TBLCONTROLBEANDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TBLCONTROLDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TBLCONTROLDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TODETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TODETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TOHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TOHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean VENDMSTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean VENDMSTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean LocTypeDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LocTypeDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean OrderStatusDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean OrderStatusDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean CYCLECOUNTUTIL_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CYCLECOUNTUTIL_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean DEFAULTSBEAN_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DEFAULTSBEAN_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean MISCBEAN_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MISCBEAN_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SEARCHBEAN_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SEARCHBEAN_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SELECTBEAN_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SELECTBEAN_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SQLBEAN_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SQLBEAN_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean USERBEAN_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean USERBEAN_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean AUDITDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean AUDITDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean DODET_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HTReportUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean InvMstUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean InvUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ItemMstUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ItemUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean VendUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LoanUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LocUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean CustomerReturnUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean PosUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean GstType_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean OrderType_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MovHisUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PlantMstUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SOUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean StockTakeUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TblControlUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TOUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DOUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DeleveryOrderServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DeleveryOrderServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean FileDownloaderServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean FileDownloaderServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LoanOrderPickingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LoanOrderPickingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LoanOrderReceivingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LoanOrderReceivingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LoanOrderServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LoanOrderServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LocMstServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LocMstServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean GstTypeServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean GstTypeServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean OrderTypeServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean OrderTypeServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean ReportServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ReportServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean PurchaseOrderServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PurchaseOrderServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean SyncServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SyncServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean TransferOrderServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TransferOrderServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean InvQueryServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CycleCountServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CycleCountServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean CycleCount_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CycleCount_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean CommonValidationServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CommonValidationServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean DoPickingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DoPickingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean Inbound_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean Inbound_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean InvQueryServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LoanPickingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LoanPickingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LoanReceivingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LoanReceivingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LocationTransfer_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LocationTransfer_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LocationTransferServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LocationTransferServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean Login_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean Login_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean MiscOrderIssuingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MiscOrderIssuingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean MiscOrderReceivingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MiscOrderReceivingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	
	//RESVI Add For Multipoestimate
	public static Boolean MultiPoUtil_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean MultiPoUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean CustomerReturnServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CustomerReturnServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean CustomerReturnServletjson_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CustomerReturnServletjson_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean OrderIssuingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean OrderIssuingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean OrderReceivingByPOServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean OrderReceivingByPOServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean PdaReceivingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PdaReceivingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean OrderReceivingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ProductionHanderPDA_KITTINGINFO = Boolean.valueOf(true);
	public static Boolean ProductionHanderPDA_KITTINGLOG = Boolean.valueOf(true);

	public static Boolean ProductionHanderPDA_DEKITTINGINFO = Boolean.valueOf(true);
	public static Boolean ProductionHanderPDA_DEKITTINGLOG = Boolean.valueOf(true);

	public static Boolean OrderReceivingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LocationType_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean Outbound_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean Outbound_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean PackingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PackingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean PickingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PickingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean PoRecv_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PoRecv_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean QueryInventoryServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean QueryInventoryServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean ShipmentConfirmServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ShipmentConfirmServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean StockTakeServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean StockTakeServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean StockTakeServletInvUpload_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean StockTakeServletInvUpload_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean TransferPickingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TransferPickingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean TransferReceivingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TransferReceivingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsCycleCount_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsCycleCount_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsIssueMaterial_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsIssueMaterial_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsPickReversal_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsPickReversal_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean WmsLoanOrderPicking_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsToReceiveMaterial_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsToReceiveMaterial_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsTOPicking_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsTOPicking_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean WmsDOTransferPicking_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsDOTransferPicking_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean WmsStockTake_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsShipConfirm_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsShipConfirm_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsReceiveMaterial_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsReceiveMaterialRandom_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean WmsReceiveMaterial_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsReceiveMaterialRandom_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsPicking_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsPickingRandom_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean WmsPickIssue_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean WmsPicking_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsPickingRandom_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsMiscReceiveMaterial_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsMiscReceiveMaterial_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsMiscIssueMaterial_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsMiscIssueMaterial_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsStockTake_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsLoanOrderPicking_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsLoanOrderReceving_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsLoanOrderReceving_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsOrderDetailsClose_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsOrderDetailsClose_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsLocationTransfer_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsLocationTransfer_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean PlantMstDAO_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean DODET_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean ATTMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ATTMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BATMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean BATMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BINMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean BINMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BOMMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean BOMMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CLASSMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CLASSMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CUSTMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CUSTMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CYCLECNT_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CYCLECNT_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DOHDR_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DOHDR_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean INVCNT_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean INVCNT_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean INVMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean INVMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ITEMCLATT_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ITEMCLATT_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ITEMMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ITEMMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PCKALLOC_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PCKALLOC_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PCKDET_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PCKDET_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PLNTMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PLNTMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PLTMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PLTMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PODET_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PODET_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean POHDR_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POHDR_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	
	//RESVI ADD FOR MULTIPURCHASEESTIMATE
	public static Boolean PO_MULTI_ESTDET_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PO_MULTI_ESTDET_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PO_MULTI_ESTHDR_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PO_MULTI_ESTHDR_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	
	//RESVI ADD FOR PURCHASEESTIMATE
	public static Boolean POESTDET_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POESTDET_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean POESTHDR_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean POESTHDR_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	
	public static Boolean PRODMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PRODMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean RECVHIS_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean RECVHIS_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SODET_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SODET_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SOHDR_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SOHDR_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TODET_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TODET_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TOHDR_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TOHDR_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean VENDMST_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean VENDMST_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean DashboardServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DashboardServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean MiscOrderHandlingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MiscOrderHandlingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean ItemMstServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ItemMstServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean InvMstServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean InvMstServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static String PROPS_FOLDER = "C:/S2TCONFIGTEST/";
	//public static String PROPS_FOLDER = "C:/S2TCONFIGLIVEUAE/";
	//public static String PROPS_FOLDER = "C:/S2TCONFIGLIVE/";
	private static String DB_PROPS_FILE = PROPS_FOLDER + "/track/config/MLogger.properties";
	public static Boolean InboundOrderHandlerServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean InboundOrderHandlerServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LoanOrderHandlerServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LoanOrderHandlerServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean TransferOrderHandlerServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TransferOrderHandlerServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean OutboundOrderHandlerServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean OutboundOrderHandlerServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean PosServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PosServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean ProcessingReceiveDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ProcessingReceiveDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean SemiFinishedProductDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SemiFinishedProductDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean KITTING_PRODUCTHANDLERINFO = Boolean.valueOf(true);
	public static Boolean KITTING_PRODUCTHANDLERLOG = Boolean.valueOf(true);

	public static Boolean DEKITTING_PRODUCTHANDLERINFO = Boolean.valueOf(true);
	public static Boolean DEKITTING_PRODUCTHANDLERLOG = Boolean.valueOf(true);

	public static Boolean mobilecustomerregisterUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean mobilecustomerregisterUtil_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean mobilecustomerregisterUtil_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean mobilecustomerregisterservlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean mobilecustomerregisterservlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean mobilecustomerregisterservlet_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean mobileadditionalchargeUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean mobileadditionalchargeUti_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean mobileadditionalcharge_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean EmailMsgDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean EmailMsgDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean EmailMsgUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean TimeSlotServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean TimeSlotServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean TimeSlotUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TimeSlotUtil_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean TimeSlotUtil_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean TIMESLOTDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TIMESLOTDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean PRODUCTBRANDServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PRODUCTBRANDServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean PRDBRANDDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PRDBRANDDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean DOMultiPickIssueServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean DOMultiPickIssueServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean CUSTOMERCOMPANYDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CUSTOMERCOMPANYDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CUSTOMERLOGINDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CUSTOMERLOGINDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean BANKServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BANKServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean BANKDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean BANKDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean TempDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TempDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TempUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean WmsTOReversal_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsTOReversal_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean WmsOBISSUEReversal_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsOBISSUEReversal_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean ProductionBomServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ProductionBomServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean ProductionBomDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ProductionBomDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean ProductionBomUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	

	public static Boolean EmployeeDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean EmployeeDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	

	public static Boolean WmsIBRECEIVEReversal_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsIBRECEIVEReversal_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean ProductServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ProductServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean LabelPrintServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LabelPrintServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LabelPrintUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean LabelPrintDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LabelPrintDAO_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean LabelPrintDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean MobileSalesServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MobileSalesServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean MobileSalesHandlerServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MobileSalesHandlerServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean WmsMobileSales_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsMobileSales_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean WmsWIPReportingReversal_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsWIPReportingReversal_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean WmsWIPAdjustment_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsWIPAdjustment_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean WmsPutAway_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsPutAway_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean WmsTOPickReceive_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsTOPickReceive_PRINTPLANTMASTERINFO = Boolean.valueOf(true);

	public static Boolean EventDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean EventDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean TimeTrackingDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TimeTrackingDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean TimeTrackingUtil_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TimeTrackingUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean ClockInOutHandlerServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean ClockInOutHandlerServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean LaborTimeTrackingDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean LaborTimeTrackingDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean LaborTimeTrackingUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean EstimateServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean EstimateServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean ESTUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ESTHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ESTHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ESTDetDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ESTDetDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);

	public static Boolean OrderPaymentDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean OrderPaymentDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CustomerStatusServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CustomerStatusServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean WmsProcessSignatureCapture_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsProcessSignatureCapture_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static boolean EstimateOrderServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static boolean EstimateOrderServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean MasterUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MasterDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean MasterDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	
	public static Boolean WmsDNPL_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean PaymentTermsServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PaymentTermsServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean PayTermsUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean PayTermsDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PayTermsDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean BillingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BillingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean BillUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BillDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean BillDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean ChartOfAccountServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ChartOfAccountServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean CoaUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CoaDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CoaDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean ExpensesServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ExpensesServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean ExpensesUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ExpensesDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ExpensesDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean InvoiceServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean InvoiceServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean InvoiceUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean InvoiceDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean InvoiceDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean BillPaymentServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BillPaymentServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean BillPaymentUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean BillPaymentDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean BillPaymentDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean InvoicePayment_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean InvoicePayment_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean InvoicePaymentUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean InvoicePaymentDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean InvoicePaymentDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean SupplierCreditServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SupplierCreditServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean SupplierCreditUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean SupplierCreditDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SupplierCreditDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean AgeingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean AgeingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean AgeingUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean AgeingDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean AgeingDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean ReturnOrderServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ReturnOrderServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean ReturnOrderUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ReturnOrderDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ReturnOrderDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean WmsPOReversalReversal_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean WmsPOReversalReversal_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	
	public static Boolean CustomerCreditNoteServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CustomerCreditNoteServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean CustomerCreditnoteUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CustomerCreditnoteDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CustomerCreditnoteDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean TaxSettingServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TaxSettingServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean TaxSettingUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TaxSettingDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TaxSettingDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean TaxReturnServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TaxReturnServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean TaxReturnUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TaxReturnDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TaxReturnDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean JournalServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean JournalServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean JournalUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean JournalDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean JournalDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean TrialBalanceServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TrialBalanceServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean TrialBalanceUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean TrialBalanceDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean TrialBalanceDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean GeneralLedgerServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean GeneralLedgerServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean GeneralLedgerUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean GeneralLedgerDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean GeneralLedgerDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean ProfitLossServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ProfitLossServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean CashFlowServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CashFlowServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	public static Boolean ProfitLossUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ProfitLossDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ProfitLossDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean CommonEmailServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean CommonEmailServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	
	public static Boolean HrEmpTypeDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrEmpTypeDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrPayrollAdditionMstDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrPayrollAdditionMstDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrLeaveTypeDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrLeaveTypeDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean HrShiftDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrShiftDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrPayrollAdditionDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrPayrollAdditionDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrDeductionDetDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrDeductionDetDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrDeductionHdrDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrDeductionHdrDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrPayrollDETDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrPayrollDETDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrPayrollHDRDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrPayrollHDRDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean EmployeeLeaveDetDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean EmployeeLeaveDetDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean ClearAgentTypeDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ClearAgentTypeDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean EmpAttachDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean EmpAttachDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean SupplierAttachDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SupplierAttachDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean CustomerAttachDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean CustomerAttachDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean PurchaseAttachDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PurchaseAttachDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	//RESVI ADDS FOR MULTIPURCHASEESTIMATE
	public static Boolean multiPurchaseEstAttachDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean multiPurchaseEstAttachDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean SalesAttachDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean SalesAttachDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean ConsignmentAttachDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ConsignmentAttachDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean EstimateAttachDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean EstimateAttachDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrHolidayMstDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrHolidayMstDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrEmpSalaryDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrEmpSalaryDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrEmpSalaryDetDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrEmpSalaryDetDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrDepartmentDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrDepartmentDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrLeaveApplyHdrDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrLeaveApplyHdrDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrLeaveApplyDetDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrLeaveApplyDetDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrClaimDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrClaimDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean HrPayrollPaymentDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean HrPayrollPaymentDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean HrPayrollPaymentServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean HrPayrollPaymentServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	
	public static Boolean FinCountryTaxTypeDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean FinCountryTaxTypeDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean ShiftServlet_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	public static Boolean ShiftServlet_PRINTPLANTMASTERINFO = Boolean.valueOf(true);
	
	public static Boolean PaymentModeMstDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PaymentModeMstDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean FinProjectDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean FinProjectDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean FinProjectAttachDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean FinProjectAttachDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static boolean LocTypeTwoDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true); //imti added frm loctypetwodao
	public static boolean LocTypeTwoDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true); //imti added frm loctypetwodao
	
	public static Boolean INTEGRATIONSUtil_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean INTEGRATIONSDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean INTEGRATIONSDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean ParentChildCmpDetDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ParentChildCmpDetDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean QuotesHdrDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean QuotesHdrDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean QuotesDetDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean QuotesDetDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean StoreHdrDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean StoreHdrDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean StoreDetDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean StoreDetDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean PltApprovalMatrixDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PltApprovalMatrixDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean POHDRAPPROVALDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean POHDRAPPROVALDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean PODETAPPROVALDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PODETAPPROVALDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);

	public static Boolean DOHDRAPPROVALDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean DOHDRAPPROVALDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean DODETAPPROVALDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean DODETAPPROVALDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean PEPPOL_RECEIVED_DATADAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PEPPOL_RECEIVED_DATADAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean PEPPOL_DOC_IDSDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean PEPPOL_DOC_IDSDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	public static Boolean ReconciliationHdrDAO_PRINTPLANTMASTERQUERY = Boolean.valueOf(true);
	public static Boolean ReconciliationHdrDAO_PRINTPLANTMASTERLOG = Boolean.valueOf(true);
	
	static {
		loadMLoggerConstant();
	}

	public static void loadMLoggerConstant() {
		
		System.out.println("Logger Constant Loading from " + DB_PROPS_FILE);
		Properties dbpr;
		InputStream dbip;
		try {
			dbip = new FileInputStream(new File(DB_PROPS_FILE));
			dbpr = new Properties();
			dbpr.load(dbip);

			/** QUERY **/
			CUSTMSTDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.CustMstDAO.PrintPlantMasterQuery"));
			CUSTOMERBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.CustomerBeanDAO.PrintPlantMasterQuery"));
			SHIPPERDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ShipperDAO.PrintPlantMasterQuery"));
			OUTLETBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.OutletBeanDAO.PrintPlantMasterQuery"));
			CYCLECOUNTBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.CycleCountBeanDAO.PrintPlantMasterQuery"));
			CYCLECOUNTDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.CycleCountDAO.PrintPlantMasterQuery"));
			DOHDRDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.DoHdrDAO.PrintPlantMasterQuery"));
			DODETDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.DoDetDAO.PrintPlantMasterQuery"));
			INVMSTDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.InvMstDAO.PrintPlantMasterQuery"));
			INVSESBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.InvSesBeanDAO.PrintPlantMasterQuery"));
			ITEMLOCBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ItemLocBeanDAO.PrintPlantMasterQuery"));
			ITEMMAPBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ItemMapBeanDAO.PrintPlantMasterQuery"));
			ITEMMSTDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ItemMstDAO.PrintPlantMasterQuery"));
			ITEMSESBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ItemSesBeanDAO.PrintPlantMasterQuery"));
			LBLDET_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LblDet.PrintPlantMasterQuery"));
			LOANDETDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LoanDetDAO.PrintPlantMasterQuery"));
			LOANHDRDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LoanHdrDAO.PrintPlantMasterQuery"));
			LOCMSTBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LocMstBeanDAO.PrintPlantMasterQuery"));
			LOCMSTDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LocMstDAO.PrintPlantMasterQuery"));
			MOVHISBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.MovHisBeanDAO.PrintPlantMasterQuery"));
			MOVHISDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.MovHisDAO.PrintPlantMasterQuery"));
			POHDRDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoHdrDAO.PrintPlantMasterQuery"));
			
			
			POMULTIHDRDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.MultiPoEstHdrDAO.PrintPlantMasterQuery"));
			
			PLANTMSTDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PlantMstDAO.PrintPlantMasterQuery"));
			POBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.POBeanDAO.PrintPlantMasterQuery"));
			PODETDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoDetDAO.PrintPlantMasterQuery"));
			POMULTIDETDETDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.multiPoEstDetDAO.PrintPlantMasterQuery"));
			POHDRDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoHdrDAO.PrintPlantMasterQuery"));
			POMULTIHDRDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoHdrDAO.PrintPlantMasterQuery"));
			
			
			POESTHDRDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoEstHdrDAO.PrintPlantMasterQuery"));
			POESTDETDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoEstDetDAO.PrintPlantMasterQuery"));
			
			
			PRDCLASSDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PrdClassDAO.PrintPlantMasterQuery"));
			PRDTYPEDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PrdTypeDAO.PrintPlantMasterQuery"));
			RECVDETDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.RecvDetDAO.PrintPlantMasterQuery"));
			REPORTSESBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ReportSesBeanDAO.PrintPlantMasterQuery"));
			RSNMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.RsnMst.PrintPlantMasterQuery"));
			SOBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.SOBeanDAO.PrintPlantMasterQuery"));
			SHIPHISDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ShipHisDAO.PrintPlantMasterQuery"));
			STOCKTAKEDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.StockTakeDAO.PrintPlantMasterQuery"));
			TBLCONTROLBEANDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.TblControlBeanDAO.PrintPlantMasterQuery"));
			TBLCONTROLDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.TblControlDAO.PrintPlantMasterQuery"));
			TODETDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ToDetDAO.PrintPlantMasterQuery"));
			TOHDRDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ToHdrDAO.PrintPlantMasterQuery"));
			VENDMSTDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.VendMstDAO.PrintPlantMasterQuery"));
			CYCLECOUNTUTIL_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.CycleCountUtil.PrintPlantMasterQuery"));
			DEFAULTSBEAN_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.defaultsBean.PrintPlantMasterQuery"));
			MISCBEAN_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.miscBean.PrintPlantMasterQuery"));
			SEARCHBEAN_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.searchBean.PrintPlantMasterQuery"));
			SELECTBEAN_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.selectBean.PrintPlantMasterQuery"));
			SQLBEAN_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.sqlBean.PrintPlantMasterQuery"));
			USERBEAN_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.userBean.PrintPlantMasterQuery"));
			ATTMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.ATTMST.PrintPlantMasterQuery"));
			BATMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.BATMST.PrintPlantMasterQuery"));
			BINMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.BINMST.PrintPlantMasterQuery"));
			BOMMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.BOMMST.PrintPlantMasterQuery"));
			CLASSMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.CLASSMST.PrintPlantMasterQuery"));
			CUSTMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.CUSTMST.PrintPlantMasterQuery"));
			CYCLECNT_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.CYCLECNT.PrintPlantMasterQuery"));
			DOHDR_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.DOHDR.PrintPlantMasterQuery"));
			INVCNT_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.INVCNT.PrintPlantMasterQuery"));
			INVMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.INVMST.PrintPlantMasterQuery"));
			ITEMCLATT_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.ITEMCLATT.PrintPlantMasterQuery"));
			ITEMMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.ITEMMST.PrintPlantMasterQuery"));
			PCKALLOC_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PCKALLOC.PrintPlantMasterQuery"));
			PCKDET_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PCKDET.PrintPlantMasterQuery"));
			PLNTMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PLNTMST.PrintPlantMasterQuery"));
			PLTMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PLTMST.PrintPlantMasterQuery"));
			PODET_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PODET.PrintPlantMasterQuery"));
			POHDR_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.POHDR.PrintPlantMasterQuery"));
			PRODMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PRODMST.PrintPlantMasterQuery"));
			RECVHIS_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.RECVHIS.PrintPlantMasterQuery"));
			SODET_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.SODET.PrintPlantMasterQuery"));
			SOHDR_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.SOHDR.PrintPlantMasterQuery"));
			TODET_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.TODET.PrintPlantMasterQuery"));
			TOHDR_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.TOHDR.PrintPlantMasterQuery"));
			VENDMST_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.VENDMST.PrintPlantMasterQuery"));

			/** LOG **/

			AUDITDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.AuditDAO.PrintPlantMasterLog"));
			HTReportUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.HTReportUtil.PrintPlantMasterLog"));
			InvMstUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.InvMstUtil.PrintPlantMasterLog"));
			InvUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.InvUtil.PrintPlantMasterLog"));
			ItemMstUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.ItemMstUtil.PrintPlantMasterLog"));
			ItemUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.ItemUtil.PrintPlantMasterLog"));
			LoanUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.LoanUtil.PrintPlantMasterLog"));
			LocUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.LocUtil.PrintPlantMasterLog"));

			CustomerReturnUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.LoanUtil.PrintPlantMasterLog"));
			CustomerReturnUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.LocUtil.PrintPlantMasterLog"));

			MovHisUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.MovHisUtil.PrintPlantMasterLog"));
			PlantMstUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.PlantMstUtil.PrintPlantMasterLog"));
			POUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.POUtil.PrintPlantMasterLog"));
			MultiPoUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.MultiPoUtil.PrintPlantMasterLog"));
			SOUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.SOUtil.PrintPlantMasterLog"));
			StockTakeUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.StockTakeUtil.PrintPlantMasterLog"));
			TblControlUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.TblControlUtil.PrintPlantMasterLog"));
			TOUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.TOUtil.PrintPlantMasterLog"));

			DeleveryOrderServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.DeleveryOrderServlet.PrintPlantMasterLog"));
			FileDownloaderServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.FileDownloaderServlet.PrintPlantMasterLog"));
			LoanOrderPickingServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.LoanOrderPickingServlet.PrintPlantMasterLog"));
			LoanOrderReceivingServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.LoanOrderReceivingServlet.PrintPlantMasterLog"));
			LoanOrderServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.LoanOrderServlet.PrintPlantMasterLog"));
			LocMstServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.LocMstServlet.PrintPlantMasterLog"));
			ReportServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.ReportServlet.PrintPlantMasterLog"));
			PurchaseOrderServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.PurchaseOrderServlet.PrintPlantMasterLog"));
			SyncServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.SyncServlet.PrintPlantMasterLog"));
			TransferOrderServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.TransferOrderServlet.PrintPlantMasterLog"));

			ShiftServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.ShiftServlet.PrintPlantMasterLog"));
			
			InvQueryServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.InvQueryServlet.PrintPlantMasterLog"));
			CycleCountServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.CycleCountServlet.PrintPlantMasterLog"));
			CycleCount_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.CycleCount.PrintPlantMasterLog"));
			CommonValidationServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.CommonValidationServlet.PrintPlantMasterLog"));
			DoPickingServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.DoPickingServlet.PrintPlantMasterLog"));
			Inbound_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.Inbound.PrintPlantMasterLog"));
			LoanPickingServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.LoanPickingServlet.PrintPlantMasterLog"));
			LoanReceivingServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.LoanReceivingServlet.PrintPlantMasterLog"));
			LocationTransfer_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.LocationTransfer.PrintPlantMasterLog"));
			LocationTransferServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.LocationTransferServlet.PrintPlantMasterLog"));
			Login_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.Login.PrintPlantMasterLog"));
			MiscOrderIssuingServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.MiscOrderIssuingServlet.PrintPlantMasterLog"));
			MiscOrderReceivingServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.MiscOrderReceivingServlet.PrintPlantMasterLog"));
			OrderIssuingServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.OrderIssuingServlet.PrintPlantMasterLog"));
			OrderReceivingByPOServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.OrderReceivingByPOServlet.PrintPlantMasterLog"));
			PdaReceivingServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.PdaReceivingServlet.PrintPlantMasterLog"));
			OrderReceivingServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.OrderReceivingServlet.PrintPlantMasterLog"));
			Outbound_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.Outbound.PrintPlantMasterLog"));
			PackingServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.PackingServlet.PrintPlantMasterLog"));
			PickingServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.PickingServlet.PrintPlantMasterLog"));
			PoRecv_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.PoRecv.PrintPlantMasterLog"));
			QueryInventoryServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.QueryInventoryServlet.PrintPlantMasterLog"));
			ShipmentConfirmServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.ShipmentConfirmServlet.PrintPlantMasterLog"));
			StockTakeServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.StockTakeServlet.PrintPlantMasterLog"));
			TransferPickingServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.TransferPickingServlet.PrintPlantMasterLog"));
			TransferReceivingServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.TransferReceivingServlet.PrintPlantMasterLog"));

			WmsCycleCount_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsCycleCount.PrintPlantMasterLog"));
			WmsIssueMaterial_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsIssueMaterial.PrintPlantMasterLog"));
			WmsPickReversal_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsPickReversal.PrintPlantMasterLog"));
			WmsLoanOrderPicking_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsLoanOrderPicking.PrintPlantMasterLog"));
			WmsToReceiveMaterial_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsToReceiveMaterial.PrintPlantMasterLog"));
			WmsTOPicking_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsTOPicking.PrintPlantMasterLog"));
			WmsShipConfirm_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsShipConfirm.PrintPlantMasterLog"));
			WmsReceiveMaterial_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsReceiveMaterial.PrintPlantMasterLog"));
			WmsPicking_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsPicking.PrintPlantMasterLog"));

			WmsPickIssue_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsPickIssue.PrintPlantMasterLog"));
			WmsMiscReceiveMaterial_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.tran.WmsMiscReceiveMaterial.PrintPlantMasterLog"));
			WmsMiscIssueMaterial_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsMiscIssueMaterial.PrintPlantMasterLog"));
			WmsStockTake_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsStockTake.PrintPlantMasterLog"));
			WmsLoanOrderReceving_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsLoanOrderReceving.PrintPlantMasterLog"));
			WmsLocationTransfer_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsLocationTransfer.PrintPlantMasterLog"));

			CUSTMSTDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.CustMstDAO.PrintPlantMasterLog"));
			CUSTOMERBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.CustomerBeanDAO.PrintPlantMasterLog"));
			SHIPPERDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ShipperDAO.PrintPlantMasterLog"));
			OUTLETBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.OutletBeanDAO.PrintPlantMasterLog"));
			CYCLECOUNTBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.CycleCountBeanDAO.PrintPlantMasterLog"));
			CYCLECOUNTDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.CycleCountDAO.PrintPlantMasterLog"));
			DOHDRDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.DoHdrDAO.PrintPlantMasterLog"));
			DODETDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.DoDetDAO.PrintPlantMasterLog"));
			INVMSTDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.InvMstDAO.PrintPlantMasterLog"));
			INVSESBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.InvSesBeanDAO.PrintPlantMasterLog"));
			ITEMLOCBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ItemLocBeanDAO.PrintPlantMasterLog"));
			ITEMMAPBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ItemMapBeanDAO.PrintPlantMasterLog"));
			ITEMMSTDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ItemMstDAO.PrintPlantMasterLog"));
			ITEMSESBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ItemSesBeanDAO.PrintPlantMasterLog"));
			LBLDET_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LblDet.PrintPlantMasterLog"));
			LOANDETDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LoanDetDAO.PrintPlantMasterLog"));
			LOANHDRDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LoanHdrDAO.PrintPlantMasterLog"));
			LOCMSTBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LocMstBeanDAO.PrintPlantMasterLog"));
			LOCMSTDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LocMstDAO.PrintPlantMasterLog"));
			MOVHISBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.MovHisBeanDAO.PrintPlantMasterLog"));
			MOVHISDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.MovHisDAO.PrintPlantMasterLog"));
			POHDRDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoHdrDAO.PrintPlantMasterLog"));
			
			POMULTIHDRDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.MultiPoEstHdrDAO.PrintPlantMasterLog"));
			
			PLANTMSTDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PlantMstDAO.PrintPlantMasterLog"));
			POBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.POBeanDAO.PrintPlantMasterLog"));
			PODETDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoDetDAO.PrintPlantMasterLog"));
			POMULTIDETDETDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.multiPoEstDetDAO.PrintPlantMasterLog"));
			
			POESTDETDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoEstDetDAO.PrintPlantMasterLog"));
			
			
			POESTHDRDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoEstHdrDAO.PrintPlantMasterLog"));
			
			
			POHDRDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PoHdrDAO.PrintPlantMasterLog"));
			
			POMULTIHDRDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.MultiPoEstHdrDAO.PrintPlantMasterLog"));
			
			PRDCLASSDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PrdClassDAO.PrintPlantMasterLog"));
			PRDTYPEDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PrdTypeDAO.PrintPlantMasterLog"));
			RECVDETDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.RecvDetDAO.PrintPlantMasterLog"));
			REPORTSESBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ReportSesBeanDAO.PrintPlantMasterLog"));
			RSNMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.RsnMst.PrintPlantMasterLog"));
			SHIPHISDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ShipHisDAO.PrintPlantMasterLog"));
			SOBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.SOBeanDAO.PrintPlantMasterLog"));
			STOCKTAKEDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.StockTakeDAO.PrintPlantMasterLog"));
			TBLCONTROLBEANDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.TblControlBeanDAO.PrintPlantMasterLog"));
			TBLCONTROLDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.TblControlDAO.PrintPlantMasterLog"));
			TODETDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ToDetDAO.PrintPlantMasterLog"));
			TOHDRDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ToHdrDAO.PrintPlantMasterLog"));
			VENDMSTDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.VendMstDAO.PrintPlantMasterLog"));
			CYCLECOUNTUTIL_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.CycleCountUtil.PrintPlantMasterLog"));
			DEFAULTSBEAN_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.defaultsBean.PrintPlantMasterLog"));
			MISCBEAN_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.miscBean.PrintPlantMasterLog"));
			SEARCHBEAN_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.searchBean.PrintPlantMasterLog"));
			SELECTBEAN_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.selectBean.PrintPlantMasterLog"));
			SQLBEAN_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.sqlBean.PrintPlantMasterLog"));
			USERBEAN_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.gates.userBean.PrintPlantMasterLog"));
			ATTMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.ATTMST.PrintPlantMasterLog"));
			BATMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.BATMST.PrintPlantMasterLog"));
			BINMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.BINMST.PrintPlantMasterLog"));
			BOMMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.BOMMST.PrintPlantMasterLog"));
			CLASSMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.CLASSMST.PrintPlantMasterLog"));
			CUSTMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.CUSTMST.PrintPlantMasterLog"));
			CYCLECNT_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.CYCLECNT.PrintPlantMasterLog"));
			DOHDR_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.DOHDR.PrintPlantMasterLog"));
			INVCNT_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.INVCNT.PrintPlantMasterLog"));
			INVMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.INVMST.PrintPlantMasterLog"));
			INVMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.INVMST.PrintPlantMasterLog"));
			ITEMMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.ITEMMST.PrintPlantMasterLog"));
			PCKALLOC_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PCKALLOC.PrintPlantMasterLog"));
			PCKDET_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PCKDET.PrintPlantMasterLog"));
			PLNTMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PLNTMST.PrintPlantMasterLog"));
			PLTMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PLTMST.PrintPlantMasterLog"));
			PODET_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PODET.PrintPlantMasterLog"));
			POHDR_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.POHDR.PrintPlantMasterLog"));
			PRODMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.PRODMST.PrintPlantMasterLog"));
			RECVHIS_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.RECVHIS.PrintPlantMasterLog"));
			SODET_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.SODET.PrintPlantMasterLog"));
			SOHDR_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.SOHDR.PrintPlantMasterLog"));
			TODET_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.TODET.PrintPlantMasterLog"));
			TOHDR_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.TOHDR.PrintPlantMasterLog"));
			VENDMST_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tables.VENDMST.PrintPlantMasterLog"));

			/** INFO **/

			DeleveryOrderServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.DeleveryOrderServlet.PrintPlantMasterInfo"));
			FileDownloaderServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.FileDownloaderServlet.PrintPlantMasterInfo"));
			LoanOrderPickingServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.LoanOrderPickingServlet.PrintPlantMasterInfo"));
			LoanOrderReceivingServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.LoanOrderReceivingServlet.PrintPlantMasterInfo"));
			LoanOrderServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.LoanOrderServlet.PrintPlantMasterInfo"));
			LocMstServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.LocMstServlet.PrintPlantMasterInfo"));
			ReportServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.ReportServlet.PrintPlantMasterInfo"));
			PurchaseOrderServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.PurchaseOrderServlet.PrintPlantMasterInfo"));
			SyncServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.SyncServlet.PrintPlantMasterInfo"));
			TransferOrderServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.TransferOrderServlet.PrintPlantMasterInfo"));

			ShiftServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.ShiftServlet.PrintPlantMasterInfo"));
			
			CycleCountServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.CycleCountServlet.PrintPlantMasterInfo"));
			CycleCount_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.CycleCount.PrintPlantMasterInfo"));
			CommonValidationServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.CommonValidationServlet.PrintPlantMasterInfo"));
			DoPickingServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.DoPickingServlet.PrintPlantMasterInfo"));
			Inbound_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.Inbound.PrintPlantMasterInfo"));
			InvQueryServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.InvQueryServlet.PrintPlantMasterInfo"));
			LoanPickingServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.LoanPickingServlet.PrintPlantMasterInfo"));
			LoanReceivingServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.LoanReceivingServlet.PrintPlantMasterInfo"));
			LocationTransfer_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.LocationTransfer.PrintPlantMasterInfo"));
			LocationTransferServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.LocationTransferServlet.PrintPlantMasterInfo"));
			Login_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.Login.PrintPlantMasterInfo"));
			MiscOrderIssuingServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.MiscOrderIssuingServlet.PrintPlantMasterInfo"));
			MiscOrderReceivingServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.MiscOrderReceivingServlet.PrintPlantMasterInfo"));

			CustomerReturnServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.CustomerReturnServlet.PrintPlantMasterInfo"));
			CustomerReturnServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.CustomerReturnServlet.PrintPlantMasterInfo"));

			CustomerReturnServletjson_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.CustomerReturnServletjson.PrintPlantMasterInfo"));
			CustomerReturnServletjson_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.CustomerReturnServletjson.PrintPlantMasterInfo"));

			OrderIssuingServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.OrderIssuingServlet.PrintPlantMasterInfo"));
			OrderReceivingByPOServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.OrderReceivingByPOServlet.PrintPlantMasterInfo"));
			PdaReceivingServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.PdaReceivingServlet.PrintPlantMasterInfo"));
			OrderReceivingServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.OrderReceivingServlet.PrintPlantMasterInfo"));
			Outbound_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.Outbound.PrintPlantMasterInfo"));
			PackingServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.PackingServlet.PrintPlantMasterInfo"));
			PickingServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.PickingServlet.PrintPlantMasterInfo"));
			PoRecv_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.PoRecv.PrintPlantMasterInfo"));
			QueryInventoryServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.QueryInventoryServlet.PrintPlantMasterInfo"));
			ShipmentConfirmServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.ShipmentConfirmServlet.PrintPlantMasterInfo"));
			StockTakeServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.StockTakeServlet.PrintPlantMasterInfo"));
			TransferReceivingServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.TransferReceivingServlet.PrintPlantMasterInfo"));
			TransferPickingServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.pda.TransferPickingServlet.PrintPlantMasterInfo"));

			WmsCycleCount_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsCycleCount.PrintPlantMasterInfo"));
			WmsIssueMaterial_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsIssueMaterial.PrintPlantMasterInfo"));
			WmsPickReversal_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsPickReversal.PrintPlantMasterInfo"));
			WmsToReceiveMaterial_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsToReceiveMaterial.PrintPlantMasterInfo"));
			WmsTOPicking_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsTOPicking.PrintPlantMasterInfo"));
			WmsStockTake_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsStockTake.PrintPlantMasterInfo"));
			WmsShipConfirm_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsShipConfirm.PrintPlantMasterInfo"));
			WmsReceiveMaterial_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsReceiveMaterial.PrintPlantMasterInfo"));
			WmsPicking_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsPicking.PrintPlantMasterInfo"));
			WmsMiscReceiveMaterial_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.tran.WmsMiscReceiveMaterial.PrintPlantMasterInfo"));
			WmsMiscIssueMaterial_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsMiscIssueMaterial.PrintPlantMasterInfo"));
			WmsLoanOrderPicking_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsLoanOrderPicking.PrintPlantMasterInfo"));
			WmsLoanOrderReceving_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsLoanOrderReceving.PrintPlantMasterInfo"));
			WmsLocationTransfer_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsLocationTransfer.PrintPlantMasterInfo"));
			PlantMstDAO_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PlantMstDAO.PrintPlantMasterInfo"));

			MiscOrderHandlingServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.MiscOrderHandlingServlet.PrintPlantMasterLog"));
			MiscOrderHandlingServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.MiscOrderHandlingServlet.PrintPlantMasterInfo"));
			ItemMstServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.ItemMstServlet.PrintPlantMasterLog"));
			ItemMstServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.ItemMstServlet.PrintPlantMasterInfo"));
			InvMstServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.InvMstServlet.PrintPlantMasterLog"));
			InvMstServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.InvMstServlet.PrintPlantMasterInfo"));
			mobilecustomerregisterUtil_PRINTPLANTMASTERLOG = Boolean.parseBoolean(dbpr
					.getProperty("MLogger.com.track.mobile.db.util.mobilecustomerregisterUtil.PrintPlantMasterLog"));
			mobileadditionalchargeUtil_PRINTPLANTMASTERLOG = Boolean.parseBoolean(dbpr
					.getProperty("MLogger.com.track.mobile.db.util.mobileadditionalchargeUtil.PrintPlantMasterLog"));
			mobilecustomerregisterservlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(dbpr
					.getProperty("MLogger.com.track.mobile.servlet.mobilecustomerregisterservlet.PrintPlantMasterLog"));
			TimeSlotUtil_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.mobile.db.util.TimeSlotUtil.PrintPlantMasterLog"));

			EmailMsgDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.EmailMsgDAO.PrintPlantMasterLog"));
			EmailMsgDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.EmailMsgDAO.PrintPlantMasterQuery"));
			EmailMsgUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.EmailMsgUtil.PrintPlantMasterLog"));
			SETTINGSSERVLET_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.SettingsServlet.PrintPlantMasterLog"));
			COUNTRYNCURRENCYDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.CountryNCurrencyDAO.PrintPlantMasterLog"));
			COUNTRYNCURRENCYDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.CountryNCurrencyDAO.PrintPlantMasterQuery"));

			PRODUCTBRANDServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.ProductBrandServlet.PrintPlantMasterLog"));
			PRDBRANDDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PrdBrandDAO.PrintPlantMasterQuery"));
			PRDBRANDDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PrdBrandDAO.PrintPlantMasterLog"));
					
					BANKServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.BankServlet.PrintPlantMasterLog"));
			BANKDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.BankDAO.PrintPlantMasterQuery"));
			BANKDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.BankDAO.PrintPlantMasterLog"));

			POSPaymentDetailDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.POSPaymentDetailDAO.PrintPlantMasterQuery"));
			POSPaymentDetailDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.POSPaymentDetailDAO.PrintPlantMasterLog"));

			TempDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.TempDAO.PrintPlantMasterQuery"));

			TempDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.TempDAO.PrintPlantMasterLog"));
			TempUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.TempUtil.PrintPlantMasterLog"));

			LocTypeDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LocTypeDAO.PrintPlantMasterQuery"));

			LocTypeDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LocTypeDAO.PrintPlantMasterLog"));

			
			System.out.println("Logger Constant Loaded from " + DB_PROPS_FILE + " file");

			OrderStatusDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.OrderStatusDAO.PrintPlantMasterQuery"));

			OrderStatusDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.OrderStatusDAO.PrintPlantMasterLog"));

			ProductionBomServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.ProductionBomServlet.PrintPlantMasterLog"));
			ProductionBomServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.ProductionBomServlet.PrintPlantMasterInfo"));

			ProductionBomDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ProductionBomDAO.PrintPlantMasterQuery"));

			ProductionBomDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ProductionBomDAO.PrintPlantMasterLog"));

			ProductionBomUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.ProductionBomUtil.PrintPlantMasterLog"));


			EmployeeDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.EmployeeDAO.PrintPlantMasterQuery"));
			EmployeeDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.EmployeeDAO.PrintPlantMasterLog"));

			WmsIBRECEIVEReversal_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsIBRECEIVEReversal.PrintPlantMasterLog"));
			WmsIBRECEIVEReversal_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.tran.WmsIBRECEIVEReversal.PrintPlantMasterQuery"));


			LabelPrintServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.LabelPrintServlet"));
			LabelPrintServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.LabelPrintServlet"));

			LabelPrintUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.LabelPrintUtil"));

			LabelPrintDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LabelPrintDAO"));
			LabelPrintDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LabelPrintDAO"));
			LabelPrintDAO_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LabelPrintDAO"));

			MobileSalesServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.MobileSalesServlet"));
			MobileSalesServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.MobileSalesServlet"));

			MobileSalesHandlerServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.MobileSalesHandlerServlet"));
			MobileSalesHandlerServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.pda.MobileSalesHandlerServlet"));

			WmsMobileSales_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsMobileSales"));
			WmsMobileSales_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsMobileSales"));
			// System.out.println("Logger Constant Loaded from
			// C:\\props\\NSeries\\config\\MLogger.properties file");
			System.out.println("Logger Constant Loaded from " + DB_PROPS_FILE + " file");

			WmsWIPReportingReversal_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.tran.WmsWIPReportingReversal.PrintPlantMasterInfo"));
			WmsWIPReportingReversal_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.tran.WmsWIPReportingReversal.PrintPlantMasterLog"));
			WmsWIPAdjustment_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsWIPAdjustment.PrintPlantMasterInfo"));
			WmsWIPAdjustment_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsWIPAdjustment.PrintPlantMasterLog"));

			WmsPutAway_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsPutAway.PrintPlantMasterLog"));
			WmsPutAway_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsPutAway.PrintPlantMasterInfo"));

			WmsTOPickReceive_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsTOPickReceive.PrintPlantMasterLog"));
			WmsTOPickReceive_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsTOPickReceive.PrintPlantMasterInfo"));

			EventDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.EventDAO.PrintPlantMasterQuery"));
			EventDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.EventDAO.PrintPlantMasterLog"));

			TimeTrackingDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.TimeTrackingDAO.PrintPlantMasterQuery"));
			TimeTrackingDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.TimeTrackingDAO.PrintPlantMasterLog"));

			TimeTrackingUtil_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.TimeTrackingUtil.PrintPlantMasterQuery"));
			TimeTrackingUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.TimeTrackingUtil.PrintPlantMasterLog"));

			ClockInOutHandlerServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.ClockInOutHandlerServlet.PrintPlantMasterInfo"));
			ClockInOutHandlerServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.ClockInOutHandlerServlet.PrintPlantMasterLog"));

			LaborTimeTrackingDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LaborTimeTrackingDAO.PrintPlantMasterQuery"));
			LaborTimeTrackingDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.LaborTimeTrackingDAO.PrintPlantMasterLog"));
			LaborTimeTrackingUtil_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.db.util.LaborTimeTrackingUtil.PrintPlantMasterLog"));

			EstimateServlet_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.EstimateServlet.PrintPlantMasterInfo"));
			EstimateServlet_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.servlet.EstimateServlet.PrintPlantMasterLog"));
			ESTUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.ESTUtil.PrintPlantMasterLog"));
			ESTHDRDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.EstHdrDAO.PrintPlantMasterQuery"));
			ESTHDRDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.EstHdrDAO.PrintPlantMasterLog"));
			ESTDetDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.EstDetDAO.PrintPlantMasterQuery"));
			ESTDetDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.EstDetDAO.PrintPlantMasterLog"));

			OrderPaymentDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.OrderPaymentDAO.PrintPlantMasterQuery"));
			OrderPaymentDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.OrderPaymentDAO.PrintPlantMasterLog"));
			CustomerStatusServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.CustomerStatusServlet.PrintPlantMasterLog"));
			CustomerStatusServlet_PRINTPLANTMASTERINFO = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.CustomerStatusServlet.PrintPlantMasterInfo"));

			WmsProcessSignatureCapture_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PlantMstDAO.PrintPlantMasterLog"));
			WmsProcessSignatureCapture_PRINTPLANTMASTERINFO = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.PlantMstDAO.PrintPlantMasterInfo"));

			MasterUtil_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.db.util.MasterUtil"));
			MasterDAO_PRINTPLANTMASTERQUERY = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.MasterDAO.PrintPlantMasterQuery"));
			MasterDAO_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.dao.MasterDAO.PrintPlantMasterLog"));
			WmsDNPL_PRINTPLANTMASTERLOG = Boolean
					.parseBoolean(dbpr.getProperty("MLogger.com.track.tran.WmsDNPL.PrintPlantMasterLog"));
			
			
			ExpensesServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.ExpensesServlet.PrintPlantMasterLog"));
			ExpensesUtil_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.db.util.ExpensesUtil"));

InvoiceServlet_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.servlet.InvoiceServlet.PrintPlantMasterLog"));
			InvoiceUtil_PRINTPLANTMASTERLOG = Boolean.parseBoolean(
					dbpr.getProperty("MLogger.com.track.db.util.InvoiceUtil"));
						
			System.out.println("Logger Constant Loaded from " + DB_PROPS_FILE + " file");
		} catch (FileNotFoundException fnfe) {
			showException("Please copy the file from " + DB_PROPS_FILE);
		} catch (Exception e) {
				showException("Please copy the file from " + DB_PROPS_FILE);
		}
	}

	private static void showException(String exp) {
		System.out.println(" ");
		System.out.println(
				"Exception ################ Exception #################\n" + "system" + ":\t" + "system" + ":\t" + exp);
		System.out.println("Exception ################ Exception #################");
		System.out.println(" ");

	}

}
