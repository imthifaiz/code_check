package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.InvoiceDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.db.object.InvPaymentAttachment;
import com.track.db.object.InvPaymentDetail;
import com.track.db.object.InvPaymentHeader;
import com.track.util.MLogger;

public class InvoicePaymentUtil {

	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.InvoicePaymentUtil_PRINTPLANTMASTERLOG;
	
	InvoicePaymentDAO invPaymentDAO=new InvoicePaymentDAO();
	ExceptionUtil exceptionUtil=new ExceptionUtil();
	
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
		
	}
	
	public ExceptionUtil addInvoicePaymentHdr(InvPaymentHeader invPaymentHeader,String plant,String user)
	{
		try {
			int receiveHeaderId=invPaymentDAO.addInvoicePaymentHdr(invPaymentHeader, plant,user);
			exceptionUtil.setResultData(receiveHeaderId);
		} catch (Exception e) {
			exceptionUtil.setStatus(ExceptionUtil.Result.ERROR);
			exceptionUtil.setClassName(e.getClass().getName());
			exceptionUtil.setErrorMessage(e.getMessage());
			return exceptionUtil;
		}
		exceptionUtil.setStatus(ExceptionUtil.Result.OK);
		return exceptionUtil;
	}
	public ExceptionUtil updateInvoicePaymentHdr(InvPaymentHeader invPaymentHeader,String plant,String user)
	{
		try {
			int receiveHeaderId=invPaymentDAO.updateInvoicePaymentHdr(invPaymentHeader, plant, user);
			exceptionUtil.setResultData(receiveHeaderId);
		} catch (Exception e) {
			exceptionUtil.setStatus(ExceptionUtil.Result.ERROR);
			exceptionUtil.setClassName(e.getClass().getName());
			exceptionUtil.setErrorMessage(e.getMessage());
			return exceptionUtil;
		}
		exceptionUtil.setStatus(ExceptionUtil.Result.OK);
		return exceptionUtil;
	}
	public ExceptionUtil addInvoicePaymentDet(InvPaymentDetail invPaymentDetail,String plant,String user)
	{
		try {
			invPaymentDAO.addInvoicePaymentDet(invPaymentDetail, plant,user);
		} catch (Exception e) {
			exceptionUtil.setStatus(ExceptionUtil.Result.ERROR);
			exceptionUtil.setClassName(e.getClass().getName());
			exceptionUtil.setErrorMessage(e.getMessage());
			return exceptionUtil;
		}
		exceptionUtil.setStatus(ExceptionUtil.Result.OK);
		return exceptionUtil;
	}
	public ExceptionUtil updateInvoicePaymentDet(InvPaymentDetail invPaymentDetail,String plant,String user)
	{
		try {
			invPaymentDAO.updateInvoicePaymentDet(invPaymentDetail, plant, user);
		} catch (Exception e) {
			exceptionUtil.setStatus(ExceptionUtil.Result.ERROR);
			exceptionUtil.setClassName(e.getClass().getName());
			exceptionUtil.setErrorMessage(e.getMessage());
			return exceptionUtil;
		}
		exceptionUtil.setStatus(ExceptionUtil.Result.OK);
		return exceptionUtil;
	}
	public ExceptionUtil addInvoicePaymentAttachment(InvPaymentAttachment invPaymentAttachment,String plant,String user)
	{
		try {
			invPaymentDAO.addInvoicePaymentAttachment(invPaymentAttachment, plant,user);
		} catch (Exception e) {
			exceptionUtil.setStatus(ExceptionUtil.Result.ERROR);
			exceptionUtil.setClassName(e.getClass().getName());
			exceptionUtil.setErrorMessage(e.getMessage());
			return exceptionUtil;
		}
		exceptionUtil.setStatus(ExceptionUtil.Result.OK);
		return exceptionUtil;
	}
	public ExceptionUtil deleteInvoicePaymentAttachmentById(int ID,String plant,String user)
	{
		try {
			invPaymentDAO.deleteInvoicePaymentAttachmentById(ID, plant, user);
		} catch (Exception e) {
			exceptionUtil.setStatus(ExceptionUtil.Result.ERROR);
			exceptionUtil.setClassName(e.getClass().getName());
			exceptionUtil.setErrorMessage(e.getMessage());
			return exceptionUtil;
		}
		exceptionUtil.setStatus(ExceptionUtil.Result.OK);
		return exceptionUtil;
	}
	public List<InvPaymentHeader> getAllInvoicePayment(String plant,String user) throws Exception
	{
		List<InvPaymentHeader> invoicePayments=new ArrayList<InvPaymentHeader>();
		try {
			 invoicePayments=invPaymentDAO.getAllInvoicePayment(plant,user);
		}
		catch (Exception e) {
			throw e;
		}
		
		return invoicePayments;
	}
	public List<InvPaymentHeader> getAllInvoicePaymentByFilter(String whereQuery,String plant,String user) throws Exception
	{
		List<InvPaymentHeader> invoicePayments=new ArrayList<InvPaymentHeader>();
		try {
			 invoicePayments=invPaymentDAO.getAllInvoicePaymentByFilter(whereQuery, plant, user);
		}
		catch (Exception e) {
			throw e;
		}
		
		return invoicePayments;
	}
	public int updateInvoiceStatus(InvPaymentDetail invPaymentDetail,String Status,String plant,String user) throws Exception
	{
		int invoiceHdrId = 0;
		StringBuffer updateQyery = new StringBuffer("set ");
		Hashtable htCond = new Hashtable();
		htCond.put("PLANT", plant);
		htCond.put("ID", String.valueOf(invPaymentDetail.getINVOICEHDRID()));
		updateQyery.append("BILL_STATUS" + " = '"
				+ Status + "'");
		InvoiceDAO  dao = new InvoiceDAO();
		dao.setmLogger(mLogger);
		invoiceHdrId = dao.update(updateQyery.toString(), htCond,"");
		return invoiceHdrId;
	}
	
	public List getPaymentReceivedDetails(String whereQuery,String plant,String user) throws Exception
	{
		List invoicePayments=new ArrayList();
		try {
			 invoicePayments=invPaymentDAO.getPaymentsMadeSummaryView(whereQuery, plant, user);
		}
		catch (Exception e) {
			throw e;
		}
		
		return invoicePayments;
	}
	
	public String getOutStdAmt(String plant, String custCode , String startDate, String endDate) throws Exception {
		String outstdamt = "";
		Hashtable ht = new Hashtable();
		
		String query = "SELECT ((SELECT ISNULL(SUM(A.TAX + A.UNITPRICE),0) FROM (SELECT B.ITEM,CASE WHEN MAX(A.OUTBOUND_GST)>0 then " +
		"ROUND((MAX(A.OUTBOUND_GST)/100)*ROUND(B.qtyis*B.UNITPRICE,2),2) ELSE 0 END [TAX],"+
		"ROUND(B.qtyis*B.UNITPRICE,2) [UNITPRICE] from ["+plant+"_DOHDR] A LEFT JOIN  ["+plant+"_dodet]B on A.DONO=B.DONO "+
		"WHERE A.CustCode= '"+custCode+"' and A.CollectionDate >= '"+startDate+"' and A.CollectionDate <= '"+endDate+"'"+
		"GROUP BY QTYIS,UNITPRICE,ITEM,A.DONO,B.DOLNNO) A)-(SELECT ISNULL(SUM(AMOUNTRECEIVED - AMOUNTREFUNDED),0)  FROM ["+plant+"_FINRECEIVEHDR] "+
		"WHERE CUSTNO='"+custCode+"' AND RECEIVE_DATE >= '"+startDate+"' AND RECEIVE_DATE <= '"+endDate+"')) AS OUTSTDAMT";

		invPaymentDAO.setmLogger(mLogger);
		outstdamt = invPaymentDAO.getOutStdAmt(query);
		return outstdamt;
	}
}
