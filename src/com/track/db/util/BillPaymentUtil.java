package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.dao.BillPaymentDAO;
import com.track.util.MLogger;

public class BillPaymentUtil {
	private boolean printLog = MLoggerConstant.BillPaymentUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
	
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public int addPaymentHdr(Hashtable ht, String plant) {
		int billHdrId = 0;
		try {
			billHdrId = billPaymentDAO.addPaymentHdr(ht, plant);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return billHdrId;		
	}
	public int updatePaymentHdr(String paymentHdrId,Hashtable ht, String plant) {
		int billHdrId = 0;
		try {
			billHdrId = billPaymentDAO.updatePaymentHeader(paymentHdrId, ht, plant);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return billHdrId;		
	}
	public boolean addMultiplePaymentDet(List<Hashtable<String, String>> billDetInfoList, String plant){
		boolean flag = false;		
		try {
			flag = billPaymentDAO.addMultiplePaymentDet(billDetInfoList, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean addPaymentAttachments(List<Hashtable<String, String>> paymentAttachmentList, String plant){
		boolean flag = false;		
		try {
			flag = billPaymentDAO.addPaymentAttachments(paymentAttachmentList, plant);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	public List getPaymentDetList(Hashtable ht){
		List paymentDetList = new ArrayList();		
		try {
			paymentDetList = billPaymentDAO.getBillPaymentDetListByType(ht);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paymentDetList;
	}
	
	public boolean updatePaymentDet(String query, Hashtable htCondition, String extCond){
		boolean flag = false;		
		try {
			flag = billPaymentDAO.updatePaymentDet(query, htCondition, extCond);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	public void addPaymentpdc(Hashtable ht, String plant) {
		try {
			billPaymentDAO.addPaymentpdc(ht, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean addMultiplePaymentpdc(List<Hashtable<String, String>> billpdcInfoList, String plant) {
		boolean flag = false;
		try {
			flag = billPaymentDAO.addMultiplePaymentpdc(billpdcInfoList, plant);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
}
