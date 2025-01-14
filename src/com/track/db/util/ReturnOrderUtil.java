package com.track.db.util;

import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.track.dao.ReturnOrderDAO;
import com.track.gates.DbBean;
import com.track.tran.WmsPOReversal;
import com.track.tran.WmsSOReversal;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;

@SuppressWarnings({"rawtypes"})
public class ReturnOrderUtil {
//	private boolean printLog = MLoggerConstant.ReturnOrderUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private ReturnOrderDAO returnOrderDAO = new ReturnOrderDAO();
	
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public List<Map<String, String>> getPurchaseOrderReturnDetails(String pono, String grno, String bill, String plant){
		List<Map<String, String>> returnOrderList = null;
		try {
			returnOrderList = returnOrderDAO.getPurchaseOrderReturnDetails(pono, grno, bill, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnOrderList;
	}
	
	public boolean process_PurchaseOrderReversal(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;

		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			
			flag = process_Wms_PurchaseOrderReversal(obj);
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			flag = false;			
			throw e;
		}

		return flag;
	}
	
    public boolean process_Wms_PurchaseOrderReversal(Map map) throws Exception {
		boolean flag = false;
		WmsTran tran = new WmsPOReversal();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}
    
    public List<Map<String, String>> getPurchaseOrderReturnByGrouping(String pono, String grno, 
			String bill, String vendno, String frmDate, String toDate, String plant){
		List<Map<String, String>> returnOrderList = null;
		try {
			returnOrderList = returnOrderDAO.getPurchaseOrderReturnByGrouping(pono, grno, bill, vendno, frmDate, toDate, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnOrderList;
	}
    
    public List<Map<String, String>> getPurchaseOrderReturnByGroupingforsummary(String pono, String grno, 
			String bill, String vendno, String frmDate, String toDate, String plant){
		List<Map<String, String>> returnOrderList = null;
		try {
			returnOrderList = returnOrderDAO.getPurchaseOrderReturnByGroupingforsummary(pono, grno, bill, vendno, frmDate, toDate, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnOrderList;
	}
    
    public List<Map<String, String>> getPOReturnDetails(String pono, String grno, 
			String bill, String vendno, String returnDate, String plant){
		List<Map<String, String>> returnOrderList = null;
		try {
			returnOrderList = returnOrderDAO.getPOReturnDetails(pono, grno, bill, vendno, returnDate, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnOrderList;
	}
    
    public List<Map<String, String>> getSalesOrderReturnDetails(String dono, String invoiceno,String gino, String plant){
		List<Map<String, String>> returnOrderList = null;
		try {
			returnOrderList = returnOrderDAO.getSalesOrderReturnDetails(dono, invoiceno,gino, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnOrderList;
	}
    
    public boolean process_SalesOrderReversal(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;

		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			
			flag = process_Wms_SalesOrderReversal(obj);
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			flag = false;			
			throw e;
		}

		return flag;
	}
    
    public boolean process_Wms_SalesOrderReversal(Map map) throws Exception {
		boolean flag = false;
		WmsTran tran = new WmsSOReversal();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	} 
    
    public List<Map<String, String>> getSalesOrderReturnByGrouping(String pono,String gino, String invoice, 
			String custno, String frmDate, String toDate, String plant){
		List<Map<String, String>> returnOrderList = null;
		try {
			returnOrderList = returnOrderDAO.getSalesOrderReturnByGrouping(pono, gino, invoice, custno, frmDate, toDate, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnOrderList;
	}
    
    public List<Map<String, String>> getSOReturnDetails(String pono, String invoice, 
			String custno, String returnDate, String plant){
		List<Map<String, String>> returnOrderList = null;
		try {
			returnOrderList = returnOrderDAO.getSOReturnDetails(pono, invoice, custno, returnDate, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnOrderList;
	}
    
    public List<Map<String, String>> getSOReturnDetailsBYso(String sorno,String dono,String gino, String invoice, String plant){
		List<Map<String, String>> returnOrderList = null;
		try {
			returnOrderList = returnOrderDAO.getSOReturnDetailsBYso(sorno,dono,gino, invoice,plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnOrderList;
	}
    
    public List<Map<String, String>> getPOReturnDetailsbyprno(String prno,String pono, String grno, String plant){
		List<Map<String, String>> returnOrderList = null;
		try {
			returnOrderList = returnOrderDAO.getPOReturnDetailsbyprno(prno,pono, grno, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnOrderList;
	}
}
