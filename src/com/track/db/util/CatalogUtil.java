package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;
import com.track.constants.MLoggerConstant;
import com.track.dao.CatalogDAO;
import com.track.gates.DbBean;
import com.track.tran.WmsMobileEnquiry;
import com.track.tran.WmsMobileRegistration;
import com.track.tran.WmsMobileShopping;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class CatalogUtil {
	private boolean printLog = MLoggerConstant.LoanUtil_PRINTPLANTMASTERLOG;
	private CatalogDAO _catalogdao;  
		private MLogger mLogger = new MLogger();

		public MLogger getmLogger() {
			return mLogger;
		}

		public void setmLogger(MLogger mLogger) {
			this.mLogger = mLogger;
		}
public CatalogUtil()
{
	_catalogdao = new CatalogDAO();
}
			public boolean process_mobileshopping(Map obj) {
				boolean flag = false;
				UserTransaction ut = null;
				try {
					ut = com.track.gates.DbBean.getUserTranaction();
					ut.begin();
					flag = process_Wms_Checkout(obj);
			
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
					
				}
				return flag;
			}
			public boolean process_mobileenquiry(Map obj) {
				boolean flag = false;
				UserTransaction ut = null;
				try {
					ut = com.track.gates.DbBean.getUserTranaction();
					ut.begin();
					flag = process_Wms_mobileenq(obj);
			
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
					
				}
				return flag;
			}
			public boolean process_mobileregistration(Map obj) {
				boolean flag = false;
				UserTransaction ut = null;
				try {
					ut = com.track.gates.DbBean.getUserTranaction();
					ut.begin();
					flag = process_Wms_mobileregister(obj);
			
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
					
				}
				return flag;
			}
	private boolean process_Wms_Checkout(Map map) throws Exception {
				boolean flag = false;
                        try{

                                    WmsTran tran = new WmsMobileShopping();
                                    ((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
                                    flag = tran.processWmsTran(map);
                        }catch(Exception e){
                            flag = false;
                            throw e;
                        }
				return flag;
			}
	private boolean process_Wms_mobileenq(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsMobileEnquiry();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}
	private boolean process_Wms_mobileregister(Map map) throws Exception {
		boolean flag = false;
		WmsTran tran = new WmsMobileRegistration();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
		
	}
	
		public boolean insertMst(Hashtable ht) {
			boolean inserted = false;
			try {
				_catalogdao.setmLogger(mLogger);
				inserted = _catalogdao.saveMst(ht);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return inserted;
		}	
		public boolean updateMst(Hashtable htupdate,Hashtable htcondtn) {
			boolean updated = false;
			try {
				_catalogdao.setmLogger(mLogger);
				updated = _catalogdao.updateMst(htupdate, htcondtn);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return updated;
		}	

	/**
	 * Author Bruhan. 12 july 2012 method :
	 * delMst(Hashtable htcondtn) description : Deletes the Calatog from the catalog master)
	 * 
	 * @param : Hashtable htcondtn
	 * @return : Boolean 
	 * @throws Exception
	 */
	public boolean delMst(Hashtable<String, String> htcondtn) {
		boolean deleted = false;
		try {
			_catalogdao.setmLogger(mLogger);
			deleted = _catalogdao.delMst(htcondtn);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return deleted;
	}
public ArrayList listCatalogs(String query,Hashtable ht,String extcondtn) throws Exception {
   
	List lst =  new ArrayList();
  _catalogdao.setmLogger(mLogger);
    lst=_catalogdao.selectMst(query, ht, extcondtn);
     return (ArrayList) lst;
}


public ArrayList getCatalogList(String plant,String aItem,String ItemDesc,String extrCond,int start,int end,String prdclsid,String prdtypeid,String prdbrandid) throws Exception {
	   
	List lst =  new ArrayList();
  _catalogdao.setmLogger(mLogger);
    lst=_catalogdao.getCatalogList(plant, aItem, ItemDesc, extrCond, start, end,prdclsid, prdtypeid, prdbrandid);
     return (ArrayList) lst;
}


public ArrayList listCatalogDet(String query,Hashtable ht,String extcondtn) throws Exception {
	   
	List lst =  new ArrayList();
  _catalogdao.setmLogger(mLogger);
    lst=_catalogdao.selectMst(query, ht, extcondtn);
     return (ArrayList) lst;
}

public ArrayList listPrevNextCatalogs(String query,Hashtable ht,String extcondtn) throws Exception {
	   
	List lst =  new ArrayList();
  _catalogdao.setmLogger(mLogger);
    lst=_catalogdao.prevNextList(query, ht, extcondtn);
     return (ArrayList) lst;
}
public boolean isExistCatalog(Hashtable ht,String extcondtn) throws Exception {
	 boolean flag= false;  
	List lst =  new ArrayList();
  _catalogdao.setmLogger(mLogger);
  flag=_catalogdao.isExisit(ht, extcondtn);
     return flag;
}
public int NoofRecords(Hashtable ht,String extcondtn) throws Exception {
	 int noofrecords=0;
 _catalogdao.setmLogger(mLogger);
 noofrecords=_catalogdao.getCount(ht, extcondtn);
    return noofrecords;
}
}
