package com.track.db.util;

import java.util.Map;

import javax.transaction.UserTransaction;

import net.sf.json.JSONObject;

import com.track.constants.IConstants;
import com.track.gates.DbBean;
import com.track.tran.WmsMiscReceiveMaterial;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.XMLUtils;

public class MiscReceivingUtil {

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public MiscReceivingUtil() {

	}
        
    public boolean process_MiscReceiveMaterialByRange(Map obj) {
            boolean flag = false;
           	UserTransaction ut = null;
            try {
            		ut = com.track.gates.DbBean.getUserTranaction();
            		ut.begin();
                    flag = process_Wms_MiscReceiving(obj);
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
                    return flag;
            }
            
        return flag;
    }

	public String process_MiscReceiveMaterial(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_MiscReceiving(obj);
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
					"Error in receiving Product : " + obj.get(IConstants.ITEM)
							+ " Order");
			return xmlStr;
		}
		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ (String) obj.get(IConstants.ITEM)
					+ "  received successfully!");
		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Error in receiving Product : "
					+ obj.get(IConstants.ITEM) + " Order");
		}
		return xmlStr;
	}
	public JSONObject process_MiscReceiveMaterial_STD(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		JSONObject resultJson = new JSONObject();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_MiscReceiving(obj);
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
					"Error in receiving Product : " + obj.get(IConstants.ITEM)
							+ " Order");
			
			resultJson.put("message","Error in receiving Product : " + obj.get(IConstants.ITEM)
					+ " Order");
	       resultJson.put("status","0");
	       resultJson.put("LineNo",obj.get("LineNo").toString());
	       
			return resultJson;
		}
		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ (String) obj.get(IConstants.ITEM)
					+ "  received successfully!");
			
			resultJson.put("message","Product : "
					+ (String) obj.get(IConstants.ITEM)
					+ "  received successfully!");
	       resultJson.put("status","1");
	       resultJson.put("LineNo",obj.get("LineNo").toString());
			
		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Error in receiving Product : "
					+ obj.get(IConstants.ITEM) + " Order");
			
			resultJson.put("message","Error in receiving Product : "
					+ obj.get(IConstants.ITEM) + " Order");
	       resultJson.put("status","0");
	       resultJson.put("LineNo",obj.get("LineNo").toString());
		}
		return resultJson;
	}

	public boolean process_MultiMiscReceiveMaterial(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			
			flag = process_Wms_MiscReceiving(obj);
			
		} catch (Exception e) {
			flag = false;
			
		}
		return flag;
	}

	private boolean process_Wms_MiscReceiving(Map map) throws Exception {

		boolean flag = false;

		WmsTran tran = new WmsMiscReceiveMaterial();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);

		return flag;
	}
}