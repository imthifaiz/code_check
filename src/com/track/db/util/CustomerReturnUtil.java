package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CustomerReturnDAO;
import com.track.dao.LoanHdrDAO;
import com.track.gates.DbBean;
import com.track.tran.WmsCustomerReturns;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;

import com.track.util.XMLUtils;


public class CustomerReturnUtil {

	CustomerReturnDAO _CustomerReturnDAO  = new CustomerReturnDAO();
	private boolean printLog = MLoggerConstant.LoanUtil_PRINTPLANTMASTERLOG;
	
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public CustomerReturnUtil() {

	}
	
	public String process_CustomerReturns(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Customer_Returns(obj);
			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			String xmlStr = XMLUtils.getXMLMessage(0,
					"Error in Customer Returns Product : " + obj.get(IConstants.ITEM)
							+ " Order");
			return xmlStr;
		}
		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Customer Returns for Product : "
					+ (String) obj.get(IConstants.ITEM)
					+ "   successfully!");
		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Error in returning Product : "
					+ obj.get(IConstants.ITEM) + " Order");
		}
		return xmlStr;
	}
	public boolean process_CustomerRetunsMultiple(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			
			flag = process_Customer_Returns(obj);
			
		} catch (Exception e) {
			flag = false;
			
		}
		return flag;
	}
	
	
	public String process_CustomerRetunsMultiplePDA(Map obj) {
		boolean flag = false;
	
		UserTransaction ut = null;

	
		
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			
			flag = process_Customer_Returns(obj);
			
			if (flag == true) {
				
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			String xmlStr = XMLUtils.getXMLMessage(0,
					"Error in Returns item : " + obj.get(IConstants.ITEM)
							+ " Order");
			return xmlStr;
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product Returns  successfully!");
		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Product Returns is NOT successfully!");
		}
		return xmlStr;
	}
	
	private boolean process_Customer_Returns(Map obj) throws Exception {
		// TODO Auto-generated method stub
		
		boolean flag = false;
		
		WmsTran tran = new WmsCustomerReturns();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(obj);

		return flag;
		
		
		
	}
	
	public ArrayList getItemDetails(String query, Hashtable ht)
		throws Exception {
	ArrayList al = new ArrayList();
	
	try {
		_CustomerReturnDAO.setmLogger(mLogger);
		
		al = _CustomerReturnDAO.selectItemDetails(query, ht, "");
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}
	return al;
	}
	
	public ArrayList getBatchDetails(String query, Hashtable ht)
		throws Exception {
	ArrayList al = new ArrayList();
	
	try {
		_CustomerReturnDAO .setmLogger(mLogger);
		al = _CustomerReturnDAO.selectBatchDetails(query, ht, "");
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}
	return al;
	}
	
	public ArrayList getOrderNoDetails(String query, Hashtable ht)
		throws Exception {
	ArrayList al = new ArrayList();
	
	try {
		_CustomerReturnDAO .setmLogger(mLogger);
		al = _CustomerReturnDAO.selectOrderNoDetails(query, ht, " ORDER BY dono ");
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}
	return al;
	}

}
