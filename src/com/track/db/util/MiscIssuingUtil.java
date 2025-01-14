package com.track.db.util;

import java.util.Map;

import javax.transaction.UserTransaction;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.track.constants.IConstants;
import com.track.gates.DbBean;
import com.track.tran.WmsMiscIssueMaterial;
import com.track.tran.WmsPosReceiveTran;
import com.track.tran.WmsPosTran;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.XMLUtils;

public class MiscIssuingUtil {

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public MiscIssuingUtil() {

	}
	//added by Bruhan
	public boolean process_SingleMiscIssueMaterial(Map obj){
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_MiscIssuing(obj);

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
	//end
	public String process_MiscIssueMaterial(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_MiscIssuing(obj);

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
					"Error in Issuing item : " + obj.get(IConstants.ITEM)
							+ " Order");
			return xmlStr;
		}
		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ (String) obj.get(IConstants.ITEM)
					+ "  Issued successfully!");

		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Error in receiving item : "
					+ obj.get(IConstants.ITEM) + " Order");
		}
		return xmlStr;
	}
	public JSONObject process_MiscIssueMaterial_STD(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		JSONObject resultJson = new JSONObject();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_MiscIssuing(obj);

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
					"Error in Issuing item : " + obj.get(IConstants.ITEM)
							+ " Order");
			resultJson.put("message","Error in Issuing item : " + obj.get(IConstants.ITEM)
					+ " Order");
	        resultJson.put("status","0");
	        resultJson.put("LineNo",obj.get("LineNo").toString());
	        
			return resultJson;
		}
		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ (String) obj.get(IConstants.ITEM)
					+ "  Issued successfully!");
			resultJson.put("message","Product : "
					+ (String) obj.get(IConstants.ITEM)
					+ "  Issued successfully!");
	        resultJson.put("status","1");
	        resultJson.put("LineNo",obj.get("LineNo").toString());

		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Error in receiving item : "
					+ obj.get(IConstants.ITEM) + " Order");
			resultJson.put("message","Error in receiving item : "
					+ obj.get(IConstants.ITEM) + " Order");
	        resultJson.put("status","0");
	        resultJson.put("LineNo",obj.get("LineNo").toString());
		}
		return resultJson;
	}
	public boolean process_MultiMiscIssueMaterial(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			
			flag = process_Wms_MiscIssuing(obj);

			
		} catch (Exception e) {
			flag = false;
			
		}
		
		return flag;
	}    
        
    public boolean process_MiscIssueMaterialByRange(Map obj) throws Exception{
            boolean flag = false;
            UserTransaction ut = null;
            try {
            		ut = com.track.gates.DbBean.getUserTranaction();
            		ut.begin();
                    flag = process_Wms_MiscIssuing(obj);
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
                this.mLogger.exception(true, "", e);
                throw new Exception( "Error in picking Product : Invalid Batch range details ");
            }
           
            return flag;
    }


	public boolean process_PosIssueMaterial(Map obj)throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
		//	ut = com.track.gates.DbBean.getUserTranaction();
			//ut.begin();
			flag = process_Pos_Issuing(obj);

		/*	if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}*/
		} catch (Exception e) {
			flag = false;
			/*	DbBean.RollbackTran(ut);
			String xmlStr = XMLUtils.getXMLMessage(0,
					"Error in Issuing Product : " + obj.get(IConstants.ITEM)
							+ " Order");*/
			throw e;
			
		}
		String xmlStr = "";

		return flag;
	}

	private boolean process_Wms_MiscIssuing(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsMiscIssueMaterial();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}
	private boolean process_Pos_Issuing(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsPosTran();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}
	public boolean process_PosReceiveMaterial(Map obj)throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
		
			flag = process_Pos_Receiving(obj);

		
		} catch (Exception e) {
			flag = false;
			
			throw e;
			
		}
		
		return flag;
	}
	private boolean process_Pos_Receiving(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsPosReceiveTran();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}
}